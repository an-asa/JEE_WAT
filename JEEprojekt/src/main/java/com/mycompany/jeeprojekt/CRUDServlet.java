/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jeeprojekt;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.mycompany.jeeprojekt.DBRow;
import com.mycompany.jeeprojekt.DBInterface;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletContext;

/**
 *
 * @author PRO
 */
@WebServlet(name = "CRUDServlet", urlPatterns = {"/CRUDServlet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 3, maxRequestSize = 1024 * 1024 * 5)
public class CRUDServlet extends HttpServlet {
    
    private DBInterface dbi;

    public CRUDServlet() throws ClassNotFoundException, SQLException {
        super();
        dbi = new DBInterface();
    }
    
    private String extractFileName(Part part) {

        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                clientFileName = clientFileName.replace("\\", "/");
                int i = clientFileName.lastIndexOf('/');
                return clientFileName.substring(i + 1);
            }
        }
        return null;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        InputStream blobstream = null;
        Part file = request.getPart("file");
        DBRow row = new DBRow();
        
        if(file != null){
            blobstream = file.getInputStream();
        }
        
        row.setUser(request.getParameter("user"));
        row.setDescription(request.getParameter("description"));
        String id = request.getParameter("id");
        
        if(id == null || id.isEmpty())
        {
            try {
                String filename = extractFileName(file);
                dbi.Create(row,blobstream,filename);
                response.getWriter().print("Row created by user " + row.getUser() + " with file " + row.getFilename());
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
                response.getWriter().print("Failure to create row by user " + row.getUser() + " with file " + row.getFilename());
            }
        }
        else
        {
            try {
                String filename = extractFileName(file);
                row.setId(Integer.parseInt(id));
                dbi.Update(row,blobstream,filename);
                response.getWriter().print("Row at id=" + id + "updated by user " + row.getUser() + " with file " + row.getFilename());
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
                response.getWriter().print("Row at id=" + id + "failed to be updated by user " + row.getUser() + " with file " + row.getFilename());
            }
        }
        
        RequestDispatcher view = request.getRequestDispatcher("/viewDocs.jsp");
        try {
            request.setAttribute("rows", dbi.Read());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        view.forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String forward="";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                dbi.Delete(id);
                forward = "/viewDocs.jsp";    
                request.setAttribute("rows", dbi.Read());
                RequestDispatcher view = request.getRequestDispatcher(forward);
                view.forward(request, response);
                response.getWriter().print("Deleted row with id=" + id);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
                PrintWriter pw = response.getWriter();
                response.getWriter().print("Failed to delete row");
            }
        } else if (action.equalsIgnoreCase("edit")){
            try {
                forward = "/editDocs.jsp";
                int id = Integer.parseInt(request.getParameter("id"));
                DBRow row = dbi.Read(id);
                request.setAttribute("row", row);
                RequestDispatcher view = request.getRequestDispatcher(forward);
                view.forward(request, response);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equalsIgnoreCase("list")){
            try {
                forward = "/viewDocs.jsp";
                request.setAttribute("rows", dbi.Read());
                RequestDispatcher view = request.getRequestDispatcher(forward);
                view.forward(request, response);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equalsIgnoreCase("insert")){
            forward = "/editDocs.jsp";
            RequestDispatcher view = request.getRequestDispatcher(forward);
            view.forward(request, response);
        } else if (action.equalsIgnoreCase("download")){
            
            try {
                               
                int id = Integer.parseInt(request.getParameter("id"));
                
                String dbURL = "jdbc:derby://localhost:1527/ProjDB";
                String dbUser = "root";
                String dbPass = "wcy";
                
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                
                Connection conn = DriverManager.getConnection(dbURL,dbUser,dbPass);
                
                PreparedStatement statement = conn.prepareStatement
                ("select * from docs where id=?");
                statement.setInt(1, id);
                
                ResultSet rs = statement.executeQuery();
                
                if(rs.next()){
                    
                    String filename = rs.getString("docname");
                    Blob file = rs.getBlob("docfile");
                    InputStream inputStream = file.getBinaryStream();
                    
                    ServletContext context = getServletContext();

                    String mimeType = context.getMimeType(filename);
                    if (mimeType == null) {        
                        mimeType = "application/octet-stream";
                    }

                    response.setContentType(mimeType);
                    
                    String headerKey = "Content-Disposition";
                    String headerValue = String.format("attachment; filename=\"%s\"", filename);
                    response.setHeader(headerKey, headerValue);

                    OutputStream outStream = response.getOutputStream();

                    byte[] buffer = new byte[4096];
                    int length;
                    
                    while ((length = inputStream.read(buffer)) > 0){
                        outStream.write(buffer, 0, length);
                    }

                    inputStream.close();
                    outStream.flush(); 
                }
                
                conn.close();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CRUDServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        } else {
            forward = "/editDocs.jsp";
            RequestDispatcher view = request.getRequestDispatcher(forward);
            view.forward(request, response);
        }

    }

}
