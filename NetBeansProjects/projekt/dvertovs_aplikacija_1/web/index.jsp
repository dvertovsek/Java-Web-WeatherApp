<%-- 
    Document   : index
    Created on : 23/mai/2016, 19:09:30
    Author     : dare
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pocetna</title>
    </head>
    <body>

        <a href="${pageContext.servletContext.contextPath}/Kontroler">Kontroler</a><br>
        <a href="${pageContext.servletContext.contextPath}/IspisKorisnika">Ispis korisnika</a><br>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnikaWS">Pregled dnevnika Web servisa</a><br>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnikaSS">Pregled dnevnika primitivnog servera</a><br><br>

        <% if (request.getSession().getAttribute("usertip") != null) { %>
        <a href="${pageContext.servletContext.contextPath}/OdjavaKorisnika">Odjava korisnika</a><br>
        <% } else {%>
        <a href="${pageContext.servletContext.contextPath}/AdminLogin">Admin login</a><br>
        <% }%>
    </body>
</html>
