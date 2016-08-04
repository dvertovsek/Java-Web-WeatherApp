/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web;

/**
 *
 * @author dare
 */
public interface MeteoDataListener {
    
    public boolean startCollecting();
    public boolean pauseCollecting();
    public boolean stopCollecting();
    public String getStatus();
}
