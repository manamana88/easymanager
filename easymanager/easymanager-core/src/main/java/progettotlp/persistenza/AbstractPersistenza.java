/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.io.Serializable;
import java.util.List;

import org.hibernate.cfg.AnnotationConfiguration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.HibernateException;

import progettotlp.exceptions.PersistenzaException;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import progettotlp.classes.AccountEmail;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;

/**
 *
 * @author Vincenzo
 */
public abstract class AbstractPersistenza implements BaseManager{

    protected static Logger logger = LoggerFactory.getLogger(AbstractPersistenza.class);
    protected static SessionFactory sessionFactory = null;
    protected enum CONJUNCTION_TIPE{AND,OR};

    protected AbstractPersistenza() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/easymanager");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "false");
        
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory(properties);
        }
    }

    protected AbstractPersistenza(Properties properties) {
        if (sessionFactory == null) {
            if (properties!=null){
                sessionFactory = buildSessionFactory(properties);
            }
        }
    }

    public static SessionFactory buildSessionFactory(Properties properties) {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new AnnotationConfiguration()
                    .addPackage("progettotlp.classes")
                    .addAnnotatedClass(Azienda.class)
                    .addAnnotatedClass(Bene.class)
                    .addAnnotatedClass(DdT.class)
                    .addAnnotatedClass(Fattura.class)
                    .addAnnotatedClass(AccountEmail.class)
                    .addProperties(properties)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    protected Session retrieveSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory!=null){
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    protected void closeSession(Session session) {
        session.close();
    }

    private void closeWork(boolean res, Transaction trx, Session sessione) throws HibernateException {
        if (res) {
            trx.commit();
            sessione.flush();
        }
        if (sessione != null) {
            sessione.close();
        }
    }

    public <T> T get(Class<T> clazz, Serializable id){
    	Session sessione=null;
    	try{
    		sessione=sessionFactory.openSession();
    		Object obj = sessione.get(clazz, id);
    		return obj!=null? clazz.cast(obj) : null;
    	} finally {
    		sessione.close();
    	}
    	
    }

    protected void save(Object o) throws PersistenzaException {
        Session sessione = null;
        Transaction trx = null;
        boolean res = true;
        try {
            sessione = sessionFactory.openSession();
            trx = sessione.beginTransaction();
            if (sessione.save(o) == null) {
                res = false;
                trx.rollback();
            }
        } catch (Exception e) {
            res = false;
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw new PersistenzaException("unable to save", e);
        } finally {
            closeWork(res, trx, sessione);
        }
    }

    protected void saveOrUpdate(Object o) throws PersistenzaException {
        Session sessione = null;
        Transaction trx = null;
        boolean res = true;
        try {
            sessione = sessionFactory.openSession();
            trx = sessione.beginTransaction();
            sessione.saveOrUpdate(o);
        } catch (Exception e) {
            res = false;
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw new PersistenzaException("unable to saveOrUpdate", e);
        } finally {
            closeWork(res, trx, sessione);
        }
    }

    protected void update(Object o) throws PersistenzaException {
        Session sessione = null;
        Transaction trx = null;
        boolean res = true;
        try {
            sessione = sessionFactory.openSession();
            trx = sessione.beginTransaction();
            sessione.update(o);
        } catch (Exception e) {
            res = false;
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw new PersistenzaException("unable to update", e);
        } finally {
            closeWork(res, trx, sessione);
        }
    }

    protected void delete(Object o) throws PersistenzaException {
        Session sessione = null;
        Transaction trx = null;
        boolean res = true;
        try {
            sessione = sessionFactory.openSession();
            trx = sessione.beginTransaction();
            sessione.delete(o);
        } catch (Exception e) {
            res = false;
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw new PersistenzaException("unable to delete", e);
        } finally {
            closeWork(res, trx, sessione);
        }
    }

    protected Criterion createCriterionFromList(String propertyName, List<String> values, CONJUNCTION_TIPE conjunctionTipe){
        if (values==null || values.isEmpty()){
            return null;
        }
        Criterion restrictions=Restrictions.eq(propertyName, values.get(0));
        for(int i=1; i<values.size(); i++){
            switch (conjunctionTipe){
                case AND:
                    restrictions=Restrictions.and(restrictions, Restrictions.eq(propertyName, values.get(i)));
                    break;
                case OR:
                    restrictions=Restrictions.or(restrictions, Restrictions.eq(propertyName, values.get(i)));
                    break;
            }

        }
        return restrictions;
    }

    protected void initializeFattura(List<Fattura> list,boolean initializeDdT, boolean initializeBeni){
        for(Fattura f:list){
            initializeFattura(f, initializeDdT, initializeBeni);
        }
    }

    protected void initializeFattura(Fattura f, boolean initializeDdT, boolean initializeBeni){
        if (initializeDdT){
            List<DdT> ddtList = f.getDdt();
            if (ddtList!=null){
                ddtList.size();
            }
            for (DdT d:ddtList){
                if (initializeBeni){
                    List<Bene> beni = d.getBeni();
                    if (beni!=null){
                        beni.size();
                    }
                }
            }
        }
    }

    protected void initializeDdT(List<?> list,boolean initializeBeni, boolean initializeFattura){
        for(Object d:list){
        	DdT ddt = (DdT) d;
            initializeDdT(ddt, initializeBeni, initializeFattura);
        }
    }

    protected void initializeDdT(DdT d,boolean initializeBeni, boolean initializeFattura){
        if (initializeFattura){
            Fattura fattura = d.getFattura();
            if (fattura!=null){
                fattura.hashCode();
            }
        }
        if (initializeBeni){
            List<Bene> beni = d.getBeni();
            if (beni!=null){
                beni.size();
            }
        }
    }

}
