<%-- 
    Document   : ispisKorisnika
    Created on : 13/abr/2016, 14:25:51
    Author     : dare
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Popis korisnika</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/displaytag.css">
    </head>
    <body>
        <h1>Korisnici</h1>
        <sql:setDataSource var="korisnici" 
                           driver="${applicationScope.BP_Konfig.driverDatabase}" 
                           url="${applicationScope.BP_Konfig.serverDatabase}${applicationScope.BP_Konfig.userDatabase}" 
                           user="${applicationScope.BP_Konfig.userUsername}"
                           password="${applicationScope.BP_Konfig.userPassword}"/>
        <sql:transaction dataSource="${korisnici}">
            <sql:query var="rezultat">
                select * from polaznici
            </sql:query>
                <display:table name="${rezultat.rows}" pagesize="5">
                    <display:column property="kor_ime" title="Kor. ime"/>
                    <display:column property="ime" title="Ime"/>
                    <display:column property="prezime" title="Prezime"/>
                </display:table>
        </sql:transaction>
    </body>
</html>
