/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import progettotlp.persistenza.AziendaManager;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.test.AnnualTest;

/**
 *
 * @author vincenzo
 */
public class FormAziendaUtilitiesTest extends AnnualTest{
    
    FormAziendaUtilities form;
    
    public FormAziendaUtilitiesTest() {
         form= new FormAziendaUtilities(new JDialog(), null);
    }

    @Before
    public void init(){
        List<Class<?extends Object>> ignore = new ArrayList<Class<? extends Object>>();
        ignore.add(AziendaManager.class);
        UiTestUtilities.initializeClass(form, ignore);
    }

    @Test
    public void testIvaCopiaPIva() {
    }

    @Test
    public void testGiuridicoAbilita() {
    }

    @Test
    public void testEsciSenzaSalvareAzienda() {
    }

    @Test
    public void testCaricaAziendaDaForm() throws ValidationException {
        String id = "1";
        Boolean aziendaPrincipale=false;
        Boolean giuridico=true;
        String cap = "65129";
        String citta = "Pescara";
        String civico = "32";
        String cod_fis = "BRRVCN88M20G482K";
        String email = "vinci.88@tiscali.it";
        String fax = "0854322129";
        String iva = "01234567890";
        String nazione = "Italia";
        String nomeAzienda = "azienda1";
        String provincia = "Pescara";
        Boolean tassabile=true;
        String telefono = "0854322128";
        String via = "via A. Volta 32";
        
        form.aziendaId.setText(id);
        form.aziendaPrincipale.setText(aziendaPrincipale.toString());
        form.cap.setText(cap);
        form.citta.setText(citta);
        form.civico.setText(civico);
        form.cod_fis.setText(cod_fis);
        form.email.setText(email);
        form.fax.setText(fax);
        form.giuridico.setSelected(giuridico);
        form.iva.setText(iva);
        form.nazione.setText(nazione);
        form.nomeAzienda.setText(nomeAzienda);
        form.provincia.setText(provincia);
        form.tassabile.setSelected(tassabile);
        form.telefono.setText(telefono);
        form.via.setText(via);
        
        Azienda caricaAziendaDaForm = form.caricaAziendaDaForm();
        
        assertEquals(Long.parseLong(id),caricaAziendaDaForm.getId().longValue());
        assertEquals(aziendaPrincipale,caricaAziendaDaForm.isPrincipale());
        assertEquals(cap,caricaAziendaDaForm.getCap());
        assertEquals(citta,caricaAziendaDaForm.getCitta());
        assertEquals(civico,caricaAziendaDaForm.getCivico());
        assertEquals(cod_fis,caricaAziendaDaForm.getCodFis());
        assertEquals(email,caricaAziendaDaForm.getMail());
        assertEquals(fax,caricaAziendaDaForm.getFax());
        assertEquals(iva,caricaAziendaDaForm.getPIva());
        assertEquals(nazione,caricaAziendaDaForm.getNazione());
        assertEquals(nomeAzienda,caricaAziendaDaForm.getNome());
        assertEquals(provincia,caricaAziendaDaForm.getProvincia());
        assertEquals(telefono,caricaAziendaDaForm.getTelefono());
        assertEquals(tassabile,caricaAziendaDaForm.isTassabile());
        assertEquals(via,caricaAziendaDaForm.getVia());
        
        aziendaPrincipale=true;
        form.aziendaPrincipale.setText(aziendaPrincipale.toString());
        caricaAziendaDaForm = form.caricaAziendaDaForm();
        assertEquals(aziendaPrincipale,caricaAziendaDaForm.isPrincipale());
        form.aziendaPrincipale.setText(null);
        caricaAziendaDaForm = form.caricaAziendaDaForm();
        assertEquals(false,caricaAziendaDaForm.isPrincipale());
        
        form.aziendaId.setText(null);
        caricaAziendaDaForm = form.caricaAziendaDaForm();
        assertNull(caricaAziendaDaForm.getId());
    }

    @Test
    public void testCompilaFormAzienda() {
        String id = "1";
        Boolean aziendaPrincipale=false;
        String cap = "65129";
        String citta = "Pescara";
        String civico = "32";
        String cod_fis = "BRRVCN88M20G482K";
        String email = "vinci.88@tiscali.it";
        String fax = "0854322129";
        String iva = "01234567890";
        String nazione = "Italia";
        String nomeAzienda = "azienda1";
        String provincia = "Pescara";
        String telefono = "0854322128";
        String via = "via A. Volta 32";
        Azienda a=new Azienda();
        
        a.setCap(cap);
        a.setCitta(citta);
        a.setCivico(civico);
        a.setCodFis(cod_fis);
        a.setFax(fax);
        a.setId(Long.parseLong(id));
        a.setMail(email);
        a.setNazione(nazione);
        a.setNome(nomeAzienda);
        a.setPIva(iva);
        a.setPrincipale(aziendaPrincipale);
        a.setProvincia(provincia);
        a.setTelefono(telefono);
        a.setVia(via);
        
        form.compilaFormAzienda(a);
        
        assertEquals(id,form.aziendaId.getText());
        assertEquals(aziendaPrincipale.toString(),form.aziendaPrincipale.getText());
        assertEquals(cap,form.cap.getText());
        assertEquals(citta,form.citta.getText());
        assertEquals(civico,form.civico.getText());
        assertEquals(cod_fis,form.cod_fis.getText());
        assertEquals(email,form.email.getText());
        assertEquals(fax,form.fax.getText());
        assertEquals(iva,form.iva.getText());
        assertEquals(nazione,form.nazione.getText());
        assertEquals(nomeAzienda,form.nomeAzienda.getText());
        assertEquals(provincia,form.provincia.getText());
        assertEquals(telefono,form.telefono.getText());
        assertEquals(via,form.via.getText());

        assertFalse(form.giuridico.isSelected());
        assertFalse(form.tassabile.isSelected());

        a.setTassabile(true);
        a.setCodFis(iva);
        form.compilaFormAzienda(a);
                
        assertEquals(id,form.aziendaId.getText());
        assertEquals(aziendaPrincipale.toString(),form.aziendaPrincipale.getText());
        assertEquals(cap,form.cap.getText());
        assertEquals(citta,form.citta.getText());
        assertEquals(civico,form.civico.getText());
        assertEquals(iva,form.cod_fis.getText());
        assertEquals(email,form.email.getText());
        assertEquals(fax,form.fax.getText());
        assertEquals(iva,form.iva.getText());
        assertEquals(nazione,form.nazione.getText());
        assertEquals(nomeAzienda,form.nomeAzienda.getText());
        assertEquals(provincia,form.provincia.getText());
        assertEquals(telefono,form.telefono.getText());
        assertEquals(via,form.via.getText());

        assertTrue(form.giuridico.isSelected());
        assertTrue(form.tassabile.isSelected());
    }

    @Test
    public void testRegistraAzienda() throws ValidationException, GenericExceptionToPrint, PersistenzaException {
        Boolean aziendaPrincipale=false;
        Boolean giuridico=true;
        String cod_fis = "BRRVCN88M20G482K";
        String email = "vinci.88@tiscali.it";
        String iva = "01234567890";
        String nomeAzienda = "azienda1";
        
        form.aziendaPrincipale.setText(aziendaPrincipale.toString());
        form.cod_fis.setText(cod_fis);
        form.email.setText(email);
        form.giuridico.setSelected(giuridico);
        form.iva.setText(iva);
        form.nomeAzienda.setText(nomeAzienda);
        
        Azienda a = new Azienda();
        a.setCap("");
        a.setCitta("");
        a.setCivico("");
        a.setCodFis(cod_fis);
        a.setFax("");
        a.setMail(email);
        a.setNazione("");
        a.setNome(nomeAzienda);
        a.setPIva(iva);
        a.setProvincia("");
        a.setTelefono("");
        a.setVia("");
        a.setPrincipale(false);
        a.setId(null);
        a.setTassabile(false);
        
        AziendaManager aziendaManager = EasyMock.createMock(AziendaManager.class);
        aziendaManager.registraAzienda(a);
        expectLastCall().andThrow(new PersistenzaException());
        aziendaManager.registraAzienda(a);
        replay(aziendaManager);
        form.aziendaManager=aziendaManager;
        try{
            form.registraAzienda();
        } catch (GenericExceptionToPrint e){}
        
        form.registraAzienda();
        
        verify(aziendaManager);
    }

    @Test
    public void testModificaAzienda() throws GenericExceptionToPrint, ValidationException, PersistenzaException {
        Boolean aziendaPrincipale=false;
        Boolean giuridico=true;
        String cod_fis = "BRRVCN88M20G482K";
        String email = "vinci.88@tiscali.it";
        String iva = "01234567890";
        String nomeAzienda = "azienda1";
        Long id=1L;
        
        form.aziendaPrincipale.setText(aziendaPrincipale.toString());
        form.cod_fis.setText(cod_fis);
        form.email.setText(email);
        form.giuridico.setSelected(giuridico);
        form.iva.setText(iva);
        form.nomeAzienda.setText(nomeAzienda);
        form.aziendaId.setText(id.toString());

        Azienda a = new Azienda();
        a.setCap("");
        a.setCitta("");
        a.setCivico("");
        a.setCodFis(cod_fis);
        a.setFax("");
        a.setMail(email);
        a.setNazione("");
        a.setNome(nomeAzienda);
        a.setPIva(iva);
        a.setProvincia("");
        a.setTelefono("");
        a.setVia("");
        a.setPrincipale(false);
        a.setId(id);
        a.setTassabile(false);
        
        AziendaManager aziendaManager = EasyMock.createMock(AziendaManager.class);
        aziendaManager.modificaAzienda(a);
        expectLastCall().andThrow(new PersistenzaException());
        aziendaManager.modificaAzienda(a);
        replay(aziendaManager);
        form.aziendaManager=aziendaManager;
        
        try{
            form.modificaAzienda();
        } catch (GenericExceptionToPrint e){}
        
        form.modificaAzienda();
        
        verify(aziendaManager);
    }

    @Test
    public void testResetFormAzienda() throws Exception{
        form.resetFormAzienda();
        
        Class<? extends FormAziendaUtilities> clazz = form.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields){
            if (JTextField.class.equals(f.getType())){
                String text = (String)f.getType().getMethod("getText").invoke(f.get(form));
                if (f.getName().equals("aziendaPrincipale")){
                    assertEquals("false", text);
                } else {
                    assertTrue(text.isEmpty());
                }
            } else if (JCheckBox.class.equals(f.getType())){
                assertTrue((Boolean)f.getType().getMethod("isSelected").invoke(f.get(form)));
            }
        }
    }

    @Test
    public void testAbilitaFormAzienda() throws Exception {
        form.abilitaFormAzienda();
        
        Class<? extends FormAziendaUtilities> clazz = form.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        List<String> ignore = Arrays.asList("aziendaId","aziendaPrincipale");
        for (Field f : declaredFields){
            boolean isCheckBox = JCheckBox.class.equals(f.getType());
            if ((JTextField.class.equals(f.getType())||isCheckBox) && !ignore.contains(f.getName())){
                String methodName=isCheckBox?"isEnabled":"isEditable";
                assertTrue((Boolean)f.getType().getMethod(methodName).invoke(f.get(form)));
            }
        }
    }

    @Test
    public void testDisabilitaFormAzienda() throws Exception {
        form.disabilitaFormAzienda();
        
        Class<? extends FormAziendaUtilities> clazz = form.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        List<String> ignore = Arrays.asList("aziendaId","aziendaPrincipale");
        for (Field f : declaredFields){
            boolean isCheckBox = JCheckBox.class.equals(f.getType());
            if ((JTextField.class.equals(f.getType())||isCheckBox) && !ignore.contains(f.getName())){
                String methodName=isCheckBox?"isEnabled":"isEditable";
                assertFalse((Boolean)f.getType().getMethod(methodName).invoke(f.get(form)));
            }
        }
    }

    @Test
    public void testShowForm() {
    }

    @Test
    public void testCheckAzienda() throws ValidationException {
        Azienda a = new Azienda();
        a.setNome("azienda1");
        a.setPIva("01234567890");
        a.setCodFis("BRRVCN88M20G482K");
        a.setMail("vinci.88@tiscali.it");

        form.checkAzienda(a);
        a.setNome(null);
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setNome("");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }

        a.setNome("azienda1");
        a.setPIva("0123456789");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setPIva("012345678901");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setPIva("");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setPIva(null);
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }

        a.setPIva("01234567890");
        a.setCodFis("01234567890");
        form.checkAzienda(a);
        a.setCodFis("012345678901");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setCodFis("0123456789");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setCodFis("BRRVCN88M20G482KK");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setCodFis("BRRVCN88M20G482");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setCodFis("BRRVCN88M20G48KK");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setMail("vinci.88tiscali.it");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
        a.setMail("vinci.88@tiscaliit");
        try {
            form.checkAzienda(a);
            fail();
        } catch (ValidationException e) {
        }
    }
}
