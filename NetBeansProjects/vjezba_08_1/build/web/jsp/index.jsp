<%-- 
    Document   : index
    Created on : 12/abr/2016, 19:58:12
    Author     : dare
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        
    </head>
    <body>
        <div class="list-group">
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/Kontroler"><i class="fa fa-tachometer fa-fw" aria-hidden="true"></i>&nbsp; Kontroler</a>
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/PrijavaKorisnika"><i class="fa fa-sign-in fa-fw" aria-hidden="true"></i>&nbsp; Prijava korisnika</a>
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/OdjavaKorisnika"><i class="fa fa-sign-out fa-fw" aria-hidden="true"></i>&nbsp; Odjava korisnika</a>
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/ProvjeraKorisnika"><i class="fa fa-check fa-fw" aria-hidden="true"></i>&nbsp; Provjera korisnika</a>
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/IspisPodataka"><i class="fa fa-list fa-fw" aria-hidden="true"></i>&nbsp; Ispis podataka</a>
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/IspisAktivnihKorisnika"><i class="fa fa-users fa-fw" aria-hidden="true"></i>&nbsp; Ispis aktivnih korisnika</a>
            <a class="list-group-item" href="${pageContext.servletContext.contextPath}/IspisKorisnika"><i class="fa fa-users fa-fw" aria-hidden="true"></i>&nbsp; Ispis korisnika</a>
        </div>
        <!--
        <div>
            <a href="${pageContext.servletContext.contextPath}/Kontroler">/Kontroler</a><br>
            <a href="${pageContext.servletContext.contextPath}/PrijavaKorisnika">/PrijavaKorisnika</a><br>
            <a href="${pageContext.servletContext.contextPath}/OdjavaKorisnika">/OdjavaKorisnika</a><br>
            <a href="${pageContext.servletContext.contextPath}/ProvjeraKorisnika">/ProvjeraKorisnika</a><br>
            <a href="${pageContext.servletContext.contextPath}/IspisPodataka">/IspisPodataka</a><br>
            <a href="${pageContext.servletContext.contextPath}/IspisAktivnihKorisnika">/IspisAktivnihKorisnika</a><br>
            <a href="${pageContext.servletContext.contextPath}/IspisKorisnika">/IspisKorisnika</a><br>
        </div>-->
    </body>
</html>