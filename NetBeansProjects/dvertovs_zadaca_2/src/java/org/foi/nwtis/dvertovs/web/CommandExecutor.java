/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.dvertovs.web.kontrole.DBController;

/**
 *
 * @author dare
 */
class CommandExecutor {

    private final String REGEX = "(GRAD|TVRTKA) ([A-Za-z0-9]+); (ADD|UPDATE);";
    private final String[] DOMAINS = {"hr", "info", "com", "eu"}; //TODO: provjeriti na itnernetu ovaj warning? kako da se popravi
    private final ArrayList<String> ADDR = new ArrayList<>();

    private String pathToSave;

    private String commandPrefix;
    private String command;
    private String commandParam;
    private String rootFolderPath;

    private int addGRADCount = 0;
    private int updateGRADCount = 0;
    private int addTVRTKACount = 0;
    private int updateTVRTKACount = 0;

    /**
     * 
     * @param msgText tekst poruke koji treba sadrzavati naredbu
     * @param pathToSave korjenski WEB-INF direktorij aplikacije
     * @return metoda vraca true ili false, ukoliko se radi o pravilnoj naredbi koja postuje sintaksu vraca se true, u drugom slucaju false
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws IOException 
     */
    public boolean executeCommand(String msgText, String pathToSave) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {
        this.pathToSave = pathToSave;
        if (this.messageTextIsCommand(msgText)) {
            if (DBController.getDbCon().executeCommand(command, commandParam)) {
                updateCommandCount();
                generateRootFolderPath();
                generateAddr();
                checkWebsites();
                return true;
            }
        }
        return false;
    }

    /**
     * metoda koja azurira brojac izvrsenih naredaba odredjenog tipa
     */
    private void updateCommandCount() {
        switch (this.commandPrefix) {
            case "TVRTKA":
                if (this.command.equals("ADD")) {
                    this.addTVRTKACount++;
                } else {
                    this.updateTVRTKACount++;
                }
                break;
            case "GRAD":
                if (this.command.equals("ADD")) {
                    this.addGRADCount++;
                } else {
                    this.updateGRADCount++;
                }
                break;
        }
    }

    /**
     * metoda koja generira naziv direktorija u koji ce se spremati sve web stranice (od jedne naredbe) koje budu pronadjene
     */
    private void generateRootFolderPath() {
        Date today = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = DATE_FORMAT.format(today);
        this.rootFolderPath = this.pathToSave + File.separator + this.commandParam + "_" + date;
    }

    /**
     * metoda koja generira sve adrese koje trebaju biti testirane za jednu naredbu
     */
    private void generateAddr() {
        for (String domain : DOMAINS) {
            ADDR.add("http://www." + this.commandParam + "." + domain);
            ADDR.add("http://" + this.commandParam + "." + domain);
        }
    }

    /**
     * metoda koja provjerava sve moguce web adrese za jednu naredbu, baca Exception ukoliko web stranica ne postoji
     * @throws IOException 
     */
    private void checkWebsites() throws IOException {

        for (String addr : ADDR) {
            try (BufferedReader br = getBuffReader(addr)) {
                saveToFile(br, addr);
            } catch (IOException ioe) {
                System.out.println("NE POSTOJI");
            }
        }
    }

    /**
     * 
     * @param address URL web stranice koju se zeli preuzeti
     * @return metoda vraca objekt tipa BufferedReader 
     * @throws IOException metoda baca IOException ukoliko web stranica ne postoji
     */
    private BufferedReader getBuffReader(String address) throws IOException {
        URL url = new URL(address);
        URLConnection conn = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return br;
    }

    /**
     * 
     * @param folderPath putanja direktorija koja se zeli kreirati 
     */
    private void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 
     * @param br buffered reader s kojeg se zeli web stranica spremiti u datoteku
     * @param address adresa web stranice koja se sprema u datoteku
     * @throws IOException 
     */
    private void saveToFile(BufferedReader br, String address) throws IOException {

        String folderPath = this.rootFolderPath + File.separator + address.replace("http://", "");
        createFolder(folderPath);

        String fileName = folderPath + File.separator + address.replace("http://", "") + ".html";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            bw.write(inputLine);
        }

        bw.close();

        System.out.println("File saved, location: " + fileName);
    }

    /**
     * 
     * @param msgText tekst poruke za koju se provjerava radi li se o naredbi ili ne
     * @return metoda vraca true ukoliko se radi o naredbi s ispravnom sintaksom, false ukoliko tekst nije naredba
     */
    public boolean messageTextIsCommand(String msgText) {
        Pattern pattern = Pattern.compile(this.REGEX);
        Matcher m = pattern.matcher(msgText.replace("\n", "").replace("\r", ""));
        boolean status = m.matches();
        if (status) {
            if (m.group(1) != null) {
                this.commandPrefix = m.group(1);
            }
            if (m.group(2) != null) {
                this.commandParam = m.group(2);
            }
            if (m.group(3) != null) {
                this.command = m.group(3);
            }
            return true;
        } else {
            return false;
        }
    }

    public int getAddGRADCount() {
        return addGRADCount;
    }

    public int getUpdateGRADCount() {
        return updateGRADCount;
    }

    public int getAddTVRTKACount() {
        return addTVRTKACount;
    }

    public int getUpdateTVRTKACount() {
        return updateTVRTKACount;
    }
}
