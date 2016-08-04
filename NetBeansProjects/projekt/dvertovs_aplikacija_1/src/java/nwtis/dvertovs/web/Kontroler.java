/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web;

import java.io.IOException;
import java.rmi.ServerException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nwtis.dvertovs.db.DBController;

/**
 *
 * @author dare
 */
public class Kontroler extends HttpServlet {

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
        String servletPutanja = request.getServletPath();
        String cilj = null;
        HttpSession session = request.getSession();
        switch (servletPutanja) {
            case "/Kontroler":
                cilj = "/index.jsp";
                break;
            case "/AdminLogin":
                cilj = "/adminprijava.jsp";
                break;
            case "/OdjavaKorisnika":
                session.invalidate();
                cilj = "/index.jsp";
                break;
            case "/ProvjeraKorisnika":
                String ki = request.getParameter("ki");
                String pw = request.getParameter("pw");
                if (adminAuthenticated(ki, pw)) {
                    session.setAttribute("user", ki);
                    session.setAttribute("usertip", "1");
                }

                cilj = "/index.jsp";
                break;
            case "/IspisKorisnika":
                if (userIsAdmin(request)) {
                    cilj = "/admin/ispisKorisnika.jsp";
                } else {
                    cilj = "/adminprijava.jsp";
                }
                break;
            case "/PregledDnevnikaWS":
                if (userIsAdmin(request)) {
                    cilj = "/admin/pregledDnevnikaWS.jsp";
                } else {
                    cilj = "/adminprijava.jsp";
                }
                break;
            case "/PregledDnevnikaSS":
                if (userIsAdmin(request)) {
                    cilj = "/admin/pregledDnevnikaSS.jsp";
                } else {
                    cilj = "/adminprijava.jsp";
                }
                break;
            default:
                cilj = null;
                break;
        }
        if (cilj == null) {
            throw new ServerException("Krivi URL.");
        }
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(cilj);
        dispatcher.forward(request, response);
    }

    private boolean adminAuthenticated(String user, String pass) {
        try {
            return DBController.getDbCon().getUserInfo(user, pass, true).next();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Kontroler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean userIsAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            if (session.getAttribute("usertip") != "1") {
                return false;
            }
        }
        return true;
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
