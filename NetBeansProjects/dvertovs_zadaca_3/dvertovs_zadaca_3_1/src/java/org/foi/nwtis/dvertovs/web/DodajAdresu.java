/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.dvertovs.db.DBController;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.rest.klijenti.GMKlijent;
import org.foi.nwtis.dvertovs.rest.klijenti.OWMKlijent;
import org.foi.nwtis.dvertovs.web.podaci.Adresa;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPodaci;

/**
 *
 * @author dare
 * Servlet koji prihvaca zahtjev iz index.jsp, te ga obradjuje
 * Ukoliko je zahtjev za prikupljanjem geo podataka (dohvatGP), dohvacaju se geo podaci te se vraca kreirani objekt adrese (unutra se nalaze lat i long podaci)
 * Ukoliko je zahtjev za spremanjem geo podataka, dohvaceni geo podaci za adresu se spremaju u bazu
 * Ukoliko je zahtjev za dohvacanjem meteo podataka, dohvacaju se meteo podaci te se vraca objekt tipa MeteoPodaci
 */
@WebServlet(name = "DodajAdresu", urlPatterns = {"/DodajAdresu"})
public class DodajAdresu extends HttpServlet {

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

        String responseString = "ERROR";
        String actionPerformed = "";

        String addr = request.getParameter("adresa");
        GMKlijent gmk = new GMKlijent();
        Adresa adresa = new Adresa(0, addr, gmk.getGeoLocation(addr));

        if (request.getParameter("dohvatGP") != null) {
            responseString = "Info o adresi dohvacen";
            request.setAttribute("adresaInfo", adresa);
            actionPerformed = "dohvatGP";
        }
        if (request.getParameter("spremiGP") != null) {
            try {
                DBController.getDbCon().insertAddress(adresa);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(DodajAdresu.class.getName()).log(Level.SEVERE, null, ex);
            }
            responseString = "Adresa spremljena u bazu";
            actionPerformed = "spremiGP";
        }
        if (request.getParameter("dohvatMP") != null) {
            ServletContext servletContext = request.getSession().getServletContext();
            String APPID = (String) ((Konfiguracija) servletContext.getAttribute("App_Konfig")).dajPostavku("OWMAppID");
            OWMKlijent owmk = new OWMKlijent(APPID);
            MeteoPodaci mp = owmk.getRealTimeWeather(adresa.getGeoloc().getLatitude(), adresa.getGeoloc().getLongitude());
            responseString = "Dohvaceni meteo podaci";
            request.setAttribute("meteoInfo", mp);
            actionPerformed = "dohvatMP";
        }

        request.setAttribute("MSG", responseString);
        request.setAttribute("actionPerformed", actionPerformed);
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
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
