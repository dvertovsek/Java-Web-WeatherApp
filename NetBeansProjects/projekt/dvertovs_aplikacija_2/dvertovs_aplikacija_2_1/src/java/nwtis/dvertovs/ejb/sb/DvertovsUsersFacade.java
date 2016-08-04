/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.ejb.sb;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;
import nwtis.dvertovs.ejb.eb.DvertovsUsers_;

/**
 *
 * @author dare
 */
@Stateless
public class DvertovsUsersFacade extends AbstractFacade<DvertovsUsers> {

    @PersistenceContext(unitName = "dvertovs_aplikacija_2_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DvertovsUsersFacade() {
        super(DvertovsUsers.class);
    }

    public DvertovsUsers getByUsername(String username) throws NoResultException, EJBException {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DvertovsUsers> criteria = builder.createQuery(DvertovsUsers.class);
        Root<DvertovsUsers> root = criteria.from(DvertovsUsers.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get(DvertovsUsers_.username), username));
        return em.createQuery(criteria).getSingleResult();
    }
}
