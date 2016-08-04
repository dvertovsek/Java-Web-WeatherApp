<%-- 
    Document   : index
    Created on : 27/abr/2016, 14:49:51
    Author     : dare
--%>

<%@page import="org.foi.nwtis.dvertovs.web.podaci.MeteoPodaci"%>
<%@page import="org.foi.nwtis.dvertovs.web.podaci.Adresa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
        <title>Zadaca 3 - dvertovs</title>
        <style>
            .larger-font{font-size:15px;}
            .btn{width:250px;}
        </style>
    </head>
    <form action="DodajAdresu" method="POST">
        <label for="adresa">Adresa</label>
        <input name="adresa" id="adresa" size="100" maxlength="254"><br>
        <button type="submit" name="dohvatGP" class="btn btn-info"><i class="pull-left fa fa-globe" aria-hidden="true"></i> Dohvati geo podatke</button><br>
        <button type="submit" name="spremiGP" class="btn btn-info"><i class="pull-left fa fa-database" aria-hidden="true"></i> Spremi geo podatke</button><br>
        <button type="submit" name="dohvatMP" class="btn btn-info"><i class="pull-left fa fa-cloud" aria-hidden="true"></i> Dohvati meteo podatke</button><br>
    </form><br>
    <%
        if (request.getAttribute("actionPerformed") != null) {
    %>
    <div class="alert alert-success" role="alert"><%= request.getAttribute("MSG")%></div>
    <%
        switch ((String) request.getAttribute("actionPerformed")) {
            case "dohvatGP":
                Adresa adresa = (Adresa) request.getAttribute("adresaInfo");
    %>
    <h1>Informacije o adresi</h1>
    <div class="list-group" style="width:450px;">
        <a class="list-group-item" href="#"><i class="fa fa-location-arrow fa-fw" aria-hidden="true"></i> Adresa <strong class="pull-right">${requestScope.adresaInfo.adresa}</strong></a>
        <a class="list-group-item" href="#"><i class="fa fa-map-marker fa-fw" aria-hidden="true"></i> Latitude <span class="pull-right label label-success larger-font">${requestScope.adresaInfo.geoloc.latitude}</span></a>
        <a class="list-group-item" href="#"><i class="fa fa-map-marker fa-fw" aria-hidden="true"></i> Longitude <span class="pull-right label label-success larger-font">${requestScope.adresaInfo.geoloc.longitude}</span></a>
    </div>
    <%
            break;
        case "dohvatMP":
            MeteoPodaci mp = (MeteoPodaci) request.getAttribute("meteoInfo");
    %>
    <h1>Informacije o metereoloskim prilikama</h1>
    <div class="list-group" style="width:450px;">
        <a class="list-group-item" href="#"><img src="http://openweathermap.org/img/w/${requestScope.meteoInfo.weatherIcon}.png" alt="${requestScope.meteoInfo.weatherValue}"><span class="pull-right label label-success larger-font">${requestScope.meteoInfo.weatherValue}</span></a>
        <a class="list-group-item" href="#"><i class="fa fa-info fa-fw" aria-hidden="true"></i> Temperatura <strong class="pull-right">${requestScope.meteoInfo.temperatureValue} ${requestScope.meteoInfo.temperatureUnit}</strong></a>
        <a class="list-group-item" href="#"><i class="fa fa-info fa-fw" aria-hidden="true"></i> Vlažnost <strong class="pull-right">${requestScope.meteoInfo.humidityValue} ${requestScope.meteoInfo.humidityUnit}</strong></a>
        <a class="list-group-item" href="#"><i class="fa fa-info fa-fw" aria-hidden="true"></i> Tlak <strong class="pull-right">${requestScope.meteoInfo.pressureValue} ${requestScope.meteoInfo.pressureUnit}</strong></a>
        <a class="list-group-item" href="#"><i class="fa fa-calendar fa-fw" aria-hidden="true"></i> Izlazak sunca <strong class="pull-right label label-info larger-font">${requestScope.meteoInfo.sunRise}</strong></a>
        <a class="list-group-item" href="#"><i class="fa fa-calendar fa-fw" aria-hidden="true"></i> Zalazak sunca <span class="pull-right label label-info larger-font">${requestScope.meteoInfo.sunSet}</span></a>     
        <a class="list-group-item" href="#"><i class="fa fa-clock-o fa-fw" aria-hidden="true"></i> Zadnje azuriranje <span class="pull-right label label-success larger-font">${requestScope.meteoInfo.lastUpdate}</span></a>
    </div>
    <%
                    break;
            }
        }
    %>
</body>
</html>