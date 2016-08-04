<%-- 
    Document   : ispisAktivnihKorisnika
    Created on : 13/abr/2016, 13:59:12
    Author     : dare
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Popis aktivnih korisnika</title>
    </head>
    <body>
        <h1>Aktivni korisnici</h1>
        <c:forEach items="${applicationScope.KORISNICI}" var="k">
            ${k.prezime} ${k.ime} <br>
        </c:forEach>
    </body>
</html>
