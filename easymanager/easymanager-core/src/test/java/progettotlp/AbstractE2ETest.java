/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp;

import org.junit.Ignore;
import org.hibernate.tool.hbm2x.StringUtils;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.After;
import org.hibernate.SessionFactory;
import progettotlp.classes.DdT;
import progettotlp.classes.Bene;
import javax.swing.JList;
import javax.swing.JInternalFrame;
import java.awt.Component;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import progettotlp.classes.Azienda;
import progettotlp.persistenza.AbstractPersistenza;
import static org.junit.Assert.*;


/**
 *
 * @author vincenzo
 */
@Ignore
public class AbstractE2ETest {
    private ProgettoTLPView program;
    private Properties properties=new Properties();
    private Map<String,Map<String,Component>> componentsCache=new HashMap<String, Map<String, Component>>();
    private SessionFactory sessionFactory = null;

    {
        System.setProperty("dialogsEnabled","false");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/easymanager_test");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        if (sessionFactory == null){
            sessionFactory=AbstractPersistenza.buildSessionFactory(properties);
        }
    }
    
    @Before
    public void setUp() throws IOException, SQLException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/clearDBE2E.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        ProgettoTLPApp programApp=ProgettoTLPApp.getApplication();
        programApp.startup(properties);
        program=(ProgettoTLPView)programApp.getMainView();

        if (sessionFactory==null){
            sessionFactory=AbstractPersistenza.buildSessionFactory(properties);
        }
        System.setProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY, "2012");
    }

    @After
    public void tearDown(){
        if (sessionFactory!=null){
            sessionFactory.close();
            sessionFactory=null;
        }
        ProgettoTLPApp.getApplication().exit();
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
        } catch (Exception e){
            if (b!=null){
                b.close();
                b=null;
            }
        } finally{
            closeSessionFactory();
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
    protected <T> T retrieveObject(Class<?> clazz, Serializable id) {
        return retrieveObject(clazz, id,null);
    }
    protected <T> T retrieveObject(Class<?> clazz, Serializable id, List<String> toRetrieve) {
        if (sessionFactory==null){
            buildSessionFactory();
        }
        Session retrieveSession = null;
        try {
            retrieveSession = sessionFactory.openSession();
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
            if (sessionFactory!=null){
                closeSessionFactory();
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
    /*protected <T extends Component> T retrieveComponent(Container form, String componentName, Class<T> clazz){
        Component result=null;
        for (Component component:form.getComponents()){
            if (componentName.equals(component.getName())){
                result=component;
                break;
            }
            if (component instanceof Container){
                result=retrieveComponent((Container)component, componentName,clazz);
                if (result != null){
                    break;
                }
            }
        }
        return result==null?null:(T)result;
    }*/
    protected Component retrieveComponent(Container form, String componentName){
        Map<String, Component> formComponents = componentsCache.get(form.getName());
        boolean isYetLoaded=false;
        if (formComponents == null){
            isYetLoaded=true;
            formComponents=loadContainerChild(form);
            componentsCache.put(componentName, formComponents);
        }
        Component result=formComponents.get(componentName);
        if (result!=null){
            return result;
        } else if (!isYetLoaded){
            formComponents=loadContainerChild(form);
            componentsCache.put(componentName, formComponents);
        }
        return formComponents.get(componentName);
    }
    protected <T extends Component> T retrieveComponent(Container form, String componentName, Class<T> clazz){
        return clazz.cast(retrieveComponent(form, componentName));
    }
    private Map<String,Component> loadContainerChild(Container form){
        Map<String,Component> componentsResult = new HashMap<String, Component>();
        for (Component component:form.getComponents()){
            componentsResult.put(component.getName(), component);
            if (component instanceof Container){
                componentsResult.putAll(loadContainerChild((Container)component));
            }
        }
        return componentsResult;
    }

    @Test
    public void registraAziendaTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTestsE2E.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        program.menuClientiAggiungi.doClick();

        JDialog formAzienda = program.formAzienda;
        assertTrue(formAzienda.isVisible());
        for(Component field: formAzienda.getComponents()){
            if (field instanceof JTextField){
                assertTrue(((JTextField)field).getText().isEmpty());
            }
        }

        Boolean giuridico=false;
        String cod_fis = "BRRVCN88M20G482J";
        String email = "vinci.87@tiscali.it";
        String iva = "01234567899";
        String nomeAzienda = "azienda1";

        retrieveComponent(formAzienda, "cod_fis",JTextField.class).setText(cod_fis);
        retrieveComponent(formAzienda, "email",JTextField.class).setText(email);
        retrieveComponent(formAzienda, "giuridico",JCheckBox.class).setSelected(giuridico);
        retrieveComponent(formAzienda, "iva",JTextField.class).setText(iva);
        retrieveComponent(formAzienda, "nomeAzienda",JTextField.class).setText(nomeAzienda);

        retrieveComponent(formAzienda, "registra",JButton.class).doClick();

        Azienda retrieved = retrieveObject(Azienda.class, 4L);
        assertNotNull(retrieved);
        assertEquals(false, retrieved.isPrincipale());
        assertEquals(cod_fis, retrieved.getCodFis());
        assertEquals(email, retrieved.getMail());
        assertEquals(iva, retrieved.getPIva());
        assertEquals(nomeAzienda, retrieved.getNome());
    }

    @Test
    public void modificaAziendaTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTestsE2E.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        program.menuClientiModifica.doClick();

        JInternalFrame sceltaAzienda = program.sceltaAzienda;
        assertTrue(sceltaAzienda.isVisible());
        JList listaAziende = retrieveComponent(sceltaAzienda, "listaAziende", JList.class);
        assertEquals(2, listaAziende.getModel().getSize());
        listaAziende.setSelectedIndex(0);

        program.aziendaScegliModifica.doClick();
        assertFalse(sceltaAzienda.isVisible());
        JDialog formAzienda = program.formAzienda;
        assertTrue(formAzienda.isVisible());
        for(Component field: formAzienda.getComponents()){
            if (field instanceof JTextField){
                assertTrue(((JTextField)field).getText().isEmpty());
            }
        }

        String cap="65128";
        String citta="citta";
        String civico="Civico";
        String cod_fis = "BRRVCN88M20G482J";
        String email = "vinci.87@tiscali.it";
        String fax = "0854322029";
        String iva = "01234567899";
        String nazione = "nazione";
        String nomeAzienda = "azienda1";
        String provincia = "provincia";
        String telefono = "telefono";
        String via = "via";

        retrieveComponent(formAzienda, "cap",JTextField.class).setText(cap);
        retrieveComponent(formAzienda, "citta",JTextField.class).setText(citta);
        retrieveComponent(formAzienda, "civico",JTextField.class).setText(civico);
        retrieveComponent(formAzienda, "cod_fis",JTextField.class).setText(cod_fis);
        retrieveComponent(formAzienda, "email",JTextField.class).setText(email);
        retrieveComponent(formAzienda, "fax",JTextField.class).setText(fax);
        retrieveComponent(formAzienda, "iva",JTextField.class).setText(iva);
        retrieveComponent(formAzienda, "nazione",JTextField.class).setText(nazione);
        retrieveComponent(formAzienda, "nomeAzienda",JTextField.class).setText(nomeAzienda);
        retrieveComponent(formAzienda, "provincia",JTextField.class).setText(provincia);
        retrieveComponent(formAzienda, "telefono",JTextField.class).setText(telefono);
        retrieveComponent(formAzienda, "via",JTextField.class).setText(via);

        retrieveComponent(formAzienda, "modificaAzienda",JButton.class).doClick();

        Azienda retrieved = retrieveObject(Azienda.class, 2L);
        assertNotNull(retrieved);
        assertEquals(cap, retrieved.getCap());
        assertEquals(citta, retrieved.getCitta());
        assertEquals(civico, retrieved.getCivico());
        assertEquals(cod_fis, retrieved.getCodFis());
        assertEquals(fax, retrieved.getFax());
        assertEquals(email, retrieved.getMail());
        assertEquals(nazione, retrieved.getNazione());
        assertEquals(nomeAzienda, retrieved.getNome());
        assertEquals(iva, retrieved.getPIva());
        assertEquals(provincia, retrieved.getProvincia());
        assertEquals(telefono, retrieved.getTelefono());
        assertEquals(via, retrieved.getVia());
        assertEquals(false, retrieved.isPrincipale());
    }

    @Test
    public void modificaAziendaPrincipaleTest() throws Exception{
        program.menuFileGestioneAzienda.doClick();

        JDialog formAzienda = program.formAzienda;
        assertTrue(formAzienda.isVisible());
        assertTrue(retrieveComponent(formAzienda, "modificaAzienda",JButton.class).isVisible());

        String cap="65128";
        String citta="citta";
        String civico="Civico";
        String cod_fis = "BRRVCN88M20G482Q";
        String email = "vinci.87@tiscali.it";
        String fax = "0854322029";
        String iva = "00234567899";
        String nazione = "nazione";
        String nomeAzienda = "aziendaP";
        String provincia = "provincia";
        String telefono = "telefono";
        String via = "via";

        retrieveComponent(formAzienda, "cap",JTextField.class).setText(cap);
        retrieveComponent(formAzienda, "citta",JTextField.class).setText(citta);
        retrieveComponent(formAzienda, "civico",JTextField.class).setText(civico);
        retrieveComponent(formAzienda, "cod_fis",JTextField.class).setText(cod_fis);
        retrieveComponent(formAzienda, "email",JTextField.class).setText(email);
        retrieveComponent(formAzienda, "fax",JTextField.class).setText(fax);
        retrieveComponent(formAzienda, "iva",JTextField.class).setText(iva);
        retrieveComponent(formAzienda, "nazione",JTextField.class).setText(nazione);
        retrieveComponent(formAzienda, "nomeAzienda",JTextField.class).setText(nomeAzienda);
        retrieveComponent(formAzienda, "provincia",JTextField.class).setText(provincia);
        retrieveComponent(formAzienda, "telefono",JTextField.class).setText(telefono);
        retrieveComponent(formAzienda, "via",JTextField.class).setText(via);

        retrieveComponent(formAzienda, "modificaAzienda",JButton.class).doClick();

        //((AbstractPersistenza)persistenza).close();
        //persistenza=new AbstractPersistenza(properties);
        Azienda retrieved = retrieveObject(Azienda.class, 1L);
        assertNotNull(retrieved);
        assertEquals(true, retrieved.isPrincipale());
        assertEquals(nomeAzienda, retrieved.getNome());
        assertEquals(cap, retrieved.getCap());
        assertEquals(citta, retrieved.getCitta());
        assertEquals(civico, retrieved.getCivico());
        assertEquals(cod_fis, retrieved.getCodFis());
        assertEquals(fax, retrieved.getFax());
        assertEquals(email, retrieved.getMail());
        assertEquals(nazione, retrieved.getNazione());
        assertEquals(iva, retrieved.getPIva());
        assertEquals(provincia, retrieved.getProvincia());
        assertEquals(telefono, retrieved.getTelefono());
        assertEquals(via, retrieved.getVia());
    }

    @Test
    public void cancellaAziendaTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTestsE2E.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        program.menuClientiCancella.doClick();

        JInternalFrame sceltaAzienda = program.sceltaAzienda;
        assertTrue(sceltaAzienda.isVisible());
        JList listaAziende = retrieveComponent(sceltaAzienda, "listaAziende", JList.class);
        assertEquals(2, listaAziende.getModel().getSize());
        listaAziende.setSelectedIndex(0);

        program.aziendaScegliCancella.doClick();
        assertEquals(1, listaAziende.getModel().getSize());

        //((AbstractPersistenza)persistenza).close();
        //persistenza=new AbstractPersistenza(properties);
        Azienda retrieved = retrieveObject(Azienda.class, 2L);
        assertNull(retrieved);
    }

    @Test
    public void cancellaDdTTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTestsE2E.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        program.menuDdTCancella.doClick();
        JInternalFrame sceltaDdT = program.sceltaDdT;
        assertTrue(sceltaDdT.isVisible());
        JTable sceltaDdTTable = retrieveComponent(sceltaDdT, "tabellaDdT", JTable.class);
        JButton ddtScegliCancella = retrieveComponent(sceltaDdT, "ddtScegliCancella", JButton.class);

        assertEquals(3,sceltaDdTTable.getRowCount());
        sceltaDdTTable.setRowSelectionInterval(0, 0);
        ddtScegliCancella.doClick();

        //((AbstractPersistenza)persistenza).close();
        //persistenza=new AbstractPersistenza(properties);
        assertNull(retrieveObject(DdT.class, 3L));

        assertEquals(2,sceltaDdTTable.getRowCount());
        sceltaDdTTable.setRowSelectionInterval(0, 0);
        assertNotNull(retrieveObject(DdT.class, 2L));
        ddtScegliCancella.doClick();

        //((AbstractPersistenza)persistenza).close();
        //persistenza=new AbstractPersistenza(properties);
        assertNull(retrieveObject(DdT.class, 2L));

        assertEquals(1,sceltaDdTTable.getRowCount());
        sceltaDdTTable.setRowSelectionInterval(0, 0);
        assertNotNull(retrieveObject(DdT.class, 1L));
        assertNotNull(retrieveObject(Bene.class, 1L));
        assertNotNull(retrieveObject(Bene.class, 2L));
        ddtScegliCancella.doClick();

        //((AbstractPersistenza)persistenza).close();
        //persistenza=new AbstractPersistenza(properties);
        assertNull(retrieveObject(DdT.class, 1L));
        assertNull(retrieveObject(Bene.class, 1L));
        assertNull(retrieveObject(Bene.class, 2L));
        assertEquals(0,sceltaDdTTable.getRowCount());
    }
    
    @Test
    public void visualizzaAziendaTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTestsE2E.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        program.menuClientiVisualizza.doClick();

        JInternalFrame sceltaAzienda = program.sceltaAzienda;
        assertTrue(sceltaAzienda.isVisible());
        JList listaAziende = retrieveComponent(sceltaAzienda, "listaAziende", JList.class);
        assertEquals(2, listaAziende.getModel().getSize());
        listaAziende.setSelectedIndex(0);

        program.aziendaScegliVisualizza.doClick();
        
        JDialog formAzienda = program.formAzienda;
        assertTrue(formAzienda.isVisible());
        for(Component field: formAzienda.getComponents()){
            if (field instanceof JTextField){
                assertFalse(((JTextField)field).isEditable());
            }
        }

        assertFalse(retrieveComponent(formAzienda, "giuridico",JCheckBox.class).isSelected());
        assertEquals("false",retrieveComponent(formAzienda, "aziendaPrincipale",JTextField.class).getText());
        assertEquals("2",retrieveComponent(formAzienda, "aziendaId",JTextField.class).getText());
        assertEquals("65129",retrieveComponent(formAzienda, "cap",JTextField.class).getText());
        assertEquals("Pescara",retrieveComponent(formAzienda, "citta",JTextField.class).getText());
        assertEquals("1",retrieveComponent(formAzienda, "civico",JTextField.class).getText());
        assertEquals("AAAAAAAAAAAAAAAA",retrieveComponent(formAzienda, "cod_fis",JTextField.class).getText());
        assertEquals("vincenzo.barrea88@gmail.com",retrieveComponent(formAzienda, "email",JTextField.class).getText());
        assertEquals("0854322029",retrieveComponent(formAzienda, "fax",JTextField.class).getText());
        assertEquals("01234567891",retrieveComponent(formAzienda, "iva",JTextField.class).getText());
        assertEquals("Italia",retrieveComponent(formAzienda, "nazione",JTextField.class).getText());
        assertEquals("ABTaglio",retrieveComponent(formAzienda, "nomeAzienda",JTextField.class).getText());
        assertEquals("PE",retrieveComponent(formAzienda, "provincia",JTextField.class).getText());
        assertEquals("0854322029",retrieveComponent(formAzienda, "telefono",JTextField.class).getText());
        assertEquals("via A.Volta",retrieveComponent(formAzienda, "via",JTextField.class).getText());

        program.bottoneOk.doClick();

        assertFalse(formAzienda.isVisible());
    }


}
