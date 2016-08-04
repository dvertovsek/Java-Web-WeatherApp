/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.foi.nwtis.dvertovs.konfiguracije.bp.BP_Konfiguracija;

/**
 *
 * @author dare
 */
@Named(value = "pregledPolaznika")
@RequestScoped
public class PregledPolaznika {

    /**
     * Creates a new instance of PregledPolaznika
     */
    private ArrayList<Polaznici> polaznici;

    public PregledPolaznika() {
    }

    public ArrayList<Polaznici> getPolaznici() throws SQLException, ClassNotFoundException {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        BP_Konfiguracija konfig = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");

        String url = konfig.getServerDatabase() + konfig.getUserDatabase();
        String query = "SELECT kor_ime, ime, prezime, lozinka, email_adresa, vrsta, datum_kreiranja, datum_promjene FROM polaznici";

        Class.forName(konfig.getDriverDatabase());
        
        Connection con = DriverManager.getConnection(url,konfig.getUserUsername(),konfig.getUserPassword());
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        this.polaznici = new ArrayList<>();
        while (rs.next()) {
            Polaznici polaznik = new Polaznici();
            polaznik.setKorisnik(rs.getString(1));
            polaznik.setIme(rs.getString(2));
            polaznik.setPrezime(rs.getString(3));
            polaznik.setLozinka(rs.getString(4));
            polaznik.setEmail(rs.getString(5));
            polaznik.setVrsta(Integer.parseInt(rs.getString(6)));
            polaznik.setDatumKreiranja(rs.getString(7));
            polaznik.setDatumPromjene(rs.getString(8));
            polaznici.add(polaznik);
        }
        return polaznici;
    }

}
