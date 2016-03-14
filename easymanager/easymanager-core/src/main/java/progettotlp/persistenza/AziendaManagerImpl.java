/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;

/**
 *
 * @author vincenzo
 */
@ManagedBean(name="aziendaManager")
@ApplicationScoped
public class AziendaManagerImpl extends AbstractPersistenza implements AziendaManager {

    public AziendaManagerImpl(Properties properties) {
        super(properties);
    }

    public AziendaManagerImpl() {
        super();
    }


    public int getNumAziende() {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query = sessione.createCriteria(Azienda.class);
            query.setProjection(Projections.rowCount());
            return ((Number)query.list().get(0)).intValue();
        } catch (Throwable e){
            logger.error("ERROR", e);
            return 0;
        } finally {
            sessione.flush();
            sessione.close();
        }
    }

    public List<Azienda> getAziende() {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query = sessione.createCriteria(Azienda.class);
            query.addOrder(Order.asc("nome"));
            return query.list();
        } finally {
            sessione.close();
        }
    }

    public Azienda getAziendaPerNome(String name) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(Restrictions.eq("nome", name));
            Object result=query.uniqueResult();
            return result==null?null:(Azienda)result;
        } finally {
            sessione.close();
        }
    }

    public List<Azienda> getAziendePerNome(List<String> nomi){
        Session sessione=null;
        if (nomi==null || nomi.isEmpty()){
            return new ArrayList<Azienda>();
        }
        try{
            sessione=sessionFactory.openSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(createCriterionFromList("nome", nomi, CONJUNCTION_TIPE.OR));
            List<Azienda> list = query.list();
            return list==null?new ArrayList<Azienda>():list;
        } finally {
            sessione.close();
        }
    }

    public Azienda getAziendaPrincipale() {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(Restrictions.eq("principale", true));
            Object result=query.uniqueResult();
            return result==null?null:(Azienda)result;
        } finally {
            sessione.close();
        }
    }

    public List<Azienda> getAziendeNonPrincipali() {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(Restrictions.ne("principale", true));
            query.addOrder(Order.asc("nome"));
            List<Azienda> res=query.list();
            return res;
        } finally {
            sessione.flush();
            sessione.close();
        }
    }

    public void registraAzienda(Azienda a) throws PersistenzaException{
        save(a);
    }

    public void modificaAzienda(Azienda f) throws PersistenzaException{
        update(f);
    }

    public void cancellaAzienda(Azienda a) throws PersistenzaException{
        delete(a);
    }

    public Boolean isAziendaTassabileByName(String text) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Query createQuery = sessione.createQuery("select a.tassabile from Azienda a where a.nome='" + text + "'");
            return (Boolean)createQuery.uniqueResult();
        } finally {
            sessione.close();
        }
    }
}
