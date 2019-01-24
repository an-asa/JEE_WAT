<%-- 
    Document   : viewDocs
    Created on : Jan 23, 2019, 10:45:17 PM
    Author     : PRO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>View Docs</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    </head>
    <body>
        <center style="margin:100px 100px 100px 100px">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID:</th>
                    <th>Submitted by:</th>
                    <th>Description:</th>
                    <th>Date submitted:</th>
                    <th>Filename:</th>
                    <th colspan=3></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${rows}" var="row">
                    <tr>
                        <td><c:out value="${row.id}" /></td>
                        <td><c:out value="${row.user}" /></td>
                        <td><c:out value="${row.description}" /></td>
                        <td><fmt:formatDate pattern="yyyy-MMM-dd" value="${row.date}" /></td>
                        <td><c:out value="${row.filename}" /></td>
                        <td><a href="CRUDServlet?action=edit&id=<c:out value="${row.id}"/>">Update</a></td>
                        <td><a href="CRUDServlet?action=delete&id=<c:out value="${row.id}"/>">Delete</a></td>
                        <td><a href="CRUDServlet?action=download&id=<c:out value="${row.id}"/>">Download</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <br />
        <p><button class="btn btn-primary" onclick="window.location.href='CRUDServlet?action=insert';">Add File</button></p>
        </center>
    </body>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
</html>
