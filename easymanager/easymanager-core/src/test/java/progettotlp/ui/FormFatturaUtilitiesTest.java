/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.Properties;

import progettotlp.test.AnnualTest;

import java.text.ParseException;
import java.util.Date;

import progettotlp.exceptions.PersistenzaException;
import progettotlp.ui.FormPrezzoUtilities.CostoType;
import progettotlp.ui.FormPrezzoUtilities.FormPrezzoType;

import org.slf4j.LoggerFactory;

import progettotlp.ui.FormFatturaUtilities.Result;
import progettotlp.ui.FormFatturaUtilities.TableColumn;

import java.util.Arrays;

import progettotlp.classes.DdT;
import progettotlp.classes.Bene;

import javax.swing.JTable;
import javax.swing.JComboBox;

import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.AziendaManager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import progettotlp.persistenza.FatturaManager;
import progettotlp.classes.Azienda;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.StringUtils;
import progettotlp.models.FatturaTableModelUtils;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author vincenzo
 */
public class FormFatturaUtilitiesTest extends AnnualTest{

    private static Logger logger = LoggerFactory.getLogger(FormFatturaUtilitiesTest.class);
    FormFatturaUtilities form;

    public FormFatturaUtilitiesTest() {
    }

    @Before
    public void init() throws Exception {
        form=new FormFatturaUtilities(new JDialog(), null, null, null, null);

        Properties properties = new Properties();
        properties.put(Property.IVA_DEFAULT.getValue(), "21");
        ConfigurationManager.setProperties(properties);

        List<Class<?extends Object>> ignore = new ArrayList<Class<? extends Object>>();
        ignore.add(AziendaManager.class);
        ignore.add(DdTManager.class);
        ignore.add(FatturaManager.class);
        ignore.add(FormPrezzoUtilitiesInterface.class);
        ignore.add(Logger.class);
        ignore.add(Float.class);
        UiTestUtilities.initializeClass(form, ignore);
        form.tabellaFattura.setModel(FatturaTableModelUtils.getDefaultTableModel());
        form.validita.addItem("30");
        form.validita.addItem("60");
        form.validita.addItem("90");
        form.validita.setSelectedIndex(1);
        form.numCapiTot.setEditable(false);
        form.netto.setEditable(false);
        form.totIva.setEditable(false);
        form.totale.setEditable(false);
        form.cliente.setEditable(false);
        form.dataScadenza.setEditable(false);
    }

    @Test
    public void testResetFormFattura() throws Exception {
        form.resetFormFattura();
        final Class<? extends FormFatturaUtilities> formClazz = form.getClass();
        Field[] declaredFields = formClazz.getDeclaredFields();
        for (Field f:declaredFields){
            final Class<?> clazz = f.getType();
            logger.debug("Analyzing "+f.getName());
            if (clazz.equals(JTextField.class)){
                logger.debug("Checking "+f.getName());
                Method declaredMethod = clazz.getMethod("getText");
                assertTrue(((String)declaredMethod.invoke(f.get(form))).isEmpty());
            } else if (clazz.equals(JComboBox.class)){
                logger.debug("Checking "+f.getName());
                Method declaredMethod = clazz.getMethod("getSelectedIndex");
                assertEquals(new Integer(1),(Integer)declaredMethod.invoke(f.get(form)));
            } else if (clazz.equals(JTable.class)){
                logger.debug("Checking "+f.getName());
                Method declaredMethod = clazz.getMethod("getRowCount");
                assertEquals(new Integer(0),(Integer)declaredMethod.invoke(f.get(form)));
            }
        }
    }

    @Test
    public void testSelectValidita() throws Exception{
        Fattura f= new Fattura();
        f.setEmissione(DateUtils.parseDate("01/01/2012"));
        f.setScadenza(DateUtils.parseDate("15/01/2012"));
        try{
            form.selectValidita(f);
            fail();
        } catch (ValidationException e){}

        f.setScadenza(DateUtils.parseDate("31/01/2012"));
        form.selectValidita(f);
        assertEquals("30", form.validita.getSelectedItem()); 

        f.setEmissione(DateUtils.parseDateAndTime("31/01/2013 12:30:45"));
        f.setScadenza(DateUtils.parseDateAndTime("1/04/2013 11:30:45"));
        
        form.selectValidita(f);
        assertEquals("60", form.validita.getSelectedItem());
    }

    @Test
    public void testCompilaFormFattura() throws Exception{
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;


        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        Boolean iaB2 = Boolean.TRUE;
        Float prezzoB2 = null;
        Float totB2=30F;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);
        b1.setInteramenteAdesivato(iaB1);
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);

        Bene b2=new Bene();
        b2.setId(idB2);
        b2.setCodice(codiceB2);
        b2.setCommessa(commessaB2);
        b2.setDescrizione(descrizioneB2);
        b2.setQta(qtaB2);
        b2.setPrototipo(protB2);
        b2.setPiazzato(piazzB2);
        b2.setPrimoCapo(pcB2);
        b2.setCampionario(campB2);
        b2.setInteramenteAdesivato(iaB2);
        b2.setPrezzo(prezzoB2);
        b2.setTot(totB2);

        Date dataDdt1=DateUtils.parseDate("01/01/2012");
        Integer idDdt1=1;

        Date dataDdt2=DateUtils.parseDate("02/01/2012");
        Integer idDdt2=2;

        DdT d1=new DdT();
        d1.setData(dataDdt1);
        d1.setId(idDdt1);
        d1.setBeni(Arrays.asList(b1));

        DdT d2=new DdT();
        d2.setData(dataDdt2);
        d2.setId(idDdt2);
        d2.setBeni(Arrays.asList(b2));

        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);
        String rifDdT2=idDdt2+" del "+DateUtils.formatDate(dataDdt2);

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("01/01/2012");
        Date dataScadenza=DateUtils.parseDate("31/01/2012");

        Azienda azienda=new Azienda();
        azienda.setNome("azienda1");

        Integer fatturaId=1;
        Long fatturaRealId=2L;
        List<DdT> listaDdT = Arrays.asList(d1, d2);
        Fattura f= new Fattura();
        f.setCliente(azienda);
        f.setDdt(listaDdT);
        f.setEmissione(dataEmissione);
        f.setId(fatturaId);
        f.setIvaPerc(ConfigurationManager.getIvaDefault());
        f.setIva(iva);
        f.setNetto(netto);
        f.setRealId(fatturaRealId);
        f.setScadenza(dataScadenza);
        f.setTotale(totale);

        form.compilaFormFattura(f);
        assertEquals(azienda.getNome(), form.cliente.getText());
        assertEquals(DateUtils.formatDate(dataEmissione), form.dataEmissione.getText());
        assertEquals(fatturaId.toString(), form.numFatt.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(iva.toString(), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(netto), form.netto.getText());
        assertEquals(fatturaRealId.toString(), form.realIdFattura.getText());
        assertEquals(DateUtils.formatDate(dataScadenza), form.dataScadenza.getText());
        assertEquals(StringUtils.formatNumber(totale), form.totale.getText());
        Float totCapi = qtaB1 + qtaB2;
        assertEquals(totCapi+"", form.numCapiTot.getText());
        assertEquals("30", form.validita.getSelectedItem());
    }

    @Test
    public void testCompilaFormFatturaAggiungi() throws Exception{
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;


        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        Boolean iaB2 = Boolean.TRUE;
        Float prezzoB2 = null;
        Float totB2=30F;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);
        b1.setInteramenteAdesivato(iaB1);
        b1.setPrezzo(null);
        b1.setTot(null);

        Bene b2=new Bene();
        b2.setId(idB2);
        b2.setCodice(codiceB2);
        b2.setCommessa(commessaB2);
        b2.setDescrizione(descrizioneB2);
        b2.setQta(qtaB2);
        b2.setPrototipo(protB2);
        b2.setPiazzato(piazzB2);
        b2.setPrimoCapo(pcB2);
        b2.setCampionario(campB2);
        b2.setInteramenteAdesivato(iaB2);
        b2.setPrezzo(null);
        b2.setTot(null);

        Date dataDdt1=DateUtils.parseDate("01/03/2012");
        Integer idDdt1=1;

        Date dataDdt2=DateUtils.parseDate("02/03/2012");
        Integer idDdt2=2;

        DdT d1=new DdT();
        d1.setData(dataDdt1);
        d1.setId(idDdt1);
        d1.setBeni(Arrays.asList(b1));

        DdT d2=new DdT();
        d2.setData(dataDdt2);
        d2.setId(idDdt2);
        d2.setBeni(Arrays.asList(b2));
        List<DdT> listaDdT = Arrays.asList(d1, d2);

        Azienda a1 = new Azienda();
        a1.setNome("azienda1");
        a1.setTassabile(true);

        int lastFattura=10;

        DdTManager ddTManager = EasyMock.createMock(DdTManager.class);
        expect(ddTManager.getAllDdT(a1, 1,true,false)).andReturn(listaDdT);
        replay(ddTManager);

        FormPrezzoUtilitiesInterface formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(d1, b1,null);
        expectLastCall().times(2);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
        expectLastCall().times(2);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(prezzoB1);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        formPrezzoUtilitiesInterface.compilaForm(d2, b2,null);
        expectLastCall().times(2);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
        expectLastCall().times(2);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(totB2);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(prezzoB1);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(totB2);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        replay(formPrezzoUtilitiesInterface);
        
        FatturaManager fatturaManager = EasyMock.createMock(FatturaManager.class);
        expect(fatturaManager.getLastFattura()).andReturn(lastFattura);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b1)).andReturn(null);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b2)).andReturn(null);
        expect(fatturaManager.getLastFattura()).andReturn(lastFattura);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b1)).andReturn(null);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b2)).andReturn(null);
        replay(fatturaManager);

        form.ddtManager=ddTManager;
        form.fatturaManager=fatturaManager;
        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        
        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);
        String rifDdT2=idDdt2+" del "+DateUtils.formatDate(dataDdt2);

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("30/03/2012");
        Date dataScadenza=DateUtils.parseDate("29/05/2012");

        form.compilaFormFatturaAggiungi(a1, 1);
        assertEquals(a1.getNome(), form.cliente.getText());
        assertEquals(DateUtils.formatDate(dataEmissione), form.dataEmissione.getText());
        assertEquals((lastFattura+1)+"", form.numFatt.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(iva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(netto), form.netto.getText());
        assertTrue(form.realIdFattura.getText().isEmpty());
        assertEquals(DateUtils.formatDate(dataScadenza), form.dataScadenza.getText());
        assertEquals(StringUtils.formatNumber(totale), form.totale.getText());
        Float totCapi = qtaB1 + qtaB2;
        assertEquals(totCapi+"", form.numCapiTot.getText());
        assertEquals("60", form.validita.getSelectedItem());
        verify(ddTManager);

        a1.setTassabile(false);
        
        ddTManager = EasyMock.createMock(DdTManager.class);
        expect(ddTManager.getAllDdT(a1, 1,true,false)).andReturn(listaDdT);
        replay(ddTManager);
        form.ddtManager=ddTManager;
        
        form.compilaFormFatturaAggiungi(a1, 1);
        assertEquals(StringUtils.formatNumber(0F), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(netto), form.netto.getText());
        assertEquals(StringUtils.formatNumber(netto), form.totale.getText());

        verify(ddTManager);
        verify(formPrezzoUtilitiesInterface);
        verify(fatturaManager);
    }
    
    @Test
    public void testSalvaFattura() throws Exception {
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);
        b1.setInteramenteAdesivato(iaB1);
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);

        Date dataDdt1=DateUtils.parseDate("01/01/2012");
        Integer idDdt1=1;

        DdT d1=new DdT();
        d1.setData(dataDdt1);
        d1.setId(idDdt1);
        d1.setBeni(Arrays.asList(b1));

        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);

        Float netto=totB1;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("31/01/2012");
        Date dataScadenza=DateUtils.parseDate("01/03/2012");

        Azienda azienda=new Azienda();
        azienda.setNome("azienda1");

        Integer fatturaId=1;
        List<DdT> listaDdT = Arrays.asList(d1);
        Fattura f= new Fattura();
        f.setCliente(azienda);
        f.setDdt(listaDdT);
        f.setEmissione(dataEmissione);
        f.setId(fatturaId);
        f.setIvaPerc(ConfigurationManager.getIvaDefault());
        f.setIva(iva);
        f.setNetto(netto);
        f.setRealId(null);
        f.setScadenza(dataScadenza);
        f.setTotale(totale);

        form.cliente.setText(azienda.getNome());
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(iva.toString());
        form.totale.setText(totale.toString());
        form.dataEmissione.setText(DateUtils.formatDate(dataEmissione));
        form.dataScadenza.setText(DateUtils.formatDate(dataScadenza));
        form.numFatt.setText(fatturaId.toString());

        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        for (int i=model.getRowCount()-1;i>-1;i--)
            model.removeRow(i);
        assertNull(form.caricaFatturaDaForm());
        model.addRow(Arrays.asList(rifDdT1,codiceB1,commessaB1,descrizioneB1,protB1,campB1,pcB1,piazzB1,iaB1,qtaB1,prezzoB1,totB1,null,idB1).toArray());

        AziendaManager aziendaManager=EasyMock.createMock(AziendaManager.class);
        expect(aziendaManager.getAziendaPerNome(azienda.getNome())).andReturn(azienda).times(3);
        replay(aziendaManager);

        DdTManager ddTManager = EasyMock.createMock(DdTManager.class);
        expect(ddTManager.getAllDdT(azienda, 1,true,false)).andReturn(listaDdT).times(3);
        replay(ddTManager);

        FatturaManager fatturaManager = EasyMock.createMock(FatturaManager.class);
        expect(fatturaManager.existsFattura(fatturaId)).andReturn(true);
        expect(fatturaManager.existsFattura(fatturaId)).andReturn(false).times(2);
        fatturaManager.registraFattura(f);
        expectLastCall().andThrow(new PersistenzaException());
        fatturaManager.registraFattura(f);
        replay(fatturaManager);

        form.aziendaManager=aziendaManager;
        form.ddtManager=ddTManager;
        form.fatturaManager=fatturaManager;

        try{
            form.salvaFattura();
            fail();
        } catch (YetExistException e){}

        try{
            form.salvaFattura();
            fail();
        } catch (GenericExceptionToPrint e){}

        assertEquals(fatturaId, form.salvaFattura().getId());

        verify(aziendaManager);
        verify(ddTManager);
        verify(fatturaManager);
    }

    @Test
    public void testModificaRiga() throws Exception {
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;

        String rifDdT="1 del 01/01/2012";
        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        for (int i=model.getRowCount()-1;i>-1;i--)
            model.removeRow(i);
        model.addRow(Arrays.asList(rifDdT,codiceB1,commessaB1,descrizioneB1,protB1,campB1,pcB1,piazzB1,iaB1,qtaB1,prezzoB1,totB1,null,idB1).toArray());

        Float netto=253.1F;
        Float totIva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+totIva;

        //case from unitario to tempo and isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(totIva.toString());
        form.totale.setText(totale.toString());
        model.setValueAt(prezzoB1,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT.toInt());
        model.setValueAt(null,0,TableColumn.TOT_TEMPO.toInt());
        
        Float newTotB1=100F;
        FormPrezzoUtilitiesInterface formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1, qtaB1, CostoType.UNITARIO, prezzoB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newTotB1);
        replay(formPrezzoUtilitiesInterface);

        Bene b=new Bene(codiceB1, commessaB1, descrizioneB1, qtaB1, protB1, campB1, protB1, piazzB1,iaB1);
        FatturaManager fatturaManager = EasyMock.createMock(FatturaManager.class);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b)).andReturn(null).times(8);
        replay(fatturaManager);

        form.fatturaManager=fatturaManager;
        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        try{
            form.modificaRiga();
            fail();
        }catch(NoSelectedRow e){}
        
        form.tabellaFattura.setRowSelectionInterval(0, 0);
        form.modificaRiga();

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        Float newNetto=netto-totB1+newTotB1;
        Float newTotIva=newNetto*ConfigurationManager.getIvaDefault()/100;
        Float newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        //case from unitario to unitario and isTassable
        form.netto.setText(StringUtils.formatNumber(netto));
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(StringUtils.formatNumber(totIva));
        form.totale.setText(StringUtils.formatNumber(totale));
        model.setValueAt(prezzoB1,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT.toInt());
        model.setValueAt(null,0,TableColumn.TOT_TEMPO.toInt());
        
        Float newPrezzoB1=100F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, CostoType.UNITARIO, prezzoB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newPrezzoB1);
        replay(formPrezzoUtilitiesInterface);
        
        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        form.modificaRiga();
        newTotB1=newPrezzoB1*qtaB1;

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(newPrezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=newNetto*ConfigurationManager.getIvaDefault()/100;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        //case from tempo to tempo and isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(totIva.toString());
        form.totale.setText(totale.toString());
        model.setValueAt(null,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(null,0,TableColumn.TOT.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT_TEMPO.toInt());

        newTotB1=150F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, CostoType.TEMPO, totB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newTotB1);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        form.modificaRiga();

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=newNetto*ConfigurationManager.getIvaDefault()/100;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        //case from tempo to unitario and isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(totIva.toString());
        form.totale.setText(totale.toString());
        model.setValueAt(null,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(null,0,TableColumn.TOT.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT_TEMPO.toInt());
        
        newPrezzoB1=10F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, CostoType.TEMPO, totB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newPrezzoB1);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        form.modificaRiga();
        newTotB1=newPrezzoB1*qtaB1;

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(newPrezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=newNetto*ConfigurationManager.getIvaDefault()/100;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        /**************************IS_NOT_TASSABLE**********************/
        netto=253.1F;
        totIva=0F;
        totale=netto+totIva;
        //case from unitario to tempo and !isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(totIva.toString());
        form.totale.setText(totale.toString());
        model.setValueAt(prezzoB1,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT.toInt());
        model.setValueAt(null,0,TableColumn.TOT_TEMPO.toInt());

        newTotB1=100F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, CostoType.UNITARIO, prezzoB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newTotB1);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;

        form.tabellaFattura.setRowSelectionInterval(0, 0);
        form.modificaRiga();

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=0F;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        //case from unitario to unitario and !isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(totIva.toString());
        form.totale.setText(totale.toString());
        model.setValueAt(prezzoB1,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT.toInt());
        model.setValueAt(null,0,TableColumn.TOT_TEMPO.toInt());

        newPrezzoB1=100F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, CostoType.UNITARIO, prezzoB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newPrezzoB1);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        form.modificaRiga();
        newTotB1=newPrezzoB1*qtaB1;

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(newPrezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=0F;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        //case from tempo to tempo and !isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(totIva.toString());
        form.totale.setText(totale.toString());
        model.setValueAt(null,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(null,0,TableColumn.TOT.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT_TEMPO.toInt());

        newTotB1=150F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1, qtaB1, CostoType.TEMPO, totB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newTotB1);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        form.modificaRiga();

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=0F;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(formPrezzoUtilitiesInterface);

        //case from tempo to unitario and !isTassable
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(Float.toString(0F));
        form.totale.setText(netto.toString());
        model.setValueAt(null,0,TableColumn.COSTO_UNI.toInt());
        model.setValueAt(null,0,TableColumn.TOT.toInt());
        model.setValueAt(totB1,0,TableColumn.TOT_TEMPO.toInt());

        newPrezzoB1=10F;
        formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1, qtaB1, CostoType.TEMPO, totB1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newPrezzoB1);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        form.modificaRiga();
        newTotB1=newPrezzoB1*qtaB1;

        assertEquals(rifDdT, form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(newPrezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(newTotB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        newNetto=netto-totB1+newTotB1;
        newTotIva=0F;
        newTotale=newNetto+newTotIva;

        assertEquals(StringUtils.formatNumber(newNetto), form.netto.getText());
        assertEquals(ConfigurationManager.getIvaDefault().toString(), form.ivaPerc.getText());
        assertEquals(StringUtils.formatNumber(newTotIva), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(newTotale), form.totale.getText());

        verify(fatturaManager);
        verify(formPrezzoUtilitiesInterface);
    }

    @Test
    public void testModificaFattura() throws Exception {
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);
        b1.setInteramenteAdesivato(iaB1);
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);

        Date dataDdt1=DateUtils.parseDate("01/01/2012");
        Integer idDdt1=1;

        DdT d1=new DdT();
        d1.setData(dataDdt1);
        d1.setId(idDdt1);
        d1.setBeni(Arrays.asList(b1));

        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);

        Float netto=totB1;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("31/01/2012");
        Date dataScadenza=DateUtils.parseDate("01/03/2012");

        Azienda azienda=new Azienda();
        azienda.setNome("azienda1");

        Integer fatturaId=1;
        List<DdT> listaDdT = Arrays.asList(d1);
        Fattura f= new Fattura();
        f.setCliente(azienda);
        f.setDdt(listaDdT);
        f.setEmissione(dataEmissione);
        f.setId(fatturaId);
        f.setIvaPerc(ConfigurationManager.getIvaDefault());
        f.setIva(iva);
        f.setNetto(netto);
        f.setRealId(null);
        f.setScadenza(dataScadenza);
        f.setTotale(totale);

        form.cliente.setText(azienda.getNome());
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(iva.toString());
        form.totale.setText(totale.toString());
        form.dataEmissione.setText(DateUtils.formatDate(dataEmissione));
        form.dataScadenza.setText(DateUtils.formatDate(dataScadenza));
        form.numFatt.setText(fatturaId.toString());

        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        for (int i=model.getRowCount()-1;i>-1;i--)
            model.removeRow(i);
        assertNull(form.caricaFatturaDaForm());
        model.addRow(Arrays.asList(rifDdT1,codiceB1,commessaB1,descrizioneB1,protB1,campB1,pcB1,piazzB1,iaB1,qtaB1,prezzoB1,totB1,null,idB1).toArray());

        AziendaManager aziendaManager=EasyMock.createMock(AziendaManager.class);
        expect(aziendaManager.getAziendaPerNome(azienda.getNome())).andReturn(azienda).times(2);
        replay(aziendaManager);

        DdTManager ddTManager = EasyMock.createMock(DdTManager.class);
        expect(ddTManager.getAllDdT(azienda, 1,true,false)).andReturn(listaDdT).times(2);
        replay(ddTManager);

        FatturaManager fatturaManager = EasyMock.createMock(FatturaManager.class);
        fatturaManager.modificaFattura(f);
        expectLastCall().andThrow(new PersistenzaException());
        fatturaManager.modificaFattura(f);
        replay(fatturaManager);

        form.aziendaManager=aziendaManager;
        form.ddtManager=ddTManager;
        form.fatturaManager=fatturaManager;

        try{
            form.modificaFattura();
            fail();
        } catch (GenericExceptionToPrint e){}

        assertEquals(fatturaId.toString(), form.modificaFattura());

        verify(aziendaManager);
        verify(ddTManager);
        verify(fatturaManager);
    }

    @Test
    public void testAnnullaAggiunta() {
    }

    @Test
    public void testRicalcolaData() throws ParseException {
        String scadenza = "15/01/2012";
        form.dataEmissione.setText("01/01/2012");
        form.dataScadenza.setText(scadenza);
        form.ricalcolaData();
        assertEquals(scadenza,form.dataScadenza.getText());
        form.registraValidita();
        form.ricalcolaData();
        assertEquals(scadenza,form.dataScadenza.getText());
        form.validita.setSelectedIndex(1);
        form.ricalcolaData();
        assertEquals("15/01/2012",form.dataScadenza.getText());
        form.validita.setSelectedIndex(0);
        form.ricalcolaData();
        assertEquals("31/01/2012",form.dataScadenza.getText());
        form.validita.setSelectedIndex(1);
        form.ricalcolaData();
        assertEquals("01/03/2012",form.dataScadenza.getText());
        form.validita.setSelectedIndex(2);
        form.ricalcolaData();
        assertEquals("31/03/2012",form.dataScadenza.getText());
    }

    @Test
    public void testShowForm() {
    }

    @Test
    public void testRemoveRows() {
        DefaultTableModel model = new DefaultTableModel();
        model.addRow(new Object[]{"ciao","ciao2"});
        model.addRow(new Object[]{"ciao","ciao2"});
        form.tabellaFattura.setModel(model);
        assertEquals(2,form.tabellaFattura.getRowCount());
        form.removeRows();
        assertEquals(0,form.tabellaFattura.getRowCount());
    }

    @Test
    public void testCompilaTabellaFattura() throws Exception {
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;


        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        Boolean iaB2 = Boolean.TRUE;
        Float prezzoB2 = null;
        Float totB2=30F;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);
        b1.setInteramenteAdesivato(iaB1);
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);

        Bene b2=new Bene();
        b2.setId(idB2);
        b2.setCodice(codiceB2);
        b2.setCommessa(commessaB2);
        b2.setDescrizione(descrizioneB2);
        b2.setQta(qtaB2);
        b2.setPrototipo(protB2);
        b2.setPiazzato(piazzB2);
        b2.setPrimoCapo(pcB2);
        b2.setCampionario(campB2);
        b2.setInteramenteAdesivato(iaB2);
        b2.setPrezzo(prezzoB2);
        b2.setTot(totB2);

        Date dataDdt1=DateUtils.parseDate("01/01/2012");
        Integer idDdt1=1;
        
        Date dataDdt2=DateUtils.parseDate("02/01/2012");
        Integer idDdt2=2;

        DdT d1=new DdT();
        d1.setData(dataDdt1);
        d1.setId(idDdt1);
        d1.setBeni(Arrays.asList(b1));

        DdT d2=new DdT();
        d2.setData(dataDdt2);
        d2.setId(idDdt2);
        d2.setBeni(Arrays.asList(b2));

        Result compilaTabellaFattura = form.compilaTabellaFattura(Arrays.asList(d1, d2), false);
        assertEquals(new Float(40), new Float(compilaTabellaFattura.totCapi));
        assertEquals(new Float(45), new Float(compilaTabellaFattura.totFattura));
        assertEquals(2, form.tabellaFattura.getRowCount());
        assertEquals(idDdt1+" del "+DateUtils.formatDate(dataDdt1), form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(prezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(totB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        assertEquals(idDdt2+" del "+DateUtils.formatDate(dataDdt2), form.tabellaFattura.getValueAt(1, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB2, form.tabellaFattura.getValueAt(1, TableColumn.CODICE.toInt()));
        assertEquals(commessaB2, form.tabellaFattura.getValueAt(1, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB2, form.tabellaFattura.getValueAt(1, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB2, form.tabellaFattura.getValueAt(1, TableColumn.PROT.toInt()));
        assertEquals(campB2, form.tabellaFattura.getValueAt(1, TableColumn.CAMP.toInt()));
        assertEquals(pcB2, form.tabellaFattura.getValueAt(1, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB2, form.tabellaFattura.getValueAt(1, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB2, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB2, form.tabellaFattura.getValueAt(1, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(1, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(1, TableColumn.TOT.toInt()));
        assertEquals(totB2, form.tabellaFattura.getValueAt(1, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB2, form.tabellaFattura.getValueAt(1, TableColumn.RIGA_ID.toInt()));

        prezzoB1=null;
        totB1=null;
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);
        prezzoB2=null;
        totB2=null;
        b2.setPrezzo(prezzoB2);
        b2.setTot(totB2);
        Float newPrezzoB1=2F;
        Float newTotB2=40F;
        FormPrezzoUtilitiesInterface formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(d1, b1, null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newPrezzoB1);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        formPrezzoUtilitiesInterface.compilaForm(d2, b2, null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(newTotB2);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        replay(formPrezzoUtilitiesInterface);
        FatturaManager fatturaManager = EasyMock.createMock(FatturaManager.class);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b1)).andReturn(null);
        expect(fatturaManager.getLastSameBeneFatturatoInfos(b2)).andReturn(null);
        replay(fatturaManager);
        form.fatturaManager=fatturaManager;
        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;

        compilaTabellaFattura = form.compilaTabellaFattura(Arrays.asList(d1, d2), true);
        assertEquals(new Float(40), new Float(compilaTabellaFattura.totCapi));
        assertEquals(new Float(70), new Float(compilaTabellaFattura.totFattura));
        assertEquals(2, form.tabellaFattura.getRowCount());
        assertEquals(idDdt1+" del "+DateUtils.formatDate(dataDdt1), form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB1, form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(commessaB1, form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB1, form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB1, form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(campB1, form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(pcB1, form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB1, form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB1, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB1, form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(newPrezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(qtaB1*newPrezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB1, form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));

        assertEquals(idDdt2+" del "+DateUtils.formatDate(dataDdt2), form.tabellaFattura.getValueAt(1, TableColumn.RIF_DDT.toInt()));
        assertEquals(codiceB2, form.tabellaFattura.getValueAt(1, TableColumn.CODICE.toInt()));
        assertEquals(commessaB2, form.tabellaFattura.getValueAt(1, TableColumn.COMMESSA.toInt()));
        assertEquals(descrizioneB2, form.tabellaFattura.getValueAt(1, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(protB2, form.tabellaFattura.getValueAt(1, TableColumn.PROT.toInt()));
        assertEquals(campB2, form.tabellaFattura.getValueAt(1, TableColumn.CAMP.toInt()));
        assertEquals(pcB2, form.tabellaFattura.getValueAt(1, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(piazzB2, form.tabellaFattura.getValueAt(1, TableColumn.PIAZZ.toInt()));
        assertEquals(iaB2, form.tabellaFattura.getValueAt(0, TableColumn.INT_ADE.toInt()));
        assertEquals(qtaB2, form.tabellaFattura.getValueAt(1, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(1, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(1, TableColumn.TOT.toInt()));
        assertEquals(newTotB2, form.tabellaFattura.getValueAt(1, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(idB2, form.tabellaFattura.getValueAt(1, TableColumn.RIGA_ID.toInt()));

        verify(formPrezzoUtilitiesInterface);
        verify(fatturaManager);
    }

    @Test
    public void testCaricaFatturaDaForm() throws Exception{
        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;


        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        Boolean iaB2 = Boolean.TRUE;
        Float prezzoB2 = null;
        Float totB2=30F;

        Bene b1=new Bene();
        b1.setId(idB1);
        b1.setCodice(codiceB1);
        b1.setCommessa(commessaB1);
        b1.setDescrizione(descrizioneB1);
        b1.setQta(qtaB1);
        b1.setPrototipo(protB1);
        b1.setPiazzato(piazzB1);
        b1.setPrimoCapo(pcB1);
        b1.setCampionario(campB1);
        b1.setInteramenteAdesivato(iaB1);
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);

        Bene b2=new Bene();
        b2.setId(idB2);
        b2.setCodice(codiceB2);
        b2.setCommessa(commessaB2);
        b2.setDescrizione(descrizioneB2);
        b2.setQta(qtaB2);
        b2.setPrototipo(protB2);
        b2.setPiazzato(piazzB2);
        b2.setPrimoCapo(pcB2);
        b2.setCampionario(campB2);
        b2.setInteramenteAdesivato(iaB2);
        b2.setPrezzo(prezzoB2);
        b2.setTot(totB2);

        Date dataDdt1=DateUtils.parseDate("01/01/2012");
        Integer idDdt1=1;
        
        Date dataDdt2=DateUtils.parseDate("02/01/2012");
        Integer idDdt2=2;

        DdT d1=new DdT();
        d1.setData(dataDdt1);
        d1.setId(idDdt1);
        d1.setBeni(Arrays.asList(b1));

        DdT d2=new DdT();
        d2.setData(dataDdt2);
        d2.setId(idDdt2);
        d2.setBeni(Arrays.asList(b2));

        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);
        String rifDdT2=idDdt2+" del "+DateUtils.formatDate(dataDdt2);

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("31/01/2012");
        Date dataScadenza=DateUtils.parseDate("01/03/2012");

        Azienda azienda=new Azienda();
        azienda.setNome("azienda1");

        Integer fatturaId=1;
        List<DdT> listaDdT = Arrays.asList(d1, d2);
        Fattura f= new Fattura();
        f.setCliente(azienda);
        f.setDdt(listaDdT);
        f.setEmissione(dataEmissione);
        f.setId(fatturaId);
        f.setIvaPerc(ConfigurationManager.getIvaDefault());
        f.setIva(iva);
        f.setNetto(netto);
        f.setRealId(null);
        f.setScadenza(dataScadenza);
        f.setTotale(totale);

        form.cliente.setText(azienda.getNome());
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(iva.toString());
        form.totale.setText(totale.toString());
        form.dataEmissione.setText(DateUtils.formatDate(dataEmissione));
        form.dataScadenza.setText(DateUtils.formatDate(dataScadenza));
        form.numFatt.setText(fatturaId.toString());

        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        for (int i=model.getRowCount()-1;i>-1;i--)
            model.removeRow(i);
        assertNull(form.caricaFatturaDaForm());
        model.addRow(Arrays.asList(rifDdT1,codiceB1,commessaB1,descrizioneB1,protB1,campB1,pcB1,piazzB1,iaB1,qtaB1,prezzoB1,totB1,null,idB1).toArray());
        model.addRow(Arrays.asList(rifDdT2,codiceB2,commessaB2,descrizioneB2,protB2,campB2,pcB2,piazzB2,iaB2,qtaB2,prezzoB2,null,totB2,idB2).toArray());
        
        AziendaManager aziendaManager=EasyMock.createMock(AziendaManager.class);
        expect(aziendaManager.getAziendaPerNome(azienda.getNome())).andReturn(azienda);
        replay(aziendaManager);

        DdTManager ddTManager = EasyMock.createMock(DdTManager.class);
        expect(ddTManager.getAllDdT(azienda, 1,true,false)).andReturn(listaDdT);
        replay(ddTManager);

        form.aziendaManager=aziendaManager;
        form.ddtManager=ddTManager;

        assertEquals(f, form.caricaFatturaDaForm());

        verify(aziendaManager);
        verify(ddTManager);
        
        Long fatturaRealId=2L;
        f.setRealId(fatturaRealId);
        form.realIdFattura.setText(fatturaRealId.toString());

        aziendaManager=EasyMock.createMock(AziendaManager.class);
        expect(aziendaManager.getAziendaPerNome(azienda.getNome())).andReturn(azienda);
        replay(aziendaManager);

        ddTManager = EasyMock.createMock(DdTManager.class);
        expect(ddTManager.getAllDdT(azienda, 1,true,false)).andReturn(listaDdT);
        replay(ddTManager);

        form.aziendaManager=aziendaManager;
        form.ddtManager=ddTManager;

        assertEquals(f, form.caricaFatturaDaForm());

        verify(aziendaManager);
        verify(ddTManager);


    }

    @Test
    public void testDisabilitaFormFattura() throws Exception {
        form.disabilitaFormFattura();
        Class<? extends FormFatturaUtilities> formClazz = form.getClass();
        Field[] declaredFields = formClazz.getDeclaredFields();
        for (Field field : declaredFields){
            Class<?> fieldType = field.getType();
            if (JTextComponent.class.isAssignableFrom(fieldType)){
                String fieldName = field.getName();
                if ("realIdFattura".equals(fieldName)){
                    logger.debug("Skipping "+fieldName);
                    continue;
                }
                if ("ivaPerc".equals(fieldName)){
                    logger.debug("Skipping "+fieldName);
                    continue;
                }
                logger.debug("Analizying "+fieldName);
                Method method = fieldType.getMethod("isEditable");
                assertFalse((Boolean)method.invoke(field.get(form)));
            }
        }
    }

    @Test
    public void testAbilitaFormFattura() throws Exception{
        form.abilitaFormFattura();
        Class<? extends FormFatturaUtilities> formClazz = form.getClass();
        Field[] declaredFields = formClazz.getDeclaredFields();
        for (Field field : declaredFields){
            Class<?> fieldType = field.getType();
            if (JTextComponent.class.isAssignableFrom(fieldType)){
                String fieldName = field.getName();
                if ("ivaPerc".equals(fieldName)){
                    logger.debug("Skipping "+fieldName);
                    continue;
                }
                logger.debug("Analizying "+fieldName);
                Method method = fieldType.getMethod("isEditable");
                Boolean isEditable = (Boolean) method.invoke(field.get(form));
                if ("numFatt".equals(fieldName) || "dataEmissione".equals(fieldName) || "realIdFattura".equals(fieldName)){
                    assertTrue(isEditable);
                } else{
                    assertFalse(isEditable);
                }
            }
        }
    }

}