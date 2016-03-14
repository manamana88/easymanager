/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.StringUtils;
import progettotlp.persistenza.LastSameBeneFatturatoInfos;

/**
 *
 * @author vincenzo
 */
public class FormPrezzoUtilities extends AbstractFormUtilities implements FormPrezzoUtilitiesInterface {
    public enum FormPrezzoType{MODIFICA_RIGA,INSERISCI_PREZZO};
    public enum CostoType{TEMPO,UNITARIO};
    
    protected JLabel labelDdT;
    protected JLabel labelCapi;
    protected JLabel labelCodice;
    protected JLabel labelCommessa;
    protected JLabel labelDescrizione;
    protected JLabel labelProto;
    protected JLabel labelPiazz;
    protected JLabel labelPC;
    protected JLabel labelCamp;
    protected JLabel labelIA;
    protected JLabel labelFatturaRef;
    protected JLabel labelFatturaOldPrice;
    protected JLabel labelFatturaOldPriceType;
    protected JButton okModificaPrezzo;
    protected JButton okPrezzo;
    protected JTextField prezzo;
    protected JRadioButton unitario;
    protected JRadioButton tempo;

    public FormPrezzoUtilities(JDialog formPrezzo) {
        init(formPrezzo);
    }
    
    public Float getPrezzo() throws ValidationException{
        try{
            Float parseFloat = Float.parseFloat(prezzo.getText());
            if (parseFloat<0){
                throw new NumberFormatException();
            }
            return parseFloat;
        }catch(NumberFormatException e){
            throw new ValidationException("Dati errati", "Prezzo non valido.");
        }
        
    }

    public CostoType getCostoType() throws ValidationException{
        boolean isUnitario = unitario.isSelected();
        boolean isTempo = tempo.isSelected();
        if (isUnitario == isTempo){
            throw new ValidationException("Dati errati", "Dati sul tipo di prezzo inconsistenti");
        } else if (!isUnitario && isTempo){
            return CostoType.TEMPO;
        } else {
            return CostoType.UNITARIO;
        } 
    }
    
    public void okAction() throws ValidationException{
        okModificaPrezzoAction();
    }
    
    public Float okModificaPrezzoAction() throws ValidationException{
        Float f=getPrezzo();
        hideForm();
        return f;
        
    }

    public void tempoAction(){
        unitario.setSelected(false);
        tempo.setSelected(true);
    }
    
    public void unitarioAction(){
        unitario.setSelected(true);
        tempo.setSelected(false);
    }

    public void compilaForm(String ddt, String codice, String commessa, String descrizione,
            boolean proto, boolean piazz, boolean pc, boolean camp, boolean intAde, int capi, CostoType costoType, Float prezzoValue,
            LastSameBeneFatturatoInfos infos){
        labelDdT.setText(ddt);
        labelCodice.setText(codice);
        labelCommessa.setText(commessa);
        labelDescrizione.setText(descrizione);
        if (proto) labelProto.setText("Si"); else labelProto.setText("No");
        if (piazz) labelPiazz.setText("Si"); else labelPiazz.setText("No");
        if (pc) labelPC.setText("Si"); else labelPC.setText("No");
        if (camp) labelCamp.setText("Si"); else labelCamp.setText("No");
        if (intAde) labelIA.setText("Si"); else labelIA.setText("No");
        labelCapi.setText(capi+"");
        if (infos!=null){
            labelFatturaRef.setForeground(Color.RED);
            labelFatturaOldPrice.setForeground(Color.RED);
            labelFatturaOldPriceType.setForeground(Color.RED);
            labelFatturaRef.setText(infos.getFatturaRef());
            Bene bene = infos.getBene();
            String oldPrice;
            String oldPriceType;
            if (bene.getTot()!=null){
                if (bene.getPrezzo()==null){
                    oldPrice=StringUtils.formatNumber(bene.getTot()/bene.getQta());
                    oldPriceType=costoType.TEMPO.toString();
                } else {
                    oldPrice=StringUtils.formatNumber(bene.getPrezzo());
                    oldPriceType=costoType.UNITARIO.toString();
                }
            } else{
                oldPrice="";
                oldPriceType="";
            }
            labelFatturaOldPrice.setText(oldPrice);
            labelFatturaOldPriceType.setText(oldPriceType);
        }else{
            labelFatturaRef.setForeground(Color.BLACK);
            labelFatturaOldPrice.setForeground(Color.BLACK);
            labelFatturaOldPriceType.setForeground(Color.BLACK);
            labelFatturaRef.setText(MAI_FATTURATO);
            labelFatturaOldPrice.setText(ND);
            labelFatturaOldPriceType.setText(ND);
        }
        if (CostoType.TEMPO.equals(costoType)){
            unitario.setSelected(false);
            tempo.setSelected(true);
        } else {
            unitario.setSelected(true);
            tempo.setSelected(false);
        }
        String value=null;
        if (prezzoValue!=null){
            value = StringUtils.formatNumber(prezzoValue);
        }
        prezzo.setText(value);
    }

    public void compilaForm(DdT d, Bene b,LastSameBeneFatturatoInfos infos){
        CostoType costoType;
        Float prezzoValue;
        if (b.getPrezzo()==null){
            final Float tot = b.getTot();
            costoType=(tot==null)?CostoType.UNITARIO:CostoType.TEMPO;
            prezzoValue=tot;
        } else {
            costoType=CostoType.UNITARIO;
            prezzoValue=b.getPrezzo();
        }
        compilaForm(d.getId()+" del "+DateUtils.formatDate(d.getData()), b.getCodice(), b.getCommessa(), b.getDescrizione(), b.getPrototipo(),
                b.getPiazzato(), b.getPrimoCapo(), b.getCampionario(), b.getInteramenteAdesivato(), b.getQta(), costoType, prezzoValue,infos);
    }
    
    public void showForm(FormPrezzoType formPrezzoType){
        switch (formPrezzoType){
            case MODIFICA_RIGA:
                okModificaPrezzo.setVisible(true);
                okPrezzo.setVisible(false);
                break;
            case INSERISCI_PREZZO:
                okModificaPrezzo.setVisible(false);
                okPrezzo.setVisible(true);
        }
        form.setVisible(true);
    }

}
