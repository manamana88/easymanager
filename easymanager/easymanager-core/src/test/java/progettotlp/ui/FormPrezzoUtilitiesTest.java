/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import progettotlp.test.AnnualTest;
import java.text.ParseException;
import progettotlp.facilities.DateUtils;
import java.util.Date;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.ui.FormPrezzoUtilities.CostoType;
import javax.swing.JDialog;
import org.junit.Before;
import org.junit.Test;
import progettotlp.facilities.StringUtils;
import progettotlp.persistenza.LastSameBeneFatturatoInfos;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormPrezzoUtilitiesTest extends AnnualTest{
    FormPrezzoUtilities form;

    @Before
    public void init() throws Exception {
        form=new FormPrezzoUtilities(new JDialog());
        UiTestUtilities.initializeClass(form);
    }

    @Test
    public void testGetPrezzo() throws ValidationException {
        form.prezzo.setText(null);
        try{
            form.getPrezzo();
            fail();
        } catch (ValidationException e){}

        form.prezzo.setText("");
        try{
            form.getPrezzo();
            fail();
        } catch (ValidationException e){}

        form.prezzo.setText("-2");
        try{
            form.getPrezzo();
            fail();
        } catch (ValidationException e){}

        form.prezzo.setText("3,2");
        try{
            form.getPrezzo();
            fail();
        } catch (ValidationException e){}

        form.prezzo.setText("3.2");
        assertEquals(new Float(3.2),form.getPrezzo());
    }

    @Test
    public void testGetCostoType() throws ValidationException {
        form.unitario.setSelected(true);
        form.tempo.setSelected(false);
        assertEquals(CostoType.UNITARIO, form.getCostoType());
        form.unitario.setSelected(false);
        form.tempo.setSelected(true);
        assertEquals(CostoType.TEMPO, form.getCostoType());
        form.unitario.setSelected(true);
        form.tempo.setSelected(true);
        try{
            form.getCostoType();
            fail();
        } catch (ValidationException e){}
        form.unitario.setSelected(false);
        form.tempo.setSelected(false);
        try{
            form.getCostoType();
            fail();
        } catch (ValidationException e){}
    }

    @Test
    public void testOkAction() throws ValidationException {
        form.form.setVisible(true);
        form.prezzo.setText("-2");
        try{
            form.okAction();
            fail();
        }catch (ValidationException e){}
        assertTrue(form.form.isVisible());

        form.prezzo.setText("3");
        form.okAction();
        assertFalse(form.form.isVisible());
    }

    @Test
    public void testOkModificaPrezzoAction() throws ValidationException {
        form.form.setVisible(true);
        form.prezzo.setText("-2");
        try{
            form.okModificaPrezzoAction();
            fail();
        }catch (ValidationException e){}
        assertTrue(form.form.isVisible());

        form.prezzo.setText("3");
        form.okModificaPrezzoAction();
        assertFalse(form.form.isVisible());
    }

    @Test
    public void testTempoAction() {
        form.tempo.setSelected(true);
        form.unitario.setSelected(false);
        form.tempoAction();
        assertTrue(form.tempo.isSelected());
        assertFalse(form.unitario.isSelected());
        form.tempo.setSelected(false);
        form.unitario.setSelected(true);
        form.tempoAction();
        assertTrue(form.tempo.isSelected());
        assertFalse(form.unitario.isSelected());
    }

    @Test
    public void testUnitarioAction() {
        form.tempo.setSelected(true);
        form.unitario.setSelected(false);
        form.unitarioAction();
        assertFalse(form.tempo.isSelected());
        assertTrue(form.unitario.isSelected());
        form.tempo.setSelected(false);
        form.unitario.setSelected(true);
        form.unitarioAction();
        assertFalse(form.tempo.isSelected());
        assertTrue(form.unitario.isSelected());
    }

    @Test
    public void testCompilaForm_11args() throws ParseException {
        Integer ddtId=1;
        Date ddtDate=DateUtils.parseDate("01/01/2012");
        String ddtDescr = ddtId + " del " + DateUtils.formatDate(ddtDate);

        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Boolean iaB1 = Boolean.TRUE;
        Float prezzoB1 = 2F;
        Float totB1=prezzoB1*qtaB1;
        CostoType costoType=CostoType.TEMPO;

        form.compilaForm(ddtDescr, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, costoType, null,null);

        assertEquals(ddtDescr,form.labelDdT.getText());
        assertEquals(convertBooleanToSiNo(campB1), form.labelCamp.getText());
        assertEquals(qtaB1.toString(), form.labelCapi.getText());
        assertEquals(codiceB1, form.labelCodice.getText());
        assertEquals(commessaB1, form.labelCommessa.getText());
        assertEquals(descrizioneB1, form.labelDescrizione.getText());
        assertEquals(convertBooleanToSiNo(pcB1), form.labelPC.getText());
        assertEquals(convertBooleanToSiNo(piazzB1), form.labelPiazz.getText());
        assertEquals(convertBooleanToSiNo(protB1), form.labelProto.getText());
        assertEquals(convertBooleanToSiNo(iaB1), form.labelIA.getText());
        assertEquals(FormPrezzoUtilitiesInterface.MAI_FATTURATO, form.labelFatturaRef.getText());
        assertEquals(FormPrezzoUtilitiesInterface.ND, form.labelFatturaOldPrice.getText());
        assertEquals(FormPrezzoUtilitiesInterface.ND, form.labelFatturaOldPriceType.getText());
        assertTrue(form.prezzo.getText().isEmpty());
        assertFalse(form.unitario.isSelected());
        assertTrue(form.tempo.isSelected());

        Bene b1 = new Bene(codiceB1, commessaB1, descrizioneB1, qtaB1, prezzoB1,totB1, protB1, campB1, protB1, piazzB1,iaB1);
        LastSameBeneFatturatoInfos last=new LastSameBeneFatturatoInfos(1, ddtDate, b1);
        form.compilaForm(ddtDescr, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, costoType, prezzoB1,last);
        assertEquals(StringUtils.formatNumber(prezzoB1),form.prezzo.getText());
        assertEquals("1 del "+DateUtils.formatDate(ddtDate), form.labelFatturaRef.getText());
        assertEquals(StringUtils.formatNumber(prezzoB1), form.labelFatturaOldPrice.getText());
        assertEquals(CostoType.UNITARIO.toString(), form.labelFatturaOldPriceType.getText());

        last.getBene().setPrezzo(null);
        costoType=CostoType.UNITARIO;
        form.compilaForm(ddtDescr, codiceB1, commessaB1, descrizioneB1, protB1, piazzB1, pcB1, campB1, iaB1,qtaB1, costoType, prezzoB1,last);
        assertTrue(form.unitario.isSelected());
        assertFalse(form.tempo.isSelected());
        assertEquals("1 del "+DateUtils.formatDate(ddtDate), form.labelFatturaRef.getText());
        assertEquals(StringUtils.formatNumber(totB1/qtaB1), form.labelFatturaOldPrice.getText());
        assertEquals(CostoType.TEMPO.toString(), form.labelFatturaOldPriceType.getText());
    }

    @Test
    public void testCompilaForm_DdT_Bene() throws ParseException {
        Integer ddtId=1;
        Date ddtDate=DateUtils.parseDate("01/01/2012");
        DdT d=new DdT();
        d.setId(ddtId);
        d.setData(ddtDate);

        Long idB1 = 1L;
        String codiceB1 = "0001";
        String commessaB1 = "C0001";
        String descrizioneB1 = "Abito";
        Float qtaB1 = 15F;
        Boolean protB1 = Boolean.TRUE;
        Boolean piazzB1 = Boolean.TRUE;
        Boolean pcB1 = Boolean.TRUE;
        Boolean campB1 = Boolean.TRUE;
        Float prezzoB1 = 2F;
        Float totB1 = 30F;
        
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

        LastSameBeneFatturatoInfos last=new LastSameBeneFatturatoInfos(1, ddtDate, b1);
        form.compilaForm(d, b1,last);

        assertEquals(ddtId+" del "+DateUtils.formatDate(ddtDate),form.labelDdT.getText());
        assertEquals(convertBooleanToSiNo(campB1), form.labelCamp.getText());
        assertEquals(qtaB1.toString(), form.labelCapi.getText());
        assertEquals(codiceB1, form.labelCodice.getText());
        assertEquals(commessaB1, form.labelCommessa.getText());
        assertEquals(descrizioneB1, form.labelDescrizione.getText());
        assertEquals(convertBooleanToSiNo(pcB1), form.labelPC.getText());
        assertEquals(convertBooleanToSiNo(piazzB1), form.labelPiazz.getText());
        assertEquals(convertBooleanToSiNo(protB1), form.labelProto.getText());

        assertEquals("1 del "+DateUtils.formatDate(ddtDate), form.labelFatturaRef.getText());
        assertTrue(form.labelFatturaOldPrice.getText().isEmpty());
        assertTrue(form.labelFatturaOldPriceType.getText().isEmpty());
        
        assertTrue(form.prezzo.getText().isEmpty());
        assertTrue(form.unitario.isSelected());
        assertFalse(form.tempo.isSelected());

        b1.setTot(totB1);
        form.compilaForm(d, b1,last);
        assertEquals("1 del "+DateUtils.formatDate(ddtDate), form.labelFatturaRef.getText());
        assertEquals(StringUtils.formatNumber(totB1/qtaB1),form.labelFatturaOldPrice.getText());
        assertEquals(CostoType.TEMPO.toString(),form.labelFatturaOldPriceType.getText());

        assertEquals(StringUtils.formatNumber(totB1),form.prezzo.getText());
        assertFalse(form.unitario.isSelected());
        assertTrue(form.tempo.isSelected());

        b1.setPrezzo(prezzoB1);
        form.compilaForm(d, b1,last);
        assertEquals("1 del "+DateUtils.formatDate(ddtDate), form.labelFatturaRef.getText());
        assertEquals(StringUtils.formatNumber(prezzoB1), form.labelFatturaOldPrice.getText());
        assertEquals(CostoType.UNITARIO.toString(), form.labelFatturaOldPriceType.getText());

        assertEquals(StringUtils.formatNumber(prezzoB1),form.prezzo.getText());
        assertTrue(form.unitario.isSelected());
        assertFalse(form.tempo.isSelected());
    }

    @Test
    public void testShowForm() {
    }

    private String convertBooleanToSiNo(Boolean b){
        return b?"Si":"No";
    }

}