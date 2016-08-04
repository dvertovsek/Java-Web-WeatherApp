/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web;

import java.sql.SQLException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nwtis.dvertovs.db.DBController;

/**
 *
 * @author dare
 */
public class UserRequestsResetTask extends TimerTask {

    @Override
    public void run() {
        try {
            DBController.getDbCon().resetUserRequests();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(UserRequestsResetTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
