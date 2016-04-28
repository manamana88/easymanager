/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;


import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.StringUtils;
import progettotlp.models.FatturaTableModelUtils;
import progettotlp.persistenza.AbstractPersistenza;
import progettotlp.persistenza.AbstractTest;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.AziendaManagerImpl;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.DdTManagerImpl;
import progettotlp.persistenza.FatturaManager;
import progettotlp.persistenza.FatturaManagerImpl;
import progettotlp.ui.FormFatturaUtilities.TableColumn;
import progettotlp.ui.FormPrezzoUtilities.CostoType;
import progettotlp.ui.FormPrezzoUtilities.FormPrezzoType;

/**
 *
 * @author vincenzo
 */
public class FormFatturaUtilitiesFunctionalTest extends AbstractTest{

    protected FormFatturaUtilities form;

    public FormFatturaUtilitiesFunctionalTest() {
        form=new FormFatturaUtilities(new JDialog(), null, null, null, null);
    }

    @Before
    public void init() throws Exception {
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

        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaFunctionalTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
    }

    @Test
    public void testCompilaFormFatturaAggiungi() throws Exception{
        DdT d1 = retrieveObject(DdT.class, 1L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        Bene b1 = d1.getBeni().get(0);
        DdT d2 = retrieveObject(DdT.class, 2L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        Bene b2 = d2.getBeni().get(0);
        Azienda a1 = retrieveObject(Azienda.class,2L,(AziendaManagerImpl)form.aziendaManager);

        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*b1.getQta();
        Float totB2=30F;
        
        int lastFattura=1;

        FormPrezzoUtilitiesInterface formPrezzoUtilitiesInterface = EasyMock.createMock(FormPrezzoUtilitiesInterface.class);
        formPrezzoUtilitiesInterface.compilaForm(d1, b1,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(prezzoB1);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.UNITARIO);
        formPrezzoUtilitiesInterface.compilaForm(d2, b2,null);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
        expect(formPrezzoUtilitiesInterface.getPrezzo()).andReturn(totB2);
        expect(formPrezzoUtilitiesInterface.getCostoType()).andReturn(CostoType.TEMPO);
        replay(formPrezzoUtilitiesInterface);

        form.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("30/03/2012");
        Date dataScadenza=DateUtils.parseDate("29/05/2012");

        form.compilaFormFatturaAggiungi(a1, 3);
        assertEquals(a1.getNome(), form.cliente.getText());
        assertEquals(DateUtils.formatDate(dataEmissione), form.dataEmissione.getText());
        assertEquals((lastFattura+1)+"", form.numFatt.getText());
        assertEquals(iva.toString(), form.totIva.getText());
        assertEquals(StringUtils.formatNumber(netto), form.netto.getText());
        assertTrue(form.realIdFattura.getText().isEmpty());
        assertEquals(DateUtils.formatDate(dataScadenza), form.dataScadenza.getText());
        assertEquals(StringUtils.formatNumber(totale), form.totale.getText());
        int totCapi = b1.getQta()+b2.getQta();
        assertEquals(totCapi+"", form.numCapiTot.getText());
        assertEquals("60", form.validita.getSelectedItem());

        assertEquals(2, form.tabellaFattura.getRowCount());
        assertEquals(d1.getId()+" del "+DateUtils.formatDate(d1.getData()), form.tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt()));
        assertEquals(b1.getCodice(), form.tabellaFattura.getValueAt(0, TableColumn.CODICE.toInt()));
        assertEquals(b1.getCommessa(), form.tabellaFattura.getValueAt(0, TableColumn.COMMESSA.toInt()));
        assertEquals(b1.getDescrizione(), form.tabellaFattura.getValueAt(0, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(b1.getPrototipo(), form.tabellaFattura.getValueAt(0, TableColumn.PROT.toInt()));
        assertEquals(b1.getCampionario(), form.tabellaFattura.getValueAt(0, TableColumn.CAMP.toInt()));
        assertEquals(b1.getPrimoCapo(), form.tabellaFattura.getValueAt(0, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(b1.getPiazzato(), form.tabellaFattura.getValueAt(0, TableColumn.PIAZZ.toInt()));
        assertEquals(b1.getQta(), form.tabellaFattura.getValueAt(0, TableColumn.QTA.toInt()));
        assertEquals(prezzoB1, form.tabellaFattura.getValueAt(0, TableColumn.COSTO_UNI.toInt()));
        assertEquals(totB1, form.tabellaFattura.getValueAt(0, TableColumn.TOT.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(0, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(b1.getId(), form.tabellaFattura.getValueAt(0, TableColumn.RIGA_ID.toInt()));
        
        assertEquals(d2.getId()+" del "+DateUtils.formatDate(d2.getData()), form.tabellaFattura.getValueAt(1, TableColumn.RIF_DDT.toInt()));
        assertEquals(b2.getCodice(), form.tabellaFattura.getValueAt(1, TableColumn.CODICE.toInt()));
        assertEquals(b2.getCommessa(), form.tabellaFattura.getValueAt(1, TableColumn.COMMESSA.toInt()));
        assertEquals(b2.getDescrizione(), form.tabellaFattura.getValueAt(1, TableColumn.DESCRIZIONE.toInt()));
        assertEquals(b2.getPrototipo(), form.tabellaFattura.getValueAt(1, TableColumn.PROT.toInt()));
        assertEquals(b2.getCampionario(), form.tabellaFattura.getValueAt(1, TableColumn.CAMP.toInt()));
        assertEquals(b2.getPrimoCapo(), form.tabellaFattura.getValueAt(1, TableColumn.PRIMO_CAPO.toInt()));
        assertEquals(b2.getPiazzato(), form.tabellaFattura.getValueAt(1, TableColumn.PIAZZ.toInt()));
        assertEquals(b2.getQta(), form.tabellaFattura.getValueAt(1, TableColumn.QTA.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(1, TableColumn.COSTO_UNI.toInt()));
        assertEquals(null, form.tabellaFattura.getValueAt(1, TableColumn.TOT.toInt()));
        assertEquals(totB2, form.tabellaFattura.getValueAt(1, TableColumn.TOT_TEMPO.toInt()));
        assertEquals(b2.getId(), form.tabellaFattura.getValueAt(1, TableColumn.RIGA_ID.toInt()));

        verify(formPrezzoUtilitiesInterface);
    }

    @Test
    public void testSalvaFattura() throws Exception {
        DdT d1 = retrieveObject(DdT.class, 1L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        Bene b1 = d1.getBeni().get(0);
        DdT d2 = retrieveObject(DdT.class, 2L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        Bene b2 = d2.getBeni().get(0);
        Azienda a1 = retrieveObject(Azienda.class,2L,(AziendaManagerImpl)form.aziendaManager);

        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*b1.getQta();
        Float totB2=30F;

        int lastFattura=1;

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("30/03/2012");
        Date dataScadenza=DateUtils.parseDate("29/05/2012");

        form.cliente.setText(a1.getNome());
        form.dataEmissione.setText(DateUtils.formatDate(dataEmissione));
        form.numFatt.setText((lastFattura+1)+"");
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(iva.toString());
        form.netto.setText(netto.toString());
        form.dataScadenza.setText(DateUtils.formatDate(dataScadenza));
        form.realIdFattura.setText(null);
        form.totale.setText(totale.toString());
        int totCapi = b1.getQta()+b2.getQta();
        form.numCapiTot.setText(totCapi+"");
        form.validita.setSelectedItem("60");

        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        model.addRow(Arrays.asList(
            d1.getId() + " del " + DateUtils.formatDate(d1.getData()),
            b1.getCodice(),
            b1.getCommessa(),
            b1.getDescrizione(),
            b1.getPrototipo(),
            b1.getCampionario(),
            b1.getPrimoCapo(),
            b1.getPiazzato(),
            b1.getInteramenteAdesivato(),
            b1.getQta(),
            prezzoB1,
            totB1,
            null,
            b1.getId()).toArray());
        model.addRow(Arrays.asList(
            d2.getId() + " del " + DateUtils.formatDate(d2.getData()),
            b2.getCodice(),
            b2.getCommessa(),
            b2.getDescrizione(),
            b2.getPrototipo(),
            b2.getCampionario(),
            b2.getPrimoCapo(),
            b2.getPiazzato(),
            b2.getInteramenteAdesivato(),
            b2.getQta(),
            null,
            null,
            totB2,
            b2.getId()).toArray());

        form.salvaFattura();

        Fattura f1=retrieveObject(Fattura.class, 3L,(FatturaManagerImpl)form.fatturaManager);

        assertNotNull(f1);
        assertEquals(a1.getNome(), f1.getCliente().getNome());
        assertEquals(dataEmissione, f1.getEmissione());
        assertEquals(new Integer(2), f1.getId());
        assertEquals(iva, f1.getIva());
        assertEquals(netto, f1.getNetto());
        assertEquals(new Long(3), f1.getRealId());
        assertEquals(dataScadenza, f1.getScadenza());
        assertEquals(totale, f1.getTotale());

        DdT newD1=retrieveObject(DdT.class, 1L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        Bene newB1=newD1.getBeni().get(0);
        assertEquals( d1.getAnnotazioni(), newD1.getAnnotazioni());
        assertEquals( d1.getAspettoEsteriore(), newD1.getAspettoEsteriore());
        assertEquals( d1.getCausale(), newD1.getCausale());
        assertEquals( d1.getCliente(), newD1.getCliente());
        assertEquals( d1.getColli(), newD1.getColli());
        assertEquals( d1.getData(), newD1.getData());
        assertEquals( d1.getDestinazione(), newD1.getDestinazione());
        assertEquals( d1.getId(), newD1.getId());
        assertEquals( d1.getMezzo(), newD1.getMezzo());
        assertEquals( d1.getPeso(), newD1.getPeso());
        assertEquals( d1.getPorto(), newD1.getPorto());
        assertEquals( d1.getProgressivo(), newD1.getProgressivo());
        assertEquals( d1.getRealId(), newD1.getRealId());
        assertEquals( d1.getRitiro(), newD1.getRitiro());
        assertEquals( d1.getTipo(), newD1.getTipo());
        assertEquals( d1.getVostroOrdine(), newD1.getVostroOrdine());
        assertEquals( d1.getVostroOrdineDel(), newD1.getVostroOrdineDel());
        assertNotNull(newD1.getFattura());
        
        assertEquals(d1.getBeni().size(), newD1.getBeni().size());
        assertEquals(b1.getCodice(),newB1.getCodice());
        assertEquals(b1.getCommessa(),newB1.getCommessa());
        assertEquals(b1.getDescrizione(),newB1.getDescrizione());
        assertEquals(b1.getId(),newB1.getId());
        assertEquals(b1.getQta(),newB1.getQta());
        assertEquals(b1.getCampionario(),newB1.getCampionario());
        assertEquals(b1.getPiazzato(),newB1.getPiazzato());
        assertEquals(b1.getPrimoCapo(),newB1.getPrimoCapo());
        assertEquals(b1.getPrototipo(),newB1.getPrototipo());
        assertEquals(prezzoB1,newB1.getPrezzo());
        assertEquals(totB1,newB1.getTot());
        
        DdT newD2=retrieveObject(DdT.class, 2L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        Bene newB2=newD2.getBeni().get(0);

        assertEquals( d2.getAnnotazioni(), newD2.getAnnotazioni());
        assertEquals( d2.getAspettoEsteriore(), newD2.getAspettoEsteriore());
        assertEquals( d2.getCausale(), newD2.getCausale());
        assertEquals( d2.getCliente(), newD2.getCliente());
        assertEquals( d2.getColli(), newD2.getColli());
        assertEquals( d2.getData(), newD2.getData());
        assertEquals( d2.getDestinazione(), newD2.getDestinazione());
        assertEquals( d2.getId(), newD2.getId());
        assertEquals( d2.getMezzo(), newD2.getMezzo());
        assertEquals( d2.getPeso(), newD2.getPeso());
        assertEquals( d2.getPorto(), newD2.getPorto());
        assertEquals( d2.getProgressivo(), newD2.getProgressivo());
        assertEquals( d2.getRealId(), newD2.getRealId());
        assertEquals( d2.getRitiro(), newD2.getRitiro());
        assertEquals( d2.getTipo(), newD2.getTipo());
        assertEquals( d2.getVostroOrdine(), newD2.getVostroOrdine());
        assertEquals( d2.getVostroOrdineDel(), newD2.getVostroOrdineDel());
        assertNotNull(newD2.getFattura());

        assertEquals(d2.getBeni().size(), newD2.getBeni().size());
        assertEquals(b2.getCodice(),newB2.getCodice());
        assertEquals(b2.getCommessa(),newB2.getCommessa());
        assertEquals(b2.getDescrizione(),newB2.getDescrizione());
        assertEquals(b2.getId(),newB2.getId());
        assertEquals(b2.getQta(),newB2.getQta());
        assertEquals(b2.getCampionario(),newB2.getCampionario());
        assertEquals(b2.getPiazzato(),newB2.getPiazzato());
        assertEquals(b2.getPrimoCapo(),newB2.getPrimoCapo());
        assertEquals(b2.getPrototipo(),newB2.getPrototipo());
        assertNull(newB2.getPrezzo());
        assertEquals(totB2,newB2.getTot());
    }

    @Test
    public void testModificaFattura() throws Exception {
        Long idB1 = 3L;
        String codiceB1 = "0003";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Integer qtaB1 = 15;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 1F;
        Float totB1=prezzoB1*qtaB1;

        Long idB2 = 4L;
        String codiceB2 = "0004";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Integer qtaB2 = 25;
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

        Date dataDdt1=DateUtils.parseDate("01/03/2012");
        Integer idDdt1=3;

        Date dataDdt2=DateUtils.parseDate("02/03/2012");
        Integer idDdt2=4;

        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);
        String rifDdT2=idDdt2+" del "+DateUtils.formatDate(dataDdt2);

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("30/03/2012");
        Date dataScadenza=DateUtils.parseDate("29/05/2012");

        String nomeAzienda = "azienda2";

        Integer fatturaId=1;

        Long realIdFatturaValue=1L;

        //before modify
        Fattura f = retrieveObject(Fattura.class, 1L,(FatturaManagerImpl)form.fatturaManager);
        assertNotNull(f);

        assertEquals(nomeAzienda,f.getCliente().getNome());
        assertEquals(dataEmissione,f.getEmissione());
        assertEquals(fatturaId,f.getId());
        assertEquals(iva,f.getIva());
        assertEquals(netto,f.getNetto());
        assertEquals(realIdFatturaValue,f.getRealId());
        assertEquals(dataScadenza,f.getScadenza());
        assertEquals(totale,f.getTotale());

        DdT d1 = retrieveObject(DdT.class, 3L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));

        assertTrue(d1.getAnnotazioni().isEmpty());
        assertTrue(d1.getAspettoEsteriore().isEmpty());
        assertTrue(d1.getCausale().isEmpty());
        assertEquals(nomeAzienda, d1.getCliente().getNome());
        assertEquals(new Integer(0),d1.getColli());
        assertEquals(dataDdt1, d1.getData());
        assertTrue(d1.getDestinazione().isEmpty());
        assertEquals(idDdt1,d1.getId());
        assertTrue(d1.getMezzo().isEmpty());
        assertEquals(new Double(0),d1.getPeso());
        assertTrue(d1.getPorto().isEmpty());
        assertEquals(new Integer(0),d1.getProgressivo());
        assertEquals(new Long(3),d1.getRealId());
        assertTrue(d1.getRitiro().isEmpty());
        assertTrue(d1.getTipo().isEmpty());
        assertTrue(d1.getVostroOrdine().isEmpty());
        assertTrue(d1.getVostroOrdineDel().isEmpty());

        assertEquals(1, d1.getBeni().size());
        assertEquals(b1, d1.getBeni().get(0));

        DdT d2 = retrieveObject(DdT.class, 4L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));

        assertTrue(d2.getAnnotazioni().isEmpty());
        assertTrue(d2.getAspettoEsteriore().isEmpty());
        assertTrue(d2.getCausale().isEmpty());
        assertEquals(nomeAzienda, d2.getCliente().getNome());
        assertEquals(new Integer(0),d2.getColli());
        assertEquals(dataDdt2, d2.getData());
        assertTrue(d2.getDestinazione().isEmpty());
        assertEquals(idDdt2,d2.getId());
        assertTrue(d2.getMezzo().isEmpty());
        assertEquals(new Double(0),d2.getPeso());
        assertTrue(d2.getPorto().isEmpty());
        assertEquals(new Integer(0),d2.getProgressivo());
        assertEquals(new Long(4),d2.getRealId());
        assertTrue(d2.getRitiro().isEmpty());
        assertTrue(d2.getTipo().isEmpty());
        assertTrue(d2.getVostroOrdine().isEmpty());
        assertTrue(d2.getVostroOrdineDel().isEmpty());

        assertEquals(1, d2.getBeni().size());
        assertEquals(b2, d2.getBeni().get(0));

        //Set modifies
        fatturaId=new Integer(5);
        dataEmissione=DateUtils.parseDate("05/03/2012");
        dataScadenza=DateUtils.parseDate("30/05/2012");
        netto+=1;
        iva+=0.21F;
        totale+=1.21F;

        form.cliente.setText(nomeAzienda);
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(iva.toString());
        form.totale.setText(totale.toString());
        form.dataEmissione.setText(DateUtils.formatDate(dataEmissione));
        form.dataScadenza.setText(DateUtils.formatDate(dataScadenza));
        form.numFatt.setText(fatturaId.toString());

        form.realIdFattura.setText(realIdFatturaValue.toString());
        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        for (int i=model.getRowCount()-1;i>-1;i--)
            model.removeRow(i);
        prezzoB1=null;
        totB1=15F;
        b1.setPrezzo(prezzoB1);
        b1.setTot(totB1);
        prezzoB2=1F;
        totB2=qtaB2*prezzoB2;
        b2.setPrezzo(prezzoB2);
        b2.setTot(totB2);

        model.addRow(Arrays.asList(rifDdT1,codiceB1,commessaB1,descrizioneB1,protB1,campB1,pcB1,piazzB1,iaB1,qtaB1,prezzoB1,null,totB1,idB1).toArray());
        model.addRow(Arrays.asList(rifDdT2,codiceB2,commessaB2,descrizioneB2,protB2,campB2,pcB2,piazzB2,iaB2,qtaB2,prezzoB2,totB2,null,idB2).toArray());

        //apply changes
        assertEquals(fatturaId.toString(), form.modificaFattura());

        //after modify
        f = retrieveObject(Fattura.class, 1L,(FatturaManagerImpl)form.fatturaManager);
        assertNotNull(f);

        assertEquals(nomeAzienda,f.getCliente().getNome());
        assertEquals(dataEmissione,f.getEmissione());
        assertEquals(fatturaId,f.getId());
        assertEquals(iva,f.getIva());
        assertEquals(ConfigurationManager.getIvaDefault(),f.getIvaPerc());
        assertEquals(netto,f.getNetto());
        assertEquals(realIdFatturaValue,f.getRealId());
        assertEquals(dataScadenza,f.getScadenza());
        assertEquals(totale,f.getTotale());

        d1 = retrieveObject(DdT.class, 3L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));

        assertTrue(d1.getAnnotazioni().isEmpty());
        assertTrue(d1.getAspettoEsteriore().isEmpty());
        assertTrue(d1.getCausale().isEmpty());
        assertEquals(nomeAzienda, d1.getCliente().getNome());
        assertEquals(new Integer(0),d1.getColli());
        assertEquals(dataDdt1, d1.getData());
        assertTrue(d1.getDestinazione().isEmpty());
        assertEquals(idDdt1,d1.getId());
        assertTrue(d1.getMezzo().isEmpty());
        assertEquals(new Double(0),d1.getPeso());
        assertTrue(d1.getPorto().isEmpty());
        assertEquals(new Integer(0),d1.getProgressivo());
        assertEquals(new Long(3),d1.getRealId());
        assertTrue(d1.getRitiro().isEmpty());
        assertTrue(d1.getTipo().isEmpty());
        assertTrue(d1.getVostroOrdine().isEmpty());
        assertTrue(d1.getVostroOrdineDel().isEmpty());

        assertEquals(1, d1.getBeni().size());
        assertEquals(b1, d1.getBeni().get(0));

        d2 = retrieveObject(DdT.class, 4L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));

        assertTrue(d2.getAnnotazioni().isEmpty());
        assertTrue(d2.getAspettoEsteriore().isEmpty());
        assertTrue(d2.getCausale().isEmpty());
        assertEquals(nomeAzienda, d2.getCliente().getNome());
        assertEquals(new Integer(0),d2.getColli());
        assertEquals(dataDdt2, d2.getData());
        assertTrue(d2.getDestinazione().isEmpty());
        assertEquals(idDdt2,d2.getId());
        assertTrue(d2.getMezzo().isEmpty());
        assertEquals(new Double(0),d2.getPeso());
        assertTrue(d2.getPorto().isEmpty());
        assertEquals(new Integer(0),d2.getProgressivo());
        assertEquals(new Long(4),d2.getRealId());
        assertTrue(d2.getRitiro().isEmpty());
        assertTrue(d2.getTipo().isEmpty());
        assertTrue(d2.getVostroOrdine().isEmpty());
        assertTrue(d2.getVostroOrdineDel().isEmpty());

        assertEquals(1, d2.getBeni().size());
        assertEquals(b2, d2.getBeni().get(0));
    }

    @Test
    public void testCaricaFatturaDaForm() throws Exception{
        Long idB1 = 3L;
        String codiceB1 = "0003";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Integer qtaB1 = 15;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 4F;
        Float totB1=prezzoB1*qtaB1;

        Long idB2 = 4L;
        String codiceB2 = "0004";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Integer qtaB2 = 25;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        Boolean iaB2 = Boolean.TRUE;
        Float prezzoB2 = null;
        Float totB2=50F;

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

        Date dataDdt1=DateUtils.parseDate("01/03/2012");
        Integer idDdt1=3;

        Date dataDdt2=DateUtils.parseDate("02/03/2012");
        Integer idDdt2=4;

        String rifDdT1=idDdt1+" del "+DateUtils.formatDate(dataDdt1);
        String rifDdT2=idDdt2+" del "+DateUtils.formatDate(dataDdt2);

        Float netto=totB1+totB2;
        Float iva=netto*ConfigurationManager.getIvaDefault()/100;
        Float totale=netto+iva;
        Date dataEmissione=DateUtils.parseDate("30/03/2012");
        Date dataScadenza=DateUtils.parseDate("29/05/2012");

        String nomeAzienda = "azienda2";

        Integer fatturaId=4;

        form.cliente.setText(nomeAzienda);
        form.netto.setText(netto.toString());
        form.ivaPerc.setText(ConfigurationManager.getIvaDefault().toString());
        form.totIva.setText(iva.toString());
        form.totale.setText(totale.toString());
        form.dataEmissione.setText(DateUtils.formatDate(dataEmissione));
        form.dataScadenza.setText(DateUtils.formatDate(dataScadenza));
        form.numFatt.setText(fatturaId.toString());

        Long realIdFatturaValue=1L;
        form.realIdFattura.setText(realIdFatturaValue.toString());
        DefaultTableModel model=(DefaultTableModel)form.tabellaFattura.getModel();
        for (int i=model.getRowCount()-1;i>-1;i--)
            model.removeRow(i);
        model.addRow(Arrays.asList(rifDdT1,codiceB1,commessaB1,descrizioneB1,protB1,campB1,pcB1,piazzB1,iaB1,qtaB1,prezzoB1,totB1,null,idB1).toArray());
        model.addRow(Arrays.asList(rifDdT2,codiceB2,commessaB2,descrizioneB2,protB2,campB2,pcB2,piazzB2,iaB2,qtaB2,prezzoB2,null,totB2,idB2).toArray());
        Fattura f = form.caricaFatturaDaForm();
        assertNotNull(f);

        assertEquals(nomeAzienda,f.getCliente().getNome());
        assertEquals(dataEmissione,f.getEmissione());
        assertEquals(fatturaId,f.getId());
        assertEquals(iva,f.getIva());
        assertEquals(ConfigurationManager.getIvaDefault(),f.getIvaPerc());
        assertEquals(netto,f.getNetto());
        assertEquals(realIdFatturaValue,f.getRealId());
        assertEquals(dataScadenza,f.getScadenza());
        assertEquals(totale,f.getTotale());

        List<DdT> ddtList = f.getDdt();
        assertNotNull(ddtList);
        assertEquals(2, ddtList.size());
        DdT d1 = ddtList.get(0);

        assertTrue(d1.getAnnotazioni().isEmpty());
        assertTrue(d1.getAspettoEsteriore().isEmpty());
        assertTrue(d1.getCausale().isEmpty());
        assertEquals(nomeAzienda, d1.getCliente().getNome());
        assertEquals(new Integer(0),d1.getColli());
        assertEquals(dataDdt1, d1.getData());
        assertTrue(d1.getDestinazione().isEmpty());
        assertEquals(idDdt1,d1.getId());
        assertTrue(d1.getMezzo().isEmpty());
        assertEquals(new Double(0),d1.getPeso());
        assertTrue(d1.getPorto().isEmpty());
        assertEquals(new Integer(0),d1.getProgressivo());
        assertEquals(new Long(3),d1.getRealId());
        assertTrue(d1.getRitiro().isEmpty());
        assertTrue(d1.getTipo().isEmpty());
        assertTrue(d1.getVostroOrdine().isEmpty());
        assertTrue(d1.getVostroOrdineDel().isEmpty());

        assertEquals(1, d1.getBeni().size());
        assertEquals(b1, d1.getBeni().get(0));

        DdT d2 = ddtList.get(1);

        assertTrue(d2.getAnnotazioni().isEmpty());
        assertTrue(d2.getAspettoEsteriore().isEmpty());
        assertTrue(d2.getCausale().isEmpty());
        assertEquals(nomeAzienda, d2.getCliente().getNome());
        assertEquals(new Integer(0),d2.getColli());
        assertEquals(dataDdt2, d2.getData());
        assertTrue(d2.getDestinazione().isEmpty());
        assertEquals(idDdt2,d2.getId());
        assertTrue(d2.getMezzo().isEmpty());
        assertEquals(new Double(0),d2.getPeso());
        assertTrue(d2.getPorto().isEmpty());
        assertEquals(new Integer(0),d2.getProgressivo());
        assertEquals(new Long(4),d2.getRealId());
        assertTrue(d2.getRitiro().isEmpty());
        assertTrue(d2.getTipo().isEmpty());
        assertTrue(d2.getVostroOrdine().isEmpty());
        assertTrue(d2.getVostroOrdineDel().isEmpty());

        assertEquals(1, d2.getBeni().size());
        assertEquals(b2, d2.getBeni().get(0));
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AziendaManagerImpl.class);
        res.add(DdTManagerImpl.class);
        res.add(FatturaManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return form;
    }
}
