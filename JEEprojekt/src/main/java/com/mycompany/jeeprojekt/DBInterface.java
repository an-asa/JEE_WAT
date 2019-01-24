/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jeeprojekt;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.mycompany.jeeprojekt.DBRow;

/**
 *
 * @author PRO
 */
public class DBInterface {
        
    public boolean Create(DBRow row, InputStream blobstream, String filename) throws SQLException, IOException, ClassNotFoundException{
                          
        String dbURL = "jdbc:derby://localhost:1527/ProjDB";
        String dbUser = "root";
        String dbPass = "wcy";

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        Connection conn = DriverManager.getConnection(dbURL,dbUser,dbPass);
            
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        
        PreparedStatement statement = conn.prepareStatement
        ("insert into docs(username,description,docname,docfile,datechanged) values (?,?,?,?,?)");
        
        statement.setString(1, row.getUser());
        statement.setString(2, row.getDescription());
        statement.setString(3, filename);
        statement.setBlob(4, blobstream);
        statement.setDate(5, date);
        
        int exec = statement.executeUpdate();
        
        conn.close();
        
        return exec > 0;
    }
    
    public List<DBRow> Read() throws SQLException, ClassNotFoundException{
        
        String dbURL = "jdbc:derby://localhost:1527/ProjDB";
        String dbUser = "root";
        String dbPass = "wcy";

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        Connection conn = DriverManager.getConnection(dbURL,dbUser,dbPass);
        
        List<DBRow> rows = new ArrayList<DBRow>();
        
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from docs");
        
        while(rs.next()){
            DBRow row = new DBRow
            (rs.getInt("id"),rs.getString("username"),rs.getString("description"),rs.getDate("datechanged"),rs.getString("docname"));
            rows.add(row);
        }
        
        conn.close();
        
        return rows;
        
    };
    
    public DBRow Read(int id) throws SQLException, ClassNotFoundException{
        
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
            DBRow row = new DBRow
            (rs.getInt("id"),rs.getString("username"),rs.getString("description"),rs.getDate("datechanged"),rs.getString("docname"));
            return row;
        }
        
        conn.close();
        
        return null;
              
    };
    
    public boolean Update(DBRow row, InputStream blobstream, String filename) throws SQLException, IOException, ClassNotFoundException{
        
        String dbURL = "jdbc:derby://localhost:1527/ProjDB";
        String dbUser = "root";
        String dbPass = "wcy";

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        Connection conn = DriverManager.getConnection(dbURL,dbUser,dbPass);
        
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        
        PreparedStatement statement = conn.prepareStatement
        ("update docs set username=?, description=?, docname=?, docfile=?, datechanged=? where id=?");
        
        statement.setString(1, row.getUser());
        statement.setString(2, row.getDescription());
        statement.setString(3, filename);
        statement.setBlob(4, blobstream);
        statement.setDate(5, date);
        statement.setInt(6, row.getId());
        
        statement.executeUpdate();
        
        PreparedStatement statement2 = conn.prepareStatement
        ("insert into records(id,username,daterecorded) values (?,?,?)");
        
        statement2.setInt(1, row.getId());
        statement2.setString(2, row.getUser());
        statement2.setDate(3, date);
        
        int exec = statement2.executeUpdate();
        
        conn.close();
        return exec > 0;
    }
    
    public boolean Delete(int id) throws SQLException, ClassNotFoundException{
        
        String dbURL = "jdbc:derby://localhost:1527/ProjDB";
        String dbUser = "root";
        String dbPass = "wcy";

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        Connection conn = DriverManager.getConnection(dbURL,dbUser,dbPass);
        
        PreparedStatement statement = conn.prepareStatement
        ("delete from records where id=?");
        
        statement.setInt(1, id);
        
        statement.executeUpdate();
        
        PreparedStatement statement2 = conn.prepareStatement
        ("delete from docs where id=?");
        
        statement2.setInt(1, id);
        
        int exec = statement2.executeUpdate();
        
        conn.close();
        
        return exec > 0;
       
    };
}
