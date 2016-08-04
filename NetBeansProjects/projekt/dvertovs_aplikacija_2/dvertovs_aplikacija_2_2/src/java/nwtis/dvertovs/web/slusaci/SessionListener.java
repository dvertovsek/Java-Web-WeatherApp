/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.slusaci;

import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;

/**
 * Web application lifecycle listener.
 *
 * @author dare
 */
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().compareTo("user") == 0) {
            System.out.println("Prijavljen korisnik: " + event.getValue());

            ServletContext context = event.getSession().getServletContext();
            Object o = context.getAttribute("KORISNICI");
            ArrayList<DvertovsUsers> korisnici = null;

            if (o == null) {
                korisnici = new ArrayList();
            } else if (o instanceof ArrayList) {
                korisnici = (ArrayList<DvertovsUsers>) o;
            } else {
                System.out.println("PogreÅ¡ka klasa za evidenciju korisnika");
            }
            if (event.getValue() instanceof DvertovsUsers) {
                DvertovsUsers k = (DvertovsUsers) event.getValue();
                korisnici.add(k);
                context.setAttribute("KORISNICI", korisnici);
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().compareTo("user") == 0) {
            ServletContext context = event.getSession().getServletContext();
            Object o = context.getAttribute("KORISNICI");
            ArrayList<DvertovsUsers> korisnici = null;

            if (o == null) {
                korisnici = new ArrayList();
            } else if (o instanceof ArrayList) {
                korisnici = (ArrayList<DvertovsUsers>) o;
            } else {
                System.out.println("PogreÅ¡ka klasa za evidenciju korisnika");
            }
            int idSedije = ((DvertovsUsers) event.getValue()).getIduser();
            for (DvertovsUsers k : korisnici) {
                if (k.getIduser() == idSedije) {
                    korisnici.remove(k);
                    context.setAttribute("KORISNICI", korisnici);
                    System.out.println("Odjavljen korisnik: " + k.getUsername());

                    break;
                }
            }
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {

    }
}
