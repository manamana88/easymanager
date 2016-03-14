/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import javax.swing.JTextArea;
import progettotlp.classes.Bene;
import java.util.ArrayList;
import java.util.Date;
import progettotlp.classes.DdT;
import javax.swing.JDialog;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.DateUtils;
import static progettotlp.facilities.Utility.*; 

/**
 *
 * @author vincenzo
 */
public class FormDdTUtilities extends AbstractFormUtilities{
    
    public static enum MezzoType{
        CESSIONARIO,CEDENTE,VETTORE;
        
        @Override
        public String toString(){
            switch (this){
                case CESSIONARIO:
                    return "Cessionario";
                case CEDENTE:
                    return "Cedente";
                case VETTORE:
                    return "Vettore";
                default:
                    return null;
            }
        }
    }
    public static enum PortoType{
        FRANCO,DESTINAZIONE;
        
        @Override
        public String toString(){
            switch (this){
                case FRANCO:
                    return "Franco";
                case DESTINAZIONE:
                    return "Destinazione";
                default:
                    return "";
            }
        }
    }
    public static enum Tipo{
        CONTO,SALDO;
        
        @Override
        public String toString(){
            switch (this){
                case CONTO:
                    return "in conto";
                case SALDO:
                    return "a saldo";
                default:
                    return "";
            }
        }
    }
    public static enum FormDdTType{AGGIUNGI,CANCELLA,MODIFICA,VISUALIZZA};
    public static enum DdTTableColumn{
        CODICE,COMMESSA,DESCRIZIONE,QTA,PROT,CAMP,PRIMO_CAPO,PIAZZ,INT_ADE,RIGA_ID;
        
        public int toInt(){
            switch (this){
                case CODICE:
                    return 0;
                case COMMESSA:
                    return 1;
                case DESCRIZIONE:
                    return 2;
                case QTA:
                    return 3;
                case PROT:
                    return 4;
                case CAMP:
                    return 5;
                case PRIMO_CAPO:
                    return 6;
                case PIAZZ:
                    return 7;
                case INT_ADE:
                    return 8;
                case RIGA_ID:
                    return 9;
                default :
                    return -1;
            }
        }
    }
    
    protected JButton DdTModifica;
    protected JButton DdTSalva;
    protected JButton DdTAnnulla;
    protected JButton DdTFatto;
    protected JTable beni;
    protected JTextField data;
    protected JTextField numero;
    protected JComboBox azienda;
    protected JComboBox tipo;
    protected JComboBox mezzo;
    protected JComboBox porto;
    protected JTextField vsordine;
    protected JTextField causale;
    protected JTextArea destinazione;
    protected JTextField aspetto;
    protected JTextField vsdel;
    protected JTextField colli;
    protected JTextField peso;
    protected JTextField ritiro;
    protected JTextField progressivo;
    protected JTextField annotazioni;
    protected JTextField realIdDdT;
    
    
    protected AziendaManager aziendaManager;
    protected DdTManager ddtManager;

    public FormDdTUtilities(JDialog formDdT, AziendaManager aziendaManager, DdTManager ddtManager) {
        this.aziendaManager=aziendaManager;
        this.ddtManager=ddtManager;
        init(formDdT);
    }
    /**
     * Handler per l'evento "salva DdT": crea la lista di beni con i valori
     * inseriti dall'utente nella tabella beni ed esegue le operazioni sul database.
     * @param evt 
     */
    public String salvaDdT() throws ValidationException, GenericExceptionToPrint, YetExistException{
        DdT daSalvare=caricaDdTDaForm();
        if (!ddtManager.existsDdTById(daSalvare.getId())){
            try{
                ddtManager.registraDdT(daSalvare);
                return numero.getText()+" del "+data.getText();
            } catch (Exception e){
                throw new GenericExceptionToPrint("Errore", "Errore nel salvataggio del DdT",e);
            }
        }else {
            throw new YetExistException("Errore", "Esiste già un DdT con lo stesso ID");
        }
    }
    
    public void modificaDdT() throws ValidationException, GenericExceptionToPrint{
        DdT daSalvare=caricaDdTDaForm();
        try{
            ddtManager.modificaDdT(daSalvare);
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore", "Errore modifica DdT",e);
        } 
    }
    
    /**
     * Metodo che compila il form relativo ai DdT con i dati del DdT passato
     * come parametro.
     * @param d 
     */
    public void compilaFormDdT(DdT d){
        data.setText(DateUtils.formatDate(d.getData()));
        numero.setText(Integer.toString(d.getId()));
        caricaListaClienti(d.getCliente().getId());
        popolaListaBeni(d);
        tipo.setSelectedItem(d.getTipo());
        vsordine.setText(d.getVostroOrdine());
        causale.setText(d.getCausale());
        aspetto.setText(d.getAspettoEsteriore());
        destinazione.setText(d.getDestinazione());
        vsdel.setText(d.getVostroOrdineDel());
        setWithNumber(colli, d.getColli());
        setWithNumber(peso, d.getPeso());
        mezzo.setSelectedItem(d.getMezzo());
        porto.setSelectedItem(d.getPorto());
        ritiro.setText(d.getRitiro());
        Integer progressivoVar = d.getProgressivo();
        progressivo.setText(progressivoVar==null?null:progressivoVar.toString());
        annotazioni.setText(d.getAnnotazioni());
        realIdDdT.setText(d.getRealId().toString());
    }
    
    public <T> T getValueAtColumn(int row,DdTTableColumn tableColumn){
        return (T)beni.getValueAt(row, tableColumn.toInt()); 
    }

    protected boolean checkRow(JTable beni, int i) throws ValidationException {
        if (isRigaVuota(beni,i))
            return false;
        String codice = getValueAtColumn(i, DdTTableColumn.CODICE);
        if (codice==null || codice.trim().isEmpty())
            throw new ValidationException("Dati errati", "Codice nullo");
        String descrizione = getValueAtColumn(i, DdTTableColumn.DESCRIZIONE);
        if (descrizione==null || descrizione.trim().isEmpty())
            throw new ValidationException("Dati errati", "Descrizione vuota");
        Integer qta=getValueAtColumn(i, DdTTableColumn.QTA);
        if (qta==null || qta<=0)
            throw new ValidationException("Dati errati", "Quantità vuota");
        return true;
    }

    public DdT caricaDdTDaForm() throws ValidationException{
        if (beni.isEditing()){
            beni.getCellEditor().stopCellEditing();
        }
        List <Bene> x=new ArrayList();
        for (int i=0;i<beni.getRowCount();i++){
            if (checkRow(beni,i)){
                Bene bene=new Bene(
                        convertNullToString(getValueAtColumn(i, DdTTableColumn.CODICE)),
                        convertNullToString(getValueAtColumn(i, DdTTableColumn.COMMESSA)),
                        convertNullToString(getValueAtColumn(i, DdTTableColumn.DESCRIZIONE)),
                        (Integer)getValueAtColumn(i, DdTTableColumn.QTA),
                        convertNullToBoolean(getValueAtColumn(i, DdTTableColumn.PROT)),
                        convertNullToBoolean(getValueAtColumn(i, DdTTableColumn.CAMP)),
                        convertNullToBoolean(getValueAtColumn(i, DdTTableColumn.PRIMO_CAPO)),
                        convertNullToBoolean(getValueAtColumn(i, DdTTableColumn.PIAZZ)),
                        convertNullToBoolean(getValueAtColumn(i, DdTTableColumn.INT_ADE)));
                Long value=(Long)getValueAtColumn(i, DdTTableColumn.RIGA_ID);
                if (value!=null && value>0){
                    bene.setId(value);
                }
                x.add(bene);
            }
        }
        if (x.isEmpty()){
            throw new ValidationException("Dati errati", "Il ddt non contiene beni");
        }
        
        Integer ddtId=parseIntegerValue(numero,true);
        DdT res=new DdT(x,parseDateValue(data, true),ddtId,aziendaManager.getAziendaPerNome((String)azienda.getSelectedItem()));
        String realIdDdTText = realIdDdT.getText();
        if (realIdDdTText != null && !realIdDdTText.trim().isEmpty()){
            res.setRealId(Long.parseLong(realIdDdTText));
        }
        res.setTipo(convertNullToString(tipo.getSelectedItem()));
        res.setVostroOrdine(vsordine.getText());
        res.setCausale(causale.getText());
        res.setAspettoEsteriore(aspetto.getText());
        res.setVostroOrdineDel(vsdel.getText());
        res.setColli(parseIntegerValue(colli));
        res.setPeso(parseDoubleValue(peso));
        res.setMezzo(convertNullToString(mezzo.getSelectedItem()));
        res.setPorto(convertNullToString(porto.getSelectedItem()));
        res.setRitiro(ritiro.getText());
        res.setProgressivo(parseIntegerValue(progressivo));
        res.setAnnotazioni(annotazioni.getText());
        res.setDestinazione(destinazione.getText());
        return res;
    
    }
    
    /**
     * Metodo che popola la lista dei beni attivi presso un DdT.
     * @param d 
     */
    private void popolaListaBeni(DdT d) {
        List <Bene> lst = d.getBeni();
        for (int i=0;i<lst.size()&&i<10;i++){
            beni.getModel().setValueAt(lst.get(i).getCodice(), i, 0);
            beni.getModel().setValueAt(lst.get(i).getCommessa(), i, 1);
            beni.getModel().setValueAt(lst.get(i).getDescrizione(), i, 2);
            beni.getModel().setValueAt(lst.get(i).getQta(), i, 3);
            beni.getModel().setValueAt(lst.get(i).getPrototipo(), i, 4);
            beni.getModel().setValueAt(lst.get(i).getCampionario(), i, 5);
            beni.getModel().setValueAt(lst.get(i).getPrimoCapo(), i, 6);
            beni.getModel().setValueAt(lst.get(i).getPiazzato(), i, 7);
            beni.getModel().setValueAt(lst.get(i).getInteramenteAdesivato(), i, 8);
            beni.getModel().setValueAt(lst.get(i).getId(), i, 9);
        }
    }
    
    
    /**
     * Metodo che resetta il form relativo ai DdT
     */
    private void resetFormDdT(){
        data.setText(DateUtils.formatDate(new Date()));
        Integer lastDdT = ddtManager.getLastDdT();
        if (lastDdT==null){
            lastDdT=0;
        }
        numero.setText(Integer.toString(lastDdT+1));
        for (int i=0;i<beni.getRowCount();i++)
            for (int j=0;j<beni.getColumnModel().getColumnCount();j++)
                beni.getModel().setValueAt(null, i, j);
        azienda.removeAllItems();
        caricaListaClienti();
        tipo.setSelectedItem("");
        vsordine.setText("");
        causale.setText("");
        aspetto.setText("");
        vsdel.setText("");
        colli.setText("");
        peso.setText("");
        mezzo.setSelectedItem("Cessionario");
        porto.setSelectedItem("");
        ritiro.setText("");
        progressivo.setText("");
        annotazioni.setText("");
        destinazione.setText("");
        realIdDdT.setText(null);
    }
    
    /**
     * Metodo che abilita la modifica del form relativo ai DdT.
     */
    private void abilitaFormDdT(){
        data.setEditable(true);
        numero.setEditable(true);
        azienda.setEnabled(true);
        beni.setEnabled(true);
        tipo.setEditable(true);
        vsordine.setEditable(true);
        causale.setEditable(true);
        aspetto.setEditable(true);
        vsdel.setEditable(true);
        colli.setEditable(true);
        peso.setEditable(true);
        mezzo.setEditable(true);
        porto.setEditable(true);
        ritiro.setEditable(true);
        progressivo.setEditable(true);
        annotazioni.setEditable(true);
        destinazione.setEditable(true);
    }
    
    /**
     * Metodo che disabilita la modifica del form relativo ai DdT.
     */
    private void disabilitaFormDdT(){
        data.setEditable(false);
        numero.setEditable(false);
        azienda.setEditable(false);
        beni.setEnabled(false);
        tipo.setEditable(false);
        vsordine.setEditable(false);
        causale.setEditable(false);
        aspetto.setEditable(false);
        vsdel.setEditable(false);
        colli.setEditable(false);
        peso.setEditable(false);
        mezzo.setEditable(false);
        porto.setEditable(false);
        ritiro.setEditable(false);
        progressivo.setEditable(false);
        annotazioni.setEditable(false);
        destinazione.setEditable(false);
    }
    
    protected void caricaListaClienti(){
        List<Azienda> aziendeNonPrincipali = aziendaManager.getAziendeNonPrincipali();
        for (Azienda a : aziendeNonPrincipali){
            azienda.addItem(a.getNome());
        }
    }
    
    public void caricaListaClienti(Long id){
        List<Azienda> tutte = aziendaManager.getAziendeNonPrincipali();
        azienda.removeAllItems();
        for (int i=0;i<tutte.size();i++){
            Azienda temp=tutte.get(i);
            azienda.addItem(temp.getNome());
            if (temp.getId().equals(id)){
                azienda.setSelectedIndex(i);
            }
        }
    }
    
    public void showForm(FormDdTType formType,String title){
        form.setSize(800, 400);
        switch (formType) {
            case AGGIUNGI:
                abilitaFormDdT();
                resetFormDdT();
                DdTAnnulla.setVisible(true);
                DdTFatto.setVisible(false);
                DdTModifica.setVisible(false);
                DdTSalva.setVisible(true);
                break;
            case MODIFICA:
                abilitaFormDdT();
                DdTAnnulla.setVisible(true);
                DdTFatto.setVisible(false);
                DdTModifica.setVisible(true);
                DdTSalva.setVisible(false);
                break;
            case VISUALIZZA:
                DdTAnnulla.setVisible(false);
                DdTFatto.setVisible(true);
                DdTModifica.setVisible(false);
                DdTSalva.setVisible(false);
                disabilitaFormDdT();
                break;
        }
        ((JDialog) form).setTitle(title);
        form.setVisible(true);
    }
    
}
