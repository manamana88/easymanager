package progettotlp.persistenza;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.StringUtils;
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
            properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/easymanager_test");
            properties.setProperty("hibernate.connection.username", "root");
            properties.setProperty("hibernate.connection.password", "root");
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            properties.setProperty("hibernate.show_sql", "true");
    }
    @Before
    public void setUp() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/clearDB.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        for (Class<? extends AbstractPersistenza> clazz : getManagersClass()){
            AbstractPersistenza manager = initializeManager(clazz);
            assignManager(manager);
        }
        Properties props = new Properties();
        properties.put(ConfigurationManager.DEFAULT_IVA_PERC, "21");
        ConfigurationManager.setProperties(properties);
    }
    @After
    public void tearDown() throws Exception{
        List<Class<? extends AbstractPersistenza>> managersClass = getManagersClass();
        Object objectToInitialize = getObjectToInitialize();
        for (Field f : objectToInitialize.getClass().getDeclaredFields()){
            if (isInList(managersClass, f.getType())){
                f.setAccessible(true);
                AbstractPersistenza get = (AbstractPersistenza)f.get(objectToInitialize);
                get.close();
                f.set(objectToInitialize, null);
            }
        }
    }

    private boolean isInList(List<Class<? extends AbstractPersistenza>> list, Class contains){
        for(Class<? extends AbstractPersistenza> toAnalyze : list){
            if (toAnalyze.equals(contains) || contains.isAssignableFrom(toAnalyze))
                return true;
        }
        return false;
    }
    protected abstract List<Class<? extends AbstractPersistenza>> getManagersClass();
    protected abstract Object getObjectToInitialize();
    protected void assignManager(AbstractPersistenza manager)throws Exception{
        Object objectToInitialize = getObjectToInitialize();
        Class clazz = objectToInitialize.getClass();
        for (Field f : clazz.getDeclaredFields()){
            if (manager.getClass().equals(f.getType()) || f.getType().isInstance(manager)){
                f.setAccessible(true);
                f.set(objectToInitialize, manager);
            }
        }
    }

    private AbstractPersistenza initializeManager(Class<? extends AbstractPersistenza> clazz) throws Exception{
        Constructor<? extends AbstractPersistenza> constructor = clazz.getConstructor(Properties.class);
        return constructor.newInstance(properties);
    }

    protected void buildSessionFactory(){
        if (sessionFactory == null){
            sessionFactory=AbstractPersistenza.buildSessionFactory(properties);
        }
    }

    protected void closeSessionFactory(){
        sessionFactory.close();
        sessionFactory=null;
    }

    protected void executeSQL(File f) throws SQLException, IOException{
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
        } finally{
            closeSessionFactory();
            if (b!=null){
                b.close();
                b=null;
            }
        }
    }
    
    protected void executeSQL(String query) throws SQLException, IOException{
        Session retrieveSession=null;
        Statement statement = null;
        try{
            retrieveSession = sessionFactory.openSession();
            statement=retrieveSession.connection().createStatement();
            System.out.println(query);
            statement.execute(query);
        } finally{
            if (statement!=null)
                statement.close();
            if (retrieveSession!=null){
                retrieveSession.flush();
                retrieveSession.close();
            }
        }
    }
    protected <T> T retrieveObject(Class<?> clazz, Serializable id, AbstractPersistenza a) {
        return retrieveObject(clazz, id,a,null);
    }
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
        Class<? extends Object> clazz = o.getClass();
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
