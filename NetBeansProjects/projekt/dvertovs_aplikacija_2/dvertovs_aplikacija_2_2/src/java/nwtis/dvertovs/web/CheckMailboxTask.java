/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJBException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.persistence.NoResultException;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;
import nwtis.dvertovs.ejb.sb.DvertovsUsersFacade;

/**
 *
 * @author dare
 */
public class CheckMailboxTask extends TimerTask {

    private Date processingStart;
    private Date processingEnd;

    public static volatile DvertovsUsersFacade dvertovsUsersFacade;

    private String folderUspjesne;
    private String folderNeuspjesne;
    private String folderNeispravne;

    private String IMAPPort;
    private String address;
    private String user;
    private String pass;
    private String subjectKeyword;
    private int brojUspjesnihPoruka;
    private int brojNeuspjesnihPoruka;

    @Override
    public void run() {
        this.processingStart = new Date();
        try {
            if (dvertovsUsersFacade != null) {
                Store store = getStore();
                store.connect(address, user, pass);
                
                Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
                
                Message messages[] = getMessages(store, folder, new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                
                sortMessages(store,messages,folder);
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(CheckMailboxTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.processingEnd = new Date();
        sendJMS();
    }

    public Store getStore() throws NoSuchProviderException{
        java.util.Properties properties = System.getProperties();
        properties.put("mail.imap.host", address);
        properties.put("mail.imap.port", IMAPPort);

        Session session = Session.getInstance(properties, null);

        return session.getStore("imap");
    }
    
    public Message[] getMessages(Store store, Folder folder, FlagTerm flagTerm) throws MessagingException  {
        
        return folder.search(flagTerm);
    }

    private void sortMessages(Store store, Message messages[], Folder folder) throws MessagingException, IOException {

        Folder defFolder = store.getDefaultFolder();

        Folder uspjesneFolder = defFolder.getFolder(this.folderUspjesne);
        if (!uspjesneFolder.exists()) {
            uspjesneFolder.create(Folder.HOLDS_MESSAGES);
        }

        Folder neuspjesneFolder = defFolder.getFolder(this.folderNeuspjesne);
        if (!neuspjesneFolder.exists()) {
            neuspjesneFolder.create(Folder.HOLDS_MESSAGES);
        }

        Folder neispravneFolder = defFolder.getFolder(this.folderNeispravne);
        if (!neispravneFolder.exists()) {
            neispravneFolder.create(Folder.HOLDS_MESSAGES);
        }
        
        System.out.println("messages.length---" + messages.length);

        for (int i = 0; i < messages.length; ++i) {
            MimeMessage message = (MimeMessage) messages[i];
            message.setFlag(Flags.Flag.SEEN, true);

            ContentType ct = new ContentType(message.getContentType());
            String s = "";

            String msgSubject = message.getSubject();
            int messageStatus = 0;
            if ("text/plain".equalsIgnoreCase(ct.getBaseType()) && this.subjectKeyword.equals(msgSubject)) {

                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + msgSubject);
                System.out.println("From: " + message.getFrom()[0]);

                BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
                String temp;
                System.out.println("Poruka: ");
                while ((temp = reader.readLine()) != null) {
                    System.out.println(s);
                    s += temp;
                }
                System.out.println("---------------------------------");

                //DA LI JE PORUKA NEISPRAVNA, NEUSPJESNA ILI USPJESNA?
                messageStatus = executeMessage(s);

            }
            Message[] msgsForTransfer = {message};
            switch (messageStatus) {
                case 0:
                    folder.copyMessages(msgsForTransfer, neispravneFolder);
                    break;
                case 1:
                    folder.copyMessages(msgsForTransfer, neuspjesneFolder);
                    this.brojNeuspjesnihPoruka++;
                    break;
                case 2:
                    folder.copyMessages(msgsForTransfer, uspjesneFolder);
                    this.brojUspjesnihPoruka++;
                    break;
            }
        }
    }

    private void sendJMS() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int executeMessage(String messageContent) {
        int status = 0;
        String userRegex = "ADD ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,}); ROLE (ADMIN|USER)";
        Matcher m = Pattern.compile(userRegex).matcher(messageContent);
        if (m.find()) {
            boolean changed;
            try {
                changed = changeUserStatus(m.group(1));
            } catch (EJBException | NoResultException ex) {
                return 1;
            }
            if (changed) {
                status = 2;
            } else {
                status = 1;
            }
        }
        return status;
    }

    public boolean changeUserStatus(String username) {
        DvertovsUsers du = dvertovsUsersFacade.getByUsername(username);
        if (du.getIsaccepted() == 1 || du.getIsaccepted() == 2) {
            return false;
        }
        du.setIsaccepted(2);
        dvertovsUsersFacade.edit(du);
        return true;
    }

    public void setIMAPPort(String IMAPPort) {
        this.IMAPPort = IMAPPort;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setSubjectKeyword(String subjectKeyword) {
        this.subjectKeyword = subjectKeyword;
    }

    public void setFolderUspjesne(String folderUspjesne) {
        this.folderUspjesne = folderUspjesne;
    }

    public void setFolderNeuspjesne(String folderNeuspjesne) {
        this.folderNeuspjesne = folderNeuspjesne;
    }

    public void setFolderNeispravne(String folderNeispravne) {
        this.folderNeispravne = folderNeispravne;
    }

}
