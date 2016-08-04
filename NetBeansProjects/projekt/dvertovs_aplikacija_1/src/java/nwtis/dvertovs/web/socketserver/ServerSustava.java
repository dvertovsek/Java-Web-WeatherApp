/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.socketserver;

import java.io.IOException;
import nwtis.dvertovs.web.MeteoDataListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nwtis.dvertovs.web.slusaci.ContextListener;
import org.foi.nwtis.dvertovs.konfiguracije.*;

/**
 *
 * @author grupa_1
 */
public class ServerSustava extends Thread {

    protected Konfiguracija konfig = null;

    protected int port;
    protected MeteoDataListener meteoDataListener;
    protected MyThreadManager myThreadManager;

    protected boolean end;

    protected ServerSocket ss;
    protected Socket socket;

    public ServerSustava(Konfiguracija konfig, MeteoDataListener meteoDataListener) {
        this.konfig = konfig;
        this.meteoDataListener = meteoDataListener;

        this.port = Integer.parseInt(konfig.dajPostavku("portPrimitivnogPosluzitelja"));

        int threadNo = Integer.parseInt(konfig.dajPostavku("brojDretviPrimitivnogPosluzitelja"));
        this.myThreadManager = new MyThreadManager(threadNo, meteoDataListener, this.konfig);
    }

    @Override
    public void run() {

        while (!end) {
            try {
                if (ss == null) {
                    ss = new ServerSocket(port);
                }
                socket = ss.accept();
                myThreadManager.selectResponseThread(socket);
            } catch (IOException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void end(){
        try {
            ss.close();
        } catch (IOException ex) {
            end = true;
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
