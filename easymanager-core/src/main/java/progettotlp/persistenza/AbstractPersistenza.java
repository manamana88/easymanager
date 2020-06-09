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
import progettotlp.facilities.CloudNativeUtils;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

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

    private static Logger logger = LoggerFactory.getLogger(AbstractPersistenza.class);

    public static final String CONNECTION_DRIVER_CLASS = "connection_driver_class";
    public static final String CONNECTION_URL = "connection_url";
    public static final String CONNECTION_USERNAME = "connection_username";
    public static final String CONNECTION_PASSWORD = "connection_password";
    public static final String CONNECTION_DIALECT = "connection_dialect";
    public static final String CONNECTION_SHOW_SQL = "connection_show_sql";

    private static SessionFactory sessionFactory = null;
    protected enum CONJUNCTION_TYPE {AND,OR}

    protected boolean corruptedSessionFactory = false;

    protected AbstractPersistenza() {
        if (sessionFactory == null || corruptedSessionFactory) {
            resetConnector();
        }
    }

    protected AbstractPersistenza(Properties properties) {
        if (sessionFactory == null || corruptedSessionFactory) {
            resetConnector(properties);
        }
    }

    private void resetConnector() {
        Properties properties = new Properties();
        String driverClass = CloudNativeUtils.getEnvOrProperty(CONNECTION_DRIVER_CLASS, "com.mysql.jdbc.Driver");
        logger.info("[{}]:[{}]", CONNECTION_DRIVER_CLASS, driverClass);
        properties.setProperty("hibernate.connection.driver_class", driverClass);

        String connectionUrl = CloudNativeUtils.getEnvOrProperty(CONNECTION_URL, "jdbc:mysql://localhost:3306/easymanager");
        logger.info("[{}]:[{}]", CONNECTION_URL, connectionUrl);
        properties.setProperty("hibernate.connection.url", connectionUrl);

        String connectionUsername = CloudNativeUtils.getEnvOrProperty(CONNECTION_USERNAME, "root");
        logger.info("[{}]:[{}]", CONNECTION_USERNAME, connectionUsername);
        properties.setProperty("hibernate.connection.username", connectionUsername);

        String connectionPassword = CloudNativeUtils.getEnvOrProperty(CONNECTION_PASSWORD, "root");
        properties.setProperty("hibernate.connection.password", connectionPassword);

        String dialect = CloudNativeUtils.getEnvOrProperty(CONNECTION_DIALECT, "org.hibernate.dialect.MySQLDialect");
        logger.info("[{}]:[{}]", CONNECTION_DIALECT, dialect);
        properties.setProperty("hibernate.dialect", dialect);

        String showSql = CloudNativeUtils.getEnvOrProperty(CONNECTION_SHOW_SQL, "false");
        logger.info("[{}]:[{}]", CONNECTION_SHOW_SQL, showSql);
        properties.setProperty("hibernate.show_sql", showSql);

        resetConnector(properties);
    }

    private void resetConnector(Properties properties){
        close();
        sessionFactory = buildSessionFactory(properties);
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
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    protected Session retrieveSession() {
        if (sessionFactory == null || corruptedSessionFactory){
            resetConnector();
        }
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory!=null){
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    protected void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }

    private void closeWork(boolean res, Transaction trx, Session sessione) throws HibernateException {
        if (res) {
            trx.commit();
            sessione.flush();
        }
        closeSession(sessione);
    }

    public <T> T get(Class<T> clazz, Serializable id){
    	Session sessione=null;
    	try{
    		sessione=sessionFactory.openSession();
    		Object obj = sessione.get(clazz, id);
    		return obj!=null? clazz.cast(obj) : null;
    	} catch (HibernateException e){
            corruptedSessionFactory=true;
            throw e;
    	} finally {
    		closeSession(sessione);
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
            corruptedSessionFactory=true;
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
            corruptedSessionFactory=true;
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
            corruptedSessionFactory=true;
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
            corruptedSessionFactory=true;
            res = false;
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw new PersistenzaException("unable to delete", e);
        } finally {
            closeWork(res, trx, sessione);
        }
    }

    protected Criterion createCriterionFromList(String propertyName, List<String> values, CONJUNCTION_TYPE conjunctionType){
        if (values==null || values.isEmpty()){
            return null;
        }
        Criterion restrictions=Restrictions.eq(propertyName, values.get(0));
        for(int i=1; i<values.size(); i++){
            switch (conjunctionType){
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
        for(FatturaInterface f:list){
            initializeFattura(f, initializeDdT, initializeBeni);
        }
    }

    protected void initializeFattura(FatturaInterface f, boolean initializeDdT, boolean initializeBeni){
        if (initializeDdT){
            List<DdTInterface> ddtList = f.getDdt();
            if (ddtList!=null){
                ddtList.size();
                for (DdTInterface d:ddtList){
                    if (initializeBeni){
                        List<BeneInterface> beni = d.getBeni();
                        if (beni!=null){
                            beni.size();
                        }
                    }
                }
            }
        }
    }

    protected void initializeDdT(List<?> list,boolean initializeBeni, boolean initializeFattura){
        for(Object d:list){
        	DdTInterface ddt = (DdTInterface) d;
            initializeDdT(ddt, initializeBeni, initializeFattura);
        }
    }

    protected void initializeDdT(DdTInterface d,boolean initializeBeni, boolean initializeFattura){
        if (initializeFattura){
            FatturaInterface fattura = d.getFattura();
            if (fattura!=null){
                fattura.hashCode();
            }
        }
        if (initializeBeni){
            List<BeneInterface> beni = d.getBeni();
            if (beni!=null){
                beni.size();
            }
        }
    }

}
