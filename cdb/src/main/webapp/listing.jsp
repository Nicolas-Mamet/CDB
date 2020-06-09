<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<link href="../css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="../css/font-awesome.css" rel="stylesheet" media="screen">
<link href="../css/main.css" rel="stylesheet" media="screen">
<html>
<head>
    <meta charset="UTF-8">
    <title>Listing</title>
</head>
    <body>
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>name</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${list}" var="u">
                    <tr>
                        <td>${u.getID()}</td>
                        <td>${u.getName()}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>