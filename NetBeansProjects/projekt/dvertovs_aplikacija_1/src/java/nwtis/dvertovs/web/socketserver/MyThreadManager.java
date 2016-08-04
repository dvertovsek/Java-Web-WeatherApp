/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.socketserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import nwtis.dvertovs.web.MeteoDataListener;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;

/**
 *
 * @author dare
 */
public class MyThreadManager {

    protected Konfiguracija konfig;
    protected int threadNo;
    protected int lastThreadResponded;
    protected ThreadGroup threadGroup;
    protected ArrayList<ObradaZahtjeva> threadList;
    
    protected MeteoDataListener meteoDataListener;

    public MyThreadManager(int threadNo, MeteoDataListener meteoDataListener, Konfiguracija konfig) {
        this.threadGroup = new ThreadGroup("dvertovs");
        this.threadList = new ArrayList<>();
        this.lastThreadResponded = -1;
        this.threadNo = threadNo;
        this.meteoDataListener = meteoDataListener;
        this.konfig = konfig;
        this.generateThreads();
    }

    /**
     * metoda koja generira dretve te ih stavlja u listu
     */
    private void generateThreads() {
        for (int i = 1; i <= this.threadNo; i++) {
            String threadName = "dvertovs_[ " + i + " ]";
            System.out.println("Thread created: " + threadName);
            ObradaZahtjeva oz = new ObradaZahtjeva(this.threadGroup, threadName, konfig, new java.util.Date());
            oz.meteoDataListener = this.meteoDataListener;
            this.threadList.add(oz);
        }
    }
    
     /**
     * metoda odabire slobodnu dretvu iz liste kružnim redoslijedom
     *
     * @param socket priključnica koja će se koristiti za eventualno slanje
     * poruke klijentu o nepostojanju slobodne dretve
     * @throws IOException
     */
    public void selectResponseThread(Socket socket) throws IOException {
        boolean foundResponseThread = false;
        for (int threadsIterated = 0; threadsIterated < this.threadNo && !foundResponseThread; threadsIterated++) {
            ++this.lastThreadResponded;
            this.lastThreadResponded %= this.threadNo;

            if (null != threadList.get(this.lastThreadResponded).getState()) {
                switch (threadList.get(this.lastThreadResponded).getState()) {
                    case WAITING:
                        foundResponseThread = true;
                        threadList.get(this.lastThreadResponded).setSocket(socket);
                        System.out.println("Thread resumed and responded: " + threadList.get(this.lastThreadResponded).getName());
                        threadList.get(this.lastThreadResponded).resumeThread();
                        break;
                    case NEW:
                        foundResponseThread = true;
                        threadList.get(this.lastThreadResponded).setSocket(socket);
                        System.out.println("Thread started and responded: " + threadList.get(this.lastThreadResponded).getName());
                        threadList.get(this.lastThreadResponded).start();
                        break;
                    case BLOCKED:
                    case TIMED_WAITING:
                        System.out.println("Thread busy: " + threadList.get(this.lastThreadResponded).getName());
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
