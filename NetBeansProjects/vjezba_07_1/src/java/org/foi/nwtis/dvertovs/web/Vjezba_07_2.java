/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.dvertovs.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author dare
 */
public class Vjezba_07_2 extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Vjezba_07_2</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Vjezba_07_2 at " + request.getContextPath() + "</h1>");
             
            String folder = this.getServletContext().getRealPath("/WEB-INF");
            String configFile = folder + File.separator + this.getInitParameter("konfiguracija");
            out.println("ime datoteke: "+configFile+"<br>");
            
            List<String> users = getUserData(configFile);
           
            try {
                out.println("<table>");
                out.println("<tr><th>Username</th><th>Firstname</th><th>Lastname</th></tr>");
                for (String user : users) {
                    String[] userArr = user.split(" ");
                    out.println("<tr>");
                    out.println("<td>");
                    out.println(userArr[0]);
                    out.println("</td>");
                    out.println("<td>");
                    out.println(userArr[2]);
                    out.println("</td>");
                    out.println("<td>");
                    out.println(userArr[1]);
                    out.println("</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } catch (NullPointerException e) {
                out.println("null pointer exception");
            }
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    public List<String> getUserData(String configFile) {
        ArrayList<String> userList = new ArrayList<String>();
        BP_Konfiguracija konfig = null;
        try {
            konfig = new BP_Konfiguracija(configFile);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(Vjezba_07_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (konfig == null) {
            return null;
        }
        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String query = "select kor_ime, prezime, ime from POLAZNICI";

         try {
            Class.forName(konfig.getDriverDatabase());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Vjezba_07_2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Connection con = DriverManager.getConnection(url,
                    konfig.getUserUsername(), konfig.getUserPassword());
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String mb = rs.getString(1);
                String pr = rs.getString(2);
                String im = rs.getString(3);
                String row = mb + " " + pr + " " + im;
                userList.add(row);
            }

            con.close();
            stmt.close();
            rs.close();
        } catch (Exception ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return userList;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
