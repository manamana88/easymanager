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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.interfaces.AziendaInterface;

/**
 *
 * @author vincenzo
 */
@ManagedBean(name="aziendaManager")
@ApplicationScoped
public class AziendaManagerImpl extends AbstractPersistenza implements AziendaManager {

    private static Logger logger = LoggerFactory.getLogger(AziendaManagerImpl.class);

    public AziendaManagerImpl(Properties properties) {
        super(properties);
    }

    public AziendaManagerImpl() {
        super();
    }


    public int getNumAziende() {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Criteria query = sessione.createCriteria(Azienda.class);
            query.setProjection(Projections.rowCount());
            return ((Number)query.list().get(0)).intValue();
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } catch (Throwable e){
            logger.error("ERROR", e);
            return 0;
        } finally {
            if (sessione!=null){
                sessione.flush();
                sessione.close();
            }
        }
    }

    public List<Azienda> getAziende() {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Criteria query = sessione.createCriteria(Azienda.class);
            query.addOrder(Order.asc("nome"));
            return query.list();
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public Azienda getAziendaPerNome(String name) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(Restrictions.eq("nome", name));
            Object result=query.uniqueResult();
            return result==null?null:(Azienda)result;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public List<Azienda> getAziendePerNome(List<String> nomi){
        Session sessione=null;
        if (nomi==null || nomi.isEmpty()){
            return new ArrayList<Azienda>();
        }
        try{
            sessione=retrieveSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(createCriterionFromList("nome", nomi, CONJUNCTION_TYPE.OR));
            List<Azienda> list = query.list();
            return list==null?new ArrayList<Azienda>():list;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public AziendaInterface getAziendaPrincipale() {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(Restrictions.eq("principale", true));
            Object result=query.uniqueResult();
            return result==null?null:(Azienda)result;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }

    public List<Azienda> getAziendeNonPrincipali() {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Criteria query= sessione.createCriteria(Azienda.class);
            query.add(Restrictions.ne("principale", true));
            query.addOrder(Order.asc("nome"));
            List<Azienda> res=query.list();
            return res;
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.flush();
                sessione.close();
            }
        }
    }

    public void registraAzienda(AziendaInterface a) throws PersistenzaException{
        save(a);
    }

    public void modificaAzienda(AziendaInterface f) throws PersistenzaException{
        update(f);
    }

    public void cancellaAzienda(AziendaInterface a) throws PersistenzaException{
        delete(a);
    }

    public Boolean isAziendaTassabileByName(String text) {
        Session sessione=null;
        try{
            sessione=retrieveSession();
            Query createQuery = sessione.createQuery("select a.tassabile from Azienda a where a.nome='" + text + "'");
            return (Boolean)createQuery.uniqueResult();
        } catch (HibernateException e){
            logger.error("Error", e);
            corruptedSessionFactory = true;
            throw e;
        } finally {
            if (sessione!=null) {
                sessione.close();
            }
        }
    }
}
