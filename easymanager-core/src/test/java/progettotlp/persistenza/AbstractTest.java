package progettotlp.persistenza;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.junit.Before;
import org.junit.Ignore;

import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.StringUtils;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.test.AnnualTest;

/**
 *
 * @author Vincenzo
 */
@Ignore
public abstract class AbstractTest extends AnnualTest{
    
    protected Properties properties;
    private SessionFactory sessionFactory = null;

    {
        properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        properties.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:easymanager");
        properties.setProperty("hibernate.connection.username", "sa");
        properties.setProperty("hibernate.connection.password", "");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
    }

    @Before
    public void setUp() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/clearDB.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        properties.put(Property.IVA_DEFAULT.getValue(), "21");
        ConfigurationManager.setProperties(properties);
    }

    protected void buildSessionFactory(){
        if (sessionFactory!=null){
            sessionFactory.close();
        }
        sessionFactory=AbstractPersistenza.buildSessionFactory(properties);
    }

    protected void closeSessionFactory(){
        sessionFactory.close();
        sessionFactory=null;
    }

    protected void executeSQL(File f) throws Exception{
        BufferedReader b=null;
        try{
            buildSessionFactory();
            Pattern p = Pattern.compile("[^;]*;");
            b = new BufferedReader(new FileReader(f));
            String sourceFile="";
            String line;
            while ((line=b.readLine())!=null){
                sourceFile+=line;
                if (p.matcher(sourceFile).matches()){
                    executeSQL(sourceFile);
                    sourceFile="";
                }
            }
        } catch (Throwable e){
        	e.printStackTrace();
        } finally{
            closeSessionFactory();
            if (b!=null){
                b.close();
            }
        }
    }
    
    protected void executeSQL(String query) throws Exception {
        StatelessSession retrieveSession=null;
        Statement statement = null;
        try{
            retrieveSession = sessionFactory.openStatelessSession();
            statement=retrieveSession.connection().createStatement();
            System.out.println(query);
            statement.execute(query);
        } finally{
            if (statement!=null)
                statement.close();
            if (retrieveSession!=null){
                retrieveSession.close();
            }
        }
    }
    protected <T> T retrieveObject(Class<?> clazz, Serializable id, AbstractPersistenza a) {
        return retrieveObject(clazz, id,a,null);
    }

    @SuppressWarnings("unchecked")
    protected <T> T retrieveObject(Class<?> clazz, Serializable id,AbstractPersistenza a, List<String> toRetrieve) {
        Session retrieveSession = null;
        try {
            retrieveSession = a.retrieveSession();
            Object founded = retrieveSession.get(clazz, id);
            if (founded==null){
                return null;
            }
            T toInitialize= (T) founded;
            if (toRetrieve!=null && !toRetrieve.isEmpty()){
                for(String fieldName:toRetrieve){
                    initializeField(fieldName, toInitialize);
                }
            }
            return toInitialize;
        } finally {
            if (retrieveSession != null) {
                retrieveSession.close();
            }
        }
    }
    private void initializeField(String fieldName, Object o){
        Class<?> clazz = o.getClass();
        try {
            Method declaredMethod = clazz.getDeclaredMethod("get" + StringUtils.capitalise(fieldName));
            Object invoke = declaredMethod.invoke(o);
            if (invoke!=null){
                invoke.toString();
            }
        } catch (Exception ex) {}
        try {
            Method declaredMethod = clazz.getMethod("get" + StringUtils.capitalise(fieldName));
            Object invoke = declaredMethod.invoke(o);
            if (invoke!=null){
                invoke.toString();
            }
        } catch (Exception ex) {}
    }
    
}
