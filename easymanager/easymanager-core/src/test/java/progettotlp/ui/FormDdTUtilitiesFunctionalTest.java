/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.Date;
import java.util.Arrays;
import java.io.File;
import java.net.URL;
import progettotlp.persistenza.AbstractPersistenza;
import progettotlp.persistenza.AbstractTest;
import progettotlp.ui.FormDdTUtilities.DdTTableColumn;
import progettotlp.ui.FormDdTUtilities.Tipo;
import progettotlp.ui.FormDdTUtilities.PortoType;
import progettotlp.ui.FormDdTUtilities.MezzoType;
import javax.swing.JTable;
import progettotlp.persistenza.AziendaManager;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JDialog;
import org.junit.Before;
import org.junit.Test;
import progettotlp.persistenza.AziendaManagerImpl;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.DdTManagerImpl;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.DateUtils;
import progettotlp.models.BeniTableModelUtils;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormDdTUtilitiesFunctionalTest extends AbstractTest{
    protected FormDdTUtilities form;

    public FormDdTUtilitiesFunctionalTest() {
        form=new FormDdTUtilities(new JDialog(), null, null);
    }

    @Before
    public void init() throws Exception {
        List<Class<?extends Object>> ignore = new ArrayList<Class<? extends Object>>();
        ignore.add(AziendaManager.class);
        ignore.add(DdTManager.class);
        UiTestUtilities.initializeClass(form, ignore);
        form.tipo.setMaximumRowCount(3);
        form.tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "in conto", "a saldo" }));
        form.mezzo.setMaximumRowCount(2);
        form.mezzo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cessionario", "Cedente", "Vettore" }));
        form.porto.setMaximumRowCount(3);
        form.porto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Destinazione", "Franco" }));
        form.beni.setModel(BeniTableModelUtils.getDefaultTableModel());

        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);

    }

    @Test
    public void testSalvaDdT() throws Exception {
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

        JTable t=form.beni;

        t.setValueAt(campB1,0,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB1,0,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB1,0,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB1,0,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB1,0,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB1,0,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB1,0,DdTTableColumn.PROT.toInt());
        t.setValueAt(iaB1,0,DdTTableColumn.INT_ADE.toInt());
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB1,0,DdTTableColumn.RIGA_ID.toInt());

        t.setValueAt(campB2,1,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB2,1,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB2,1,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB2,1,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB2,1,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB2,1,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB2,1,DdTTableColumn.PROT.toInt());
        t.setValueAt(iaB2,1,DdTTableColumn.INT_ADE.toInt());
        t.setValueAt(qtaB2,1,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB2,1,DdTTableColumn.RIGA_ID.toInt());

        Integer id=new Integer(1);
        Integer wrongId=new Integer(106);
        Date data=new Date();
        form.numero.setText(wrongId.toString());
        form.data.setText(DateUtils.formatDate(data));

        final String aziendaNome = "azienda1";
        form.azienda.addItem(aziendaNome);
        
        try{
            form.salvaDdT();
            fail();
        } catch (YetExistException e){}

        form.numero.setText(id.toString());
        final String expectedResult = id.toString() + " del " + DateUtils.formatDate(data);
        assertEquals(expectedResult,form.salvaDdT());

        DdT retrieved = retrieveObject(DdT.class, 6L,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        assertTrue(retrieved.getAnnotazioni().isEmpty());
        assertTrue(retrieved.getAspettoEsteriore().isEmpty());
        assertTrue(retrieved.getCausale().isEmpty());
        assertEquals(aziendaNome,retrieved.getCliente().getNome());
        assertNull(retrieved.getColli());
        assertEquals(DateUtils.formatDate(data),DateUtils.formatDate(retrieved.getData()));
        assertTrue(retrieved.getDestinazione().isEmpty());
        assertEquals(id,retrieved.getId());
        assertEquals(FormDdTUtilities.MezzoType.CESSIONARIO.toString(),retrieved.getMezzo());
        assertNull(retrieved.getPeso());
        assertTrue(retrieved.getPorto().isEmpty());
        assertNull(retrieved.getProgressivo());
        assertTrue(retrieved.getRitiro().isEmpty());
        assertTrue(retrieved.getTipo().isEmpty());
        assertTrue(retrieved.getVostroOrdine().isEmpty());
        assertTrue(retrieved.getVostroOrdineDel().isEmpty());

        List<Bene> beni = retrieved.getBeni();
        assertEquals(2, beni.size());
        Bene b=beni.get(0);
        assertEquals(campB1,b.getCampionario());
        assertEquals(codiceB1,b.getCodice());
        assertEquals(commessaB1,b.getCommessa());
        assertEquals(descrizioneB1,b.getDescrizione());
        assertEquals(piazzB1,b.getPiazzato());
        assertEquals(pcB1,b.getPrimoCapo());
        assertEquals(protB1,b.getPrototipo());
        assertEquals(iaB1,b.getInteramenteAdesivato());
        assertEquals(qtaB1,b.getQta());
        assertEquals(idB1,b.getId());

        b=beni.get(1);
        assertEquals(campB2,b.getCampionario());
        assertEquals(codiceB2,b.getCodice());
        assertEquals(commessaB2,b.getCommessa());
        assertEquals(descrizioneB2,b.getDescrizione());
        assertEquals(piazzB2,b.getPiazzato());
        assertEquals(pcB2,b.getPrimoCapo());
        assertEquals(protB2,b.getPrototipo());
        assertEquals(iaB2,b.getInteramenteAdesivato());
        assertEquals(qtaB2,b.getQta());
        assertEquals(idB2,b.getId());
    }

    @Test
    public void testModificaDdT() throws Exception {
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

        JTable t=form.beni;

        t.setValueAt(campB1,0,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB1,0,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB1,0,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB1,0,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB1,0,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB1,0,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB1,0,DdTTableColumn.PROT.toInt());
        t.setValueAt(iaB1,0,DdTTableColumn.INT_ADE.toInt());
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB1,0,DdTTableColumn.RIGA_ID.toInt());

        t.setValueAt(campB2,1,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB2,1,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB2,1,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB2,1,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB2,1,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB2,1,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB2,1,DdTTableColumn.PROT.toInt());
        t.setValueAt(iaB2,1,DdTTableColumn.INT_ADE.toInt());
        t.setValueAt(qtaB2,1,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB2,1,DdTTableColumn.RIGA_ID.toInt());

        form.azienda.removeAllItems();
        form.azienda.addItem("azienda1");
        form.azienda.addItem("azienda2");
        form.azienda.addItem("azienda3");
        form.azienda.setSelectedIndex(1);

        String annotazioni = "annotazioni";
        String aspetto = "aspetto";
        String causale = "causale";
        Integer colli=1;
        Date data=DateUtils.parseDate("08/01/2012");
        String destinazione = "destinazione";
        String mezzo = FormDdTUtilities.MezzoType.VETTORE.toString();
        Integer id=new Integer(1);
        Double peso=2D;
        String porto = FormDdTUtilities.PortoType.FRANCO.toString();
        Integer progressivo=1;
        Long realId = 1L;
        String ritiro = "ritiro";
        String tipo = FormDdTUtilities.Tipo.SALDO.toString();
        String vsDel = "vsDel";
        String vsOrdine = "vsOrdine";

        form.annotazioni.setText(annotazioni);
        form.aspetto.setText(aspetto);
        form.causale.setText(causale);
        form.colli.setText(colli.toString());
        form.data.setText(DateUtils.formatDate(data));
        form.destinazione.setText(destinazione);
        form.mezzo.setSelectedItem(mezzo);
        form.numero.setText(id.toString());
        form.peso.setText(peso.toString());
        form.porto.setSelectedItem(porto);
        form.progressivo.setText(progressivo.toString());
        form.realIdDdT.setText(realId.toString());
        form.ritiro.setText(ritiro);
        form.tipo.setSelectedItem(tipo);
        form.vsdel.setText(vsDel);
        form.vsordine.setText(vsOrdine);

        DdT d= retrieveObject(DdT.class, realId,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));
        
        assertEquals("",d.getAnnotazioni());
        assertEquals("",d.getAspettoEsteriore());
        assertEquals("Reso c/adesivazione",d.getCausale());
        assertEquals(new Long(2),d.getCliente().getId());
        assertEquals("azienda1",d.getCliente().getNome());
        assertNull(d.getColli());
        Date oldDdTdata=DateUtils.parseDate("01/05/2012");
        assertEquals(oldDdTdata, d.getData());
        assertNull(d.getDestinazione());
        assertNull(d.getFattura());
        assertEquals(new Integer(106),d.getId());
        assertEquals(FormDdTUtilities.MezzoType.CESSIONARIO.toString(), d.getMezzo());
        assertNull(d.getPeso());
        assertTrue(d.getPorto().isEmpty());
        assertNull(d.getProgressivo());
        assertEquals(new Long(1),d.getRealId());
        assertNull(d.getRitiro());
        assertTrue(d.getTipo().isEmpty());
        assertTrue(d.getVostroOrdine().isEmpty());
        assertNull(d.getVostroOrdineDel());

        for (Bene b:d.getBeni()){
            if (b.getId().equals(1L)){
                assertEquals("cod",b.getCodice());
                assertEquals(false,b.getPiazzato());
                assertEquals(false,b.getPrimoCapo());
                assertEquals(false,b.getPrototipo());
                assertEquals(false,b.getCampionario());
                assertEquals(true,b.getInteramenteAdesivato());
                assertEquals("com",b.getCommessa());
                assertEquals("descr",b.getDescrizione());
                assertEquals(new Float(0),b.getPrezzo());
                assertEquals(new Float(10),b.getQta());
                assertEquals(new Float(0),b.getTot());
            } else if (b.getId().equals(2L)){
                assertEquals("cod2",b.getCodice());
                assertEquals(false,b.getCampionario());
                assertEquals("com2",b.getCommessa());
                assertEquals("descr2",b.getDescrizione());
                assertEquals(false,b.getPiazzato());
                assertEquals(new Float(0),b.getPrezzo());
                assertEquals(false,b.getPrimoCapo());
                assertEquals(true,b.getPrototipo());
                assertEquals(new Float(11),b.getQta());
                assertEquals(new Float(0),b.getTot());
                assertEquals(true,b.getInteramenteAdesivato());
            } else {
                fail();
            }
        }

        form.modificaDdT();

        d= retrieveObject(DdT.class, realId,(DdTManagerImpl)form.ddtManager,Arrays.asList("beni"));

        assertEquals(annotazioni,d.getAnnotazioni());
        assertEquals(aspetto,d.getAspettoEsteriore());
        assertEquals(causale,d.getCausale());
        assertEquals(new Long(3),d.getCliente().getId());
        assertEquals("azienda2",d.getCliente().getNome());
        assertEquals(colli,d.getColli());
        assertEquals(data, d.getData());
        assertEquals(destinazione,d.getDestinazione());
        assertNull(d.getFattura());
        assertEquals(new Integer(1),d.getId());
        assertEquals(mezzo, d.getMezzo());
        assertEquals(peso,d.getPeso());
        assertEquals(porto,d.getPorto());
        assertEquals(progressivo,d.getProgressivo());
        assertEquals(realId,d.getRealId());
        assertEquals(ritiro,d.getRitiro());
        assertEquals(tipo,d.getTipo());
        assertEquals(vsOrdine,d.getVostroOrdine());
        assertEquals(vsDel,d.getVostroOrdineDel());

        for (Bene b:d.getBeni()){
            if (b.getId().equals(1L)){
                assertEquals(codiceB1,b.getCodice());
                assertEquals(commessaB1,b.getCommessa());
                assertEquals(pcB1,b.getPrimoCapo());
                assertEquals(protB1,b.getPrototipo());
                assertEquals(piazzB1,b.getPiazzato());
                assertEquals(campB1,b.getCampionario());
                assertEquals(descrizioneB1,b.getDescrizione());
                assertEquals(new Float(0),b.getPrezzo());
                assertEquals(qtaB1,b.getQta());
                assertEquals(new Float(0),b.getTot());
                assertEquals(true,b.getInteramenteAdesivato());
            } else if (b.getId().equals(2L)){
                assertEquals(codiceB2,b.getCodice());
                assertEquals(campB2,b.getCampionario());
                assertEquals(commessaB2,b.getCommessa());
                assertEquals(descrizioneB2,b.getDescrizione());
                assertEquals(piazzB2,b.getPiazzato());
                assertEquals(new Float(0),b.getPrezzo());
                assertEquals(pcB2,b.getPrimoCapo());
                assertEquals(protB2,b.getPrototipo());
                assertEquals(qtaB2,b.getQta());
                assertEquals(new Float(0),b.getTot());
                assertEquals(true,b.getInteramenteAdesivato());
            } else {
                fail();
            }
        }
    }

    @Test
    public void testCaricaDdTDaForm() throws Exception {
        String annotazioni = "annotazioni";
        String aspetto = "aspetto";
        String causale = "causale";
        String colli = "1";
        Date data = DateUtils.parseDate("01/01/2012");
        String cessionario = MezzoType.CESSIONARIO.toString();
        String numero = "2";
        Double peso = 3.2;
        String franco = PortoType.FRANCO.toString();
        String progressivo = "3";
        String realId = "4";
        String ritiro = "ritiro";
        String saldo = Tipo.SALDO.toString();
        String vsordine = "vsordine";
        String vsdel = "vsdel";
        String aziendaNome = "azienda2";

        Azienda a1=new Azienda();
        a1.setNome("azienda2");

        form.annotazioni.setText(annotazioni);
        form.aspetto.setText(aspetto);
        form.azienda.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"azienda1", "azienda2" }));
        form.azienda.setSelectedItem(aziendaNome);
        form.causale.setText(causale);
        form.colli.setText(colli);
        form.data.setText(DateUtils.formatDate(data));
        form.mezzo.setSelectedItem(cessionario);
        form.numero.setText(numero);
        form.peso.setText(peso.toString());
        form.porto.setSelectedItem(franco);
        form.progressivo.setText(progressivo);
        form.realIdDdT.setText(realId);
        form.ritiro.setText(ritiro);
        form.tipo.setSelectedItem(saldo);
        form.vsordine.setText(vsordine);
        form.vsdel.setText(vsdel);

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

        //Now the table is empty
        try{
            form.caricaDdTDaForm();
            fail();
        } catch (ValidationException e){}

        JTable t=form.beni;

        t.setValueAt(campB1,0,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB1,0,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB1,0,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB1,0,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB1,0,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB1,0,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB1,0,DdTTableColumn.PROT.toInt());
        t.setValueAt(iaB1,0,DdTTableColumn.INT_ADE.toInt());
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB1,0,DdTTableColumn.RIGA_ID.toInt());

        t.setValueAt(campB2,1,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB2,1,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB2,1,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB2,1,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB2,1,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB2,1,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB2,1,DdTTableColumn.PROT.toInt());
        t.setValueAt(iaB2,1,DdTTableColumn.INT_ADE.toInt());
        t.setValueAt(qtaB2,1,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB2,1,DdTTableColumn.RIGA_ID.toInt());

        DdT d = form.caricaDdTDaForm();

        assertEquals(form.annotazioni.getText(),d.getAnnotazioni());
        assertEquals(form.aspetto.getText(),d.getAspettoEsteriore());
        assertEquals(form.azienda.getSelectedItem(),d.getCliente().getNome());
        assertEquals(form.causale.getText(),d.getCausale());
        assertEquals(form.colli.getText(),d.getColli().toString());
        assertEquals(form.data.getText(),DateUtils.formatDate(d.getData()));
        assertEquals(form.mezzo.getSelectedItem(),d.getMezzo());
        assertEquals(form.numero.getText(),d.getId().toString());
        assertEquals(form.peso.getText(),d.getPeso().toString());
        assertEquals(form.porto.getSelectedItem(),d.getPorto());
        assertEquals(form.progressivo.getText(),d.getProgressivo().toString());
        assertEquals(form.realIdDdT.getText(),d.getRealId().toString());
        assertEquals(form.ritiro.getText(),d.getRitiro());
        assertEquals(form.tipo.getSelectedItem(),d.getTipo());
        assertEquals(form.vsordine.getText(),d.getVostroOrdine());
        assertEquals(form.vsdel.getText(),d.getVostroOrdineDel());

        assertEquals(2, d.getBeni().size());

        Bene b=d.getBeni().get(0);
        assertEquals(campB1,b.getCampionario());
        assertEquals(codiceB1,b.getCodice());
        assertEquals(commessaB1,b.getCommessa());
        assertEquals(descrizioneB1,b.getDescrizione());
        assertEquals(piazzB1,b.getPiazzato());
        assertEquals(pcB1,b.getPrimoCapo());
        assertEquals(protB1,b.getPrototipo());
        assertEquals(iaB1,b.getInteramenteAdesivato());
        assertEquals(qtaB1,b.getQta());
        assertEquals(idB1,b.getId());

        b=d.getBeni().get(1);
        assertEquals(campB2,b.getCampionario());
        assertEquals(codiceB2,b.getCodice());
        assertEquals(commessaB2,b.getCommessa());
        assertEquals(descrizioneB2,b.getDescrizione());
        assertEquals(piazzB2,b.getPiazzato());
        assertEquals(pcB2,b.getPrimoCapo());
        assertEquals(protB2,b.getPrototipo());
        assertEquals(iaB2,b.getInteramenteAdesivato());
        assertEquals(qtaB2,b.getQta());
        assertEquals(idB2,b.getId());

        t.setValueAt(null, 0, DdTTableColumn.DESCRIZIONE.toInt());
        try{
            form.caricaDdTDaForm();
            fail();
        }catch (ValidationException e){}
        t.setValueAt(descrizioneB1,0,DdTTableColumn.DESCRIZIONE.toInt());

        t.setValueAt(null,0,DdTTableColumn.CODICE.toInt());
        try{
            form.caricaDdTDaForm();
            fail();
        }catch (ValidationException e){}
        t.setValueAt(codiceB1,0,DdTTableColumn.CODICE.toInt());

        t.setValueAt(null,0,DdTTableColumn.QTA.toInt());
        try{
            form.caricaDdTDaForm();
            fail();
        }catch (ValidationException e){}
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());

    }

    @Test
    public void testCaricaListaClienti() {
        form.caricaListaClienti();
        assertEquals(4,form.azienda.getItemCount());
        assertEquals("azienda1",form.azienda.getItemAt(0));
        assertEquals("azienda2",form.azienda.getItemAt(1));
        assertEquals("azienda3",form.azienda.getItemAt(2));
        assertEquals("azienda4",form.azienda.getItemAt(3));

        form.caricaListaClienti(2L);
        assertEquals(0,form.azienda.getSelectedIndex());
        form.caricaListaClienti(3L);
        assertEquals(1,form.azienda.getSelectedIndex());
        form.caricaListaClienti(1L);
        assertEquals(0,form.azienda.getSelectedIndex());
        form.caricaListaClienti(4L);
        assertEquals(2,form.azienda.getSelectedIndex());
        form.caricaListaClienti(5L);
        assertEquals(3,form.azienda.getSelectedIndex());
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AziendaManagerImpl.class);
        res.add(DdTManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return form;
    }
}
