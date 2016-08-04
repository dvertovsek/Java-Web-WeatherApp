<%-- 
    Document   : admin
    Created on : 23/mai/2016, 20:18:37
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
        <title>Admin</title>
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
                select * from dvertovs_users
            </sql:query>
            <%
                ServletContext sc = request.getServletContext();
                Konfiguracija konfig = (Konfiguracija) sc.getAttribute("App_Konfig");
            %>
            <display:table name="${rezultat.rows}" pagesize='<%= Integer.parseInt(konfig.dajPostavku("stranicenje")) %>'>
                <display:column property="iduser" title="ID"/>
                <display:column property="username" title="Kor. ime"/>
                <display:column property="password" title="Pass"/>
                <display:column property="idusertype" title="User type"/>
                <display:column property="category" title="Category"/>
                <display:column property="requests" title="Requests"/>
            </display:table>
        </sql:transaction>
    </body>
</html>
