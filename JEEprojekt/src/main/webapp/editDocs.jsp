<%-- 
    Document   : row
    Created on : Jan 23, 2019, 10:54:32 PM
    Author     : PRO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.18.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
        <script>  
        function validateform(){
        var user=document.rowform.user.value;  
        var description=document.rowform.description.value;
        var file=document.rowform.file.value;  

            if (user==null || user=="" || description==null || description=="" || file==null){  
              alert("Fields cannot be empty");  
              return false;
            }
        }
        </script>
        <title>Edit Docs</title>
    </head>
    <body>
    <center style="margin:100px 100px 100px 100px">
        <form name="rowform" method="POST" action='CRUDServlet' enctype="multipart/form-data" onsubmit="return validateform()">
            ID : <br />  <input class="form-control" type="text" readonly="readonly" name="id"
                        value="<c:out value="${row.id}"/>"/> <br /> 
            User : <br /> <input class="form-control" type="text" name="user"
                        value="<c:out value="${row.user}"/>"/> <br /> 
            Description : <br /> <input class="form-control" type="text" name="description"
                        value="<c:out value="${row.description}"/>"/><br />
            File : <br /> <input type="file" name="file"/> <br />
            <br />
            <input class="btn btn-primary" type="submit" />
            
        </form>
    </center>
    </body>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
</html>
