/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import progettotlp.test.AnnualTest;
import java.util.Date;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.ui.FormDdTUtilities.DdTTableColumn;
import progettotlp.ui.FormDdTUtilities.Tipo;
import progettotlp.ui.FormDdTUtilities.PortoType;
import progettotlp.ui.FormDdTUtilities.MezzoType;
import javax.swing.JTable;
import progettotlp.persistenza.AziendaManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JDialog;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import progettotlp.persistenza.DdTManager;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.DateUtils;
import progettotlp.models.BeniTableModelUtils;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormDdTUtilitiesTest extends AnnualTest{
    FormDdTUtilities form;
    
    public FormDdTUtilitiesTest() {
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

        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;

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

        JTable t=form.beni;

        t.setValueAt(campB1,0,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB1,0,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB1,0,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB1,0,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB1,0,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB1,0,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB1,0,DdTTableColumn.PROT.toInt());
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB1,0,DdTTableColumn.RIGA_ID.toInt());

        t.setValueAt(campB2,1,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB2,1,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB2,1,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB2,1,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB2,1,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB2,1,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB2,1,DdTTableColumn.PROT.toInt());
        t.setValueAt(qtaB2,1,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB2,1,DdTTableColumn.RIGA_ID.toInt());

        Integer id=new Integer(1);
        Date data=DateUtils.setMidnight(new Date());
        form.numero.setText(id.toString());
        form.data.setText(DateUtils.formatDate(data));

        DdT d1=new DdT();
        d1.setBeni(Arrays.asList(b1,b2));
        d1.setId(id);
        d1.setData(data);

        d1.setMezzo(MezzoType.CESSIONARIO.toString());
        d1.setCausale("");
        d1.setDestinazione("");
        d1.setVostroOrdine("");
        d1.setVostroOrdineDel("");
        d1.setTipo("");
        d1.setAspettoEsteriore("");
        d1.setPorto("");
        d1.setRitiro("");
        d1.setAnnotazioni("");

        Azienda a1=new Azienda();
        final String aziendaNome = "Azienda1";
        a1.setNome(aziendaNome);
        d1.setCliente(a1);

        form.azienda.addItem(aziendaNome);
        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        expect(a.getAziendaPerNome(aziendaNome)).andReturn(a1).times(3);
        replay(a);
        form.aziendaManager=a;

        DdTManager d = EasyMock.createMock(DdTManager.class);
        expect(d.existsDdTById(d1.getId())).andReturn(Boolean.TRUE);
        expect(d.existsDdTById(d1.getId())).andReturn(Boolean.FALSE).times(2);
        d.registraDdT(d1);
        expectLastCall().andThrow(new PersistenzaException());
        d.registraDdT(d1);
        replay(d);
        form.ddtManager=d;
        
        try{
            form.salvaDdT();
            fail();
        } catch (YetExistException e){}

        try{
            form.salvaDdT();
            fail();
        } catch (GenericExceptionToPrint e){}

        final String expectedResult = d1.getId().toString() + " del " + DateUtils.formatDate(d1.getData());
        assertEquals(expectedResult,form.salvaDdT());
        verify(d);
        verify(a);
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

        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;

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

        JTable t=form.beni;

        t.setValueAt(campB1,0,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB1,0,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB1,0,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB1,0,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB1,0,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB1,0,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB1,0,DdTTableColumn.PROT.toInt());
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB1,0,DdTTableColumn.RIGA_ID.toInt());

        t.setValueAt(campB2,1,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB2,1,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB2,1,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB2,1,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB2,1,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB2,1,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB2,1,DdTTableColumn.PROT.toInt());
        t.setValueAt(qtaB2,1,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB2,1,DdTTableColumn.RIGA_ID.toInt());

        Integer id=new Integer(1);
        Date data=DateUtils.setMidnight(new Date());
        form.numero.setText(id.toString());
        form.data.setText(DateUtils.formatDate(data));

        DdT d1=new DdT();
        d1.setBeni(Arrays.asList(b1,b2));
        d1.setId(id);
        d1.setData(data);


        d1.setMezzo(MezzoType.CESSIONARIO.toString());
        d1.setCausale("");
        d1.setDestinazione("");
        d1.setVostroOrdine("");
        d1.setVostroOrdineDel("");
        d1.setTipo("");
        d1.setAspettoEsteriore("");
        d1.setPorto("");
        d1.setRitiro("");
        d1.setAnnotazioni("");

        Azienda a1=new Azienda();
        final String aziendaNome = "Azienda1";
        a1.setNome(aziendaNome);
        d1.setCliente(a1);
        
        DdTManager d = EasyMock.createMock(DdTManager.class);
        d.modificaDdT(d1);
        expectLastCall().andThrow(new PersistenzaException());
        d.modificaDdT(d1);
        replay(d);
        form.ddtManager=d;

        form.azienda.addItem(aziendaNome);
        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        expect(a.getAziendaPerNome(aziendaNome)).andReturn(a1).times(2);
        replay(a);
        form.aziendaManager=a;

        try{
            form.modificaDdT();
            fail();
        } catch (GenericExceptionToPrint e){}
        
        form.modificaDdT();
        verify(d);
        verify(a);

    }

    @Test
    public void testCompilaFormDdT() throws Exception{
        
        long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        float prezzoB1 = 1F;
        float totB1 = 15F;
        
        long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        float prezzoB2 = 2F;
        float totB2 = 50F;
        
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
        b2.setPrezzo(prezzoB2);
        b2.setTot(totB2);
        
        Azienda a1=new Azienda();
        a1.setNome("azienda1");
        a1.setId(1L);
        Azienda a2=new Azienda();
        a2.setNome("azienda2");
        a2.setId(2L);
        Azienda a3=new Azienda();
        a3.setNome("azienda3");
        a3.setId(3L);
        
        DdT d=new DdT();
        d.setBeni(Arrays.asList(b1,b2));
        d.setData(1, 1, 2012);
        d.setCliente(a2);
        d.setAnnotazioni("annotazioni");
        d.setAspettoEsteriore("aspetto");
        d.setCausale("causale");
        d.setColli(1);
        d.setDestinazione("destinazione");
        d.setId(2);
        d.setMezzo(MezzoType.CESSIONARIO.toString());
        d.setPeso(3D);
        d.setPorto(PortoType.FRANCO.toString());
        d.setProgressivo(4);
        d.setRealId(5L);
        d.setRitiro("ritiro");
        d.setTipo(Tipo.CONTO.toString());
        d.setVostroOrdine("vostroOrdine");
        d.setVostroOrdineDel("vostroOrdineDel");
        
        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        expect(a.getAziendeNonPrincipali()).andReturn(Arrays.asList(a1,a2,a3));
        replay(a);
        
        form.aziendaManager=a;
        form.compilaFormDdT(d);
        
        assertEquals(d.getAnnotazioni(),form.annotazioni.getText());
        assertEquals(d.getAspettoEsteriore(),form.aspetto.getText());
        assertEquals(d.getCliente().getNome(),form.azienda.getSelectedItem());
        assertEquals(d.getCausale(),form.causale.getText());
        assertEquals(d.getColli().toString(),form.colli.getText());
        assertEquals(DateUtils.formatDate(d.getData()),form.data.getText());
        assertEquals(d.getMezzo(),form.mezzo.getSelectedItem());
        assertEquals(d.getId().toString(),form.numero.getText());
        assertEquals(d.getPeso().toString(),form.peso.getText());
        assertEquals(d.getPorto(),form.porto.getSelectedItem());
        assertEquals(d.getProgressivo().toString(),form.progressivo.getText());
        assertEquals(d.getRealId().toString(),form.realIdDdT.getText());
        assertEquals(d.getRitiro(),form.ritiro.getText());
        assertEquals(d.getTipo(),form.tipo.getSelectedItem());
        assertEquals(d.getVostroOrdine(),form.vsordine.getText());
        assertEquals(d.getVostroOrdineDel(),form.vsdel.getText());
        
        JTable t=form.beni;
        
        assertEquals(b1.getCampionario(),t.getValueAt(0, DdTTableColumn.CAMP.toInt()));
        assertEquals(b1.getCodice(),t.getValueAt(0, DdTTableColumn.CODICE.toInt()));
        assertEquals(b1.getCommessa(),t.getValueAt(0, DdTTableColumn.COMMESSA.toInt()));
        assertEquals(b1.getDescrizione(),t.getValueAt(0, DdTTableColumn.DESCRIZIONE.toInt()));
        assertEquals(b1.getPiazzato(),t.getValueAt(0, DdTTableColumn.PIAZZ.toInt()));
        assertEquals(b1.getPrimoCapo(),t.getValueAt(0, DdTTableColumn.PRIMO_CAPO.toInt()));
        assertEquals(b1.getPrototipo(),t.getValueAt(0, DdTTableColumn.PROT.toInt()));
        assertEquals(b1.getQta(),t.getValueAt(0, DdTTableColumn.QTA.toInt()));
        assertEquals(b1.getId(),t.getValueAt(0, DdTTableColumn.RIGA_ID.toInt()));
        
        assertEquals(b2.getCampionario(),t.getValueAt(1, DdTTableColumn.CAMP.toInt()));
        assertEquals(b2.getCodice(),t.getValueAt(1, DdTTableColumn.CODICE.toInt()));
        assertEquals(b2.getCommessa(),t.getValueAt(1, DdTTableColumn.COMMESSA.toInt()));
        assertEquals(b2.getDescrizione(),t.getValueAt(1, DdTTableColumn.DESCRIZIONE.toInt()));
        assertEquals(b2.getPiazzato(),t.getValueAt(1, DdTTableColumn.PIAZZ.toInt()));
        assertEquals(b2.getPrimoCapo(),t.getValueAt(1, DdTTableColumn.PRIMO_CAPO.toInt()));
        assertEquals(b2.getPrototipo(),t.getValueAt(1, DdTTableColumn.PROT.toInt()));
        assertEquals(b2.getQta(),t.getValueAt(1, DdTTableColumn.QTA.toInt()));
        assertEquals(b2.getId(),t.getValueAt(1, DdTTableColumn.RIGA_ID.toInt()));


        verify(a);
    }

    @Test
    public void testGetValueAtColumn() {
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

        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        Azienda a1=new Azienda();
        a1.setNome("azienda2");
        expect(a.getAziendaPerNome(aziendaNome)).andReturn(a1);
        replay(a);
        form.aziendaManager=a;
        
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
        
        Long idB2 = 2L;
        String codiceB2 = "0002";
        String commessaB2 = "C0002";
        String descrizioneB2 = "Abito";
        Float qtaB2 = 25F;
        Boolean protB2 = Boolean.TRUE;
        Boolean piazzB2 = Boolean.TRUE;
        Boolean pcB2 = Boolean.FALSE;
        Boolean campB2 = Boolean.TRUE;
        
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
        t.setValueAt(qtaB1,0,DdTTableColumn.QTA.toInt());
        t.setValueAt(idB1,0,DdTTableColumn.RIGA_ID.toInt());
        
        t.setValueAt(campB2,1,DdTTableColumn.CAMP.toInt());
        t.setValueAt(codiceB2,1,DdTTableColumn.CODICE.toInt());
        t.setValueAt(commessaB2,1,DdTTableColumn.COMMESSA.toInt());
        t.setValueAt(descrizioneB2,1,DdTTableColumn.DESCRIZIONE.toInt());
        t.setValueAt(piazzB2,1,DdTTableColumn.PIAZZ.toInt());
        t.setValueAt(pcB2,1,DdTTableColumn.PRIMO_CAPO.toInt());
        t.setValueAt(protB2,1,DdTTableColumn.PROT.toInt());
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
        
        verify(a);
    }

    @Test
    public void testCaricaListaClienti() {
        Azienda a1= new Azienda();
        a1.setNome("azienda1");
        a1.setId(1L);
        Azienda a2= new Azienda();
        a2.setNome("azienda2");
        a2.setId(2L);
        Azienda a3= new Azienda();
        a3.setNome("azienda3");
        a3.setId(3L);

        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        expect(a.getAziendeNonPrincipali()).andReturn(Arrays.asList(a1,a2,a3)).times(5);
        replay(a);
        form.aziendaManager=a;

        form.caricaListaClienti();
        assertEquals(3,form.azienda.getItemCount());
        assertEquals(a1.getNome(),form.azienda.getItemAt(0));
        assertEquals(a2.getNome(),form.azienda.getItemAt(1));
        assertEquals(a3.getNome(),form.azienda.getItemAt(2));

        form.caricaListaClienti(2L);
        assertEquals(1,form.azienda.getSelectedIndex());
        form.caricaListaClienti(3L);
        assertEquals(2,form.azienda.getSelectedIndex());
        form.caricaListaClienti(1L);
        assertEquals(0,form.azienda.getSelectedIndex());
        form.caricaListaClienti(4L);
        assertEquals(0,form.azienda.getSelectedIndex());

        verify(a);
    }

    @Test
    public void testShowForm() {
    }
}
