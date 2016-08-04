/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author dare
 */
public class Vjezba_07_3 extends HttpServlet {

    DretvaPromjena dp;
    
    @Override
    public void init() throws ServletException{
        SlusacPromjena sp = new SlusacPromjena();
        int timeInterval = Integer.parseInt(this.getInitParameter("interval"));
        this.dp = new DretvaPromjena(sp, timeInterval, this.getInitParameter("klasa"));
        dp.start();
        super.init();
    }

    @Override
    public void destroy(){
        if(dp != null && dp.isAlive()){
            dp.interrupt();
        }
        super.destroy();
    }

    
}
