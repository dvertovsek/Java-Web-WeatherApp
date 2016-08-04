/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nwtis.dvertovs.ejb.eb.DvertovsDnevnik;

/**
 *
 * @author dare
 */
@Stateless
public class DvertovsDnevnikFacade extends AbstractFacade<DvertovsDnevnik> {

    @PersistenceContext(unitName = "dvertovs_aplikacija_2_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DvertovsDnevnikFacade() {
        super(DvertovsDnevnik.class);
    }
    
}
