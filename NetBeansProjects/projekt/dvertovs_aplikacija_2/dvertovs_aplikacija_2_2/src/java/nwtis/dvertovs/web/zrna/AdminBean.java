/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.zrna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.servlet.ServletContext;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;
import nwtis.dvertovs.ejb.sb.DvertovsUsersFacade;
import nwtis.dvertovs.web.CheckMailboxTask;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;

/**
 *
 * @author dare
 */
@Named(value = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {

    @EJB
    private DvertovsUsersFacade dvertovsUsersFacade;

    private String command;
    private String response;

    private List<String> usersWaiting;
    private List<String> users;

    private String selectedFolder;
    private List<String> foldersList;

    private List<Poruka> listapor;

    /**
     * Creates a new instance of AdminBean
     */
    public AdminBean() {
    }

    public String accept() {
        if (CheckMailboxTask.dvertovsUsersFacade == null) {
            CheckMailboxTask.dvertovsUsersFacade = dvertovsUsersFacade;
        }
        DvertovsUsers dv = dvertovsUsersFacade.getByUsername(command);
        String role = (dv.getIsadmin() ? "ADMIN" : "USER");
        response = posaljiZahtjevNaServer("ADD " + command + "; PASSWD " + dv.getPassword() + "; ROLE " + role);
        return "OK";
    }

    public String decline() {
        DvertovsUsers dv = dvertovsUsersFacade.getByUsername(command);
        dv.setIsaccepted(1);
        dvertovsUsersFacade.edit(dv);
        response = "OK";
        return "OK";
    }

    public String catUp() {
        response = posaljiZahtjevNaServer("UP " + command);
        return "OK";
    }

    public String catDown() {
        response = posaljiZahtjevNaServer("DOWN " + command);
        return "OK";
    }

    public void selectFolder() {

    }

    public void deleteFolder() throws MessagingException {
        Konfiguracija konfig = getKonfig();
        String address = konfig.dajPostavku("mailserver");
        String user = konfig.dajPostavku("mailuser");
        String pass = konfig.dajPostavku("mailpass");
        CheckMailboxTask cmt = new CheckMailboxTask();
        cmt.setAddress(address);
        cmt.setIMAPPort(konfig.dajPostavku("IMAPport"));
        Message[] messages = null;
        Store store = null;
        Folder folder = null;
        try {
            store = cmt.getStore();
            store.connect(address, user, pass);

            folder = store.getFolder(this.selectedFolder);
            folder.open(Folder.READ_ONLY);

            messages = cmt.getMessages(store, folder, new FlagTerm(new Flags(Flags.Flag.SEEN), true));
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.listapor = new ArrayList<>();
        for (Message mess : messages) {
            mess.setFlag(Flags.Flag.DELETED, true);
        }
        folder.close(true);
        store.close();
    }

    public void sendCommand() {
        response = posaljiZahtjevNaServer(command);
    }

    private String posaljiZahtjevNaServer(String command) {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        DvertovsUsers dv = (DvertovsUsers) context.getExternalContext().getSessionMap().get("user");
        Konfiguracija konfig = (Konfiguracija) servletContext.getAttribute("App_Konfig");

        StringBuilder response = null;
        try {
            Socket socket = new Socket(konfig.dajPostavku("primitivniPosluziteljAdresa"), Integer.parseInt(konfig.dajPostavku("portPrimitivnogPosluzitelja")));
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            String request = "USER " + dv.getUsername() + "; PASSWD " + dv.getPassword() + "; " + command + ";";
            os.write(request.getBytes());
            os.flush();
            socket.shutdownOutput();

            response = new StringBuilder();
            int bajt;
            while ((bajt = is.read()) != -1) {
                response.append((char) bajt);
            }
            is.close();
            os.close();
            System.out.println("RESPONSE: " + response);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toString();
    }

    public List<String> getUsersWaiting() {
        this.usersWaiting = new ArrayList<>();
        for (DvertovsUsers dv : dvertovsUsersFacade.findAll()) {
            if (dv.getIsaccepted() == 0) {
                usersWaiting.add(dv.getUsername());
            }
        }
        return usersWaiting;
    }

    public List<String> getUsers() {
        this.users = new ArrayList<>();
        for (DvertovsUsers dv : dvertovsUsersFacade.findAll()) {
            if (!dv.getIsadmin() && dv.getIsaccepted() == 2) {
                users.add(dv.getUsername());
            }
        }
        return users;
    }

    public List<String> getFoldersList() {

        Konfiguracija konfig = getKonfig();
        this.foldersList = new ArrayList<>();
        this.foldersList.add(konfig.dajPostavku("folderuspjesne"));
        this.foldersList.add(konfig.dajPostavku("folderneuspjesne"));
        this.foldersList.add(konfig.dajPostavku("folderneispravne"));
        return foldersList;
    }

    public List<Poruka> getListapor() throws MessagingException, IOException {
        if (this.selectedFolder != null) {
            Konfiguracija konfig = getKonfig();
            String address = konfig.dajPostavku("mailserver");
            String user = konfig.dajPostavku("mailuser");
            String pass = konfig.dajPostavku("mailpass");
            CheckMailboxTask cmt = new CheckMailboxTask();
            cmt.setAddress(address);
            cmt.setIMAPPort(konfig.dajPostavku("IMAPport"));

            Message[] messages = null;
            try {
                Store store = cmt.getStore();
                store.connect(address, user, pass);

                Folder folder = store.getFolder(this.selectedFolder);
                folder.open(Folder.READ_ONLY);

                messages = cmt.getMessages(store, folder, new FlagTerm(new Flags(Flags.Flag.SEEN), true));
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.listapor = new ArrayList<>();
            for (Message mess : messages) {
                MimeMessage m = (MimeMessage) mess;
                Poruka p = new Poruka();
                p.setFrom(m.getFrom()[0].toString());
                p.setSubject(m.getSubject());
                p.setText(getMsgText(m));
                this.listapor.add(p);
            }
        }
        return listapor;
    }

    private String getMsgText(Message message) throws MessagingException, IOException {
        String s = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
        String temp;
        System.out.println("Poruka: ");
        while ((temp = reader.readLine()) != null) {
            System.out.println(s);
            s += temp;
        }
        return s;
    }

    private Konfiguracija getKonfig() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        return (Konfiguracija) servletContext.getAttribute("App_Konfig");
    }

    public void setListapor(List<Poruka> listapor) {
        this.listapor = listapor;
    }

    public void setFoldersList(List<String> foldersList) {
        this.foldersList = foldersList;
    }

    public String getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(String selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public void setUsersWaiting(List<String> usersWaiting) {
        this.usersWaiting = usersWaiting;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
