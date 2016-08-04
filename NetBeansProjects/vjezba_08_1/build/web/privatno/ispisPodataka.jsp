<%-- 
    Document   : ispisPodataka
    Created on : 12/abr/2016, 20:00:51
    Author     : dare
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ispis podataka korisnika</title>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    </head>
    <body>
        <h1>Podaci o korisniku</h1>
        <div class="list-group" style="width:450px;">
            <a class="list-group-item" href="#"><i class="fa fa-user fa-fw" aria-hidden="true"></i>&nbsp; Korisničko ime <strong class="pull-right">${sessionScope.korisnik.korisnik}</strong></a>
            <a class="list-group-item" href="#"><i class="fa fa-pencil-square-o fa-fw" aria-hidden="true"></i>&nbsp; Ime <strong class="pull-right">${sessionScope.korisnik.ime}</strong></a>
            <a class="list-group-item" href="#"><i class="fa fa-pencil-square-o fa-fw" aria-hidden="true"></i>&nbsp; Prezime <strong class="pull-right">${sessionScope.korisnik.prezime}</strong></a>
            <a class="list-group-item" href="#"><i class="fa fa-map-marker fa-fw" aria-hidden="true"></i>&nbsp; IP Adresa <strong class="pull-right">${sessionScope.korisnik.ip_adresa}</strong></a>
            <a class="list-group-item" href="#"><i class="fa fa-hashtag fa-fw" aria-hidden="true"></i>&nbsp; Vrsta <span class="badge">${sessionScope.korisnik.vrsta}</span></a>
            <a class="list-group-item" href="#"><i class="fa fa-hashtag fa-fw" aria-hidden="true"></i>&nbsp; ID sesije <span class="pull-right label label-success">${sessionScope.korisnik.ses_ID}</span></a>
        </div>
            <!--
        <h1>Ispis podataka korisnika</h1>
        Korisnicko ime: ${sessionScope.korisnik.korisnik}<br>
        Prezime: ${sessionScope.korisnik.prezime}<br>
        Ime: ${sessionScope.korisnik.ime}<br>
        IP adresa: ${sessionScope.korisnik.ip_adresa}<br>
        Vrsta: ${sessionScope.korisnik.vrsta}<br>
        ID sesije: ${sessionScope.korisnik.ses_ID}<br>    -->   
    </body>
</html>
