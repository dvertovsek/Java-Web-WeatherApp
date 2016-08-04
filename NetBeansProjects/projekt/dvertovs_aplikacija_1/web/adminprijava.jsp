<%-- 
    Document   : admin
    Created on : 23/mai/2016, 20:14:56
    Author     : dare
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin prijava</title>
    </head>
    <body>

        <form action="${pageContext.servletContext.contextPath}/ProvjeraKorisnika" method="POST">
            <input type="text" name="ki" placeholder="korisnik">
            <input type="password" name="pw" placeholder="lozinka">
            <input type="submit" name="login" value="Login">
        </form>

    </body>
</html>
