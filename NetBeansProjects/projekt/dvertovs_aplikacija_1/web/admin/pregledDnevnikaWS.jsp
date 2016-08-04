<%-- 
    Document   : pregledDnevnikaWS
    Created on : 24/mai/2016, 10:06:57
    Author     : dare
--%>

<%@page import="org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/displaytag.css">
        <title>Dnevnik WS</title>
    </head>
    <body>
        <h1>Korisnici</h1>
        <form action="${pageContext.servletContext.contextPath}/PregledDnevnikaWS">
            Korisnik: <input type="text" name="kor"><br>
            Vrsta web servisa <input type="text" name="vrsta"><br>
            Naziv web servisa <input type="text" name="naziv"><br>
            Datum od <input type="datetime-local" step="1" name="datumod"><br>
            Datum do <input type="datetime-local" step="1" name="datumdo"><br>
            <input type="submit" value="Filtriraj!">
        </form>
        <sql:setDataSource var="dnevnik" 
                           driver="${applicationScope.BP_Konfig.driverDatabase}" 
                           url="${applicationScope.BP_Konfig.serverDatabase}${applicationScope.BP_Konfig.userDatabase}" 
                           user="${applicationScope.BP_Konfig.userUsername}"
                           password="${applicationScope.BP_Konfig.userPassword}"/>
        <sql:transaction dataSource="${dnevnik}">
            <sql:query var="rezultat">
                <%
                    boolean filter = false;
                    String filterString = "and";
                    if (request.getParameter("vrsta") != null) {
                        filter = true;
                        filterString += " vrsta LIKE '%" + request.getParameter("vrsta") + "%' AND username LIKE '%" + request.getParameter("kor") + "%' AND naziv LIKE '%" + request.getParameter("naziv") + "%'";
                    }
                    String datumod = "0000-00-00 00:00:00.0";
                    String datumdo = "9999-12-30 23:59:00.0";
                    System.out.println(request.getParameter("datumod"));
                    if (request.getParameter("datumod") != null) {
                        if (!request.getParameter("datumod").equals("")) {
                            datumod = request.getParameter("datumod");
                        }
                        if (!request.getParameter("datumdo").equals("")) {
                            datumdo = request.getParameter("datumdo");
                        }
                    }
                    filterString += " AND vrijeme BETWEEN '" + datumod + "' AND '" + datumdo + "'";
                    if (!filter) {
                        filterString = "";
                    }

                %>
                select username, vrsta, naziv, vrijeme from dvertovs_dnevnik_WS join dvertovs_users USING(iduser) WHERE (vrsta = 'SOAP' OR vrsta = 'REST') <%= filterString%>    
            </sql:query>
            <%
                ServletContext sc = request.getServletContext();
                Konfiguracija konfig = (Konfiguracija) sc.getAttribute("App_Konfig");
            %>
            <display:table name="${rezultat.rows}" pagesize='<%= Integer.parseInt(konfig.dajPostavku("stranicenje"))%>'>
                <display:column property="username" title="Kor. ime"/>
                <display:column property="vrsta" title="Vrsta servisa"/>
                <display:column property="naziv" title="Naziv servisa"/>
                <display:column property="vrijeme" title="Vrijeme"/>
            </display:table>
        </sql:transaction>
    </body>
</html>
