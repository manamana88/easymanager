package progettotlp.ui;

import java.text.ParseException;
import java.util.Date;

import progettotlp.facilities.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.FatturaManager;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.Dialogs;
import progettotlp.facilities.NumberUtils;
import progettotlp.facilities.StringUtils;
import progettotlp.facilities.Utility;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.persistenza.LastSameBeneFatturatoInfos;
import progettotlp.ui.FormPrezzoUtilities.CostoType;
import progettotlp.ui.FormPrezzoUtilities.FormPrezzoType;
import static progettotlp.facilities.Utility.*;

/**
 *
 * @author vincenzo
 */
public class FormFatturaUtilities extends AbstractFormUtilities{
    
    public static enum FormFatturaType{AGGIUNGI,MODIFICA, VISUALIZZA};
    public static enum TableColumn{
        RIF_DDT,CODICE,COMMESSA,DESCRIZIONE,PROT,CAMP,PRIMO_CAPO,PIAZZ,INT_ADE,QTA,COSTO_UNI,TOT,TOT_TEMPO,RIGA_ID;
        
        public int toInt(){
            switch (this){
                case RIF_DDT:
                    return 0;
                case CODICE:
                    return 1;
                case COMMESSA:
                    return 2;
                case DESCRIZIONE:
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
                case QTA:
                    return 9;
                case COSTO_UNI:
                    return 10;
                case TOT:
                    return 11;
                case TOT_TEMPO:
                    return 12;
                case RIGA_ID:
                    return 13;
                default :
                    return -1;
            }
        }
    }
    
    protected static Logger logger = LoggerFactory.getLogger(FormFatturaUtilities.class);
    
    protected JComboBox validita;
    private boolean isValiditaRegistered=false;
    private Object validitaLastSelectedObject;
    protected JTable tabellaFattura;
    protected JTextField numFatt; 
    protected JTextField cliente;
    protected JTextField dataEmissione;
    protected JTextField dataScadenza;
    protected JTextField numCapiTot;
    protected JTextField netto;
    protected JTextField ivaPerc;
    protected JTextField totIva;
    protected JTextField totale;
    protected JTextField realIdFattura;
    protected JButton fatturaModificaRiga;
    protected JButton fatturaFatto;
    protected JButton fatturaSalva;
    protected JButton fatturaAnnulla;
    protected JButton fatturaModifica;
    
    protected AziendaManager aziendaManager;
    protected DdTManager ddtManager;
    protected FatturaManager fatturaManager;

    protected FormPrezzoUtilitiesInterface formPrezzoUtilitiesInterface;
    
    public FormFatturaUtilities(JDialog formFattura, FormPrezzoUtilitiesInterface formPrezzoUtilitiesInterface, AziendaManager aziendaManager, DdTManager ddtManager, FatturaManager fatturaManager) {
        this.aziendaManager=aziendaManager;
        this.ddtManager=ddtManager;
        this.fatturaManager=fatturaManager;
        this.formPrezzoUtilitiesInterface=formPrezzoUtilitiesInterface;
        init(formFattura);
        if (validita!=null){
            validita.addItem("30");
            validita.addItem("60");
            validita.addItem("90");
            validita.setSelectedIndex(1);
        }
    }
    
    
    public void resetFormFattura(){
        numFatt.setText("");
        cliente.setText("");
        dataEmissione.setText("");
        validita.setSelectedIndex(1);
        numCapiTot.setText("");
        netto.setText("");
        ivaPerc.setText("");
        totIva.setText("");
        totale.setText("");
        dataScadenza.setText("");
        realIdFattura.setText(null);
        removeRows();
    }

    public void compilaFormFattura(Fattura f) throws ValidationException{
        resetFormFattura();
        realIdFattura.setText(f.getRealId().toString());
        List<DdT> lst=f.getDdt();
        int q=0;
        q = compilaTabellaFattura(lst, false).totCapi;
        if(!lst.isEmpty() && q>0){
            numCapiTot.setText(q+"");
            netto.setText(StringUtils.formatNumber(f.getNetto()));
            ivaPerc.setText(f.getIvaPerc().toString());
            totIva.setText(StringUtils.formatNumber(f.getIva()));
            totale.setText(StringUtils.formatNumber(f.getTotale()));
            numFatt.setText(f.getId().toString());
            cliente.setText(f.getCliente().getNome());
            dataEmissione.setText(DateUtils.formatDate(f.getEmissione()));
            dataScadenza.setText(DateUtils.formatDate(f.getScadenza()));
            selectValidita(f);
        }
    }

    protected void selectValidita(Fattura f) throws ValidationException {
        Integer validitaValue = DateUtils.getTimeFrame(f.getEmissione(),f.getScadenza());
        boolean existItem = false;
        for (int i = 0; i < validita.getItemCount(); i++) {
            logger.info("validita.getItemAt("+i+") = ["+validita.getItemAt(i)+"]");
            if (validita.getItemAt(i).equals(validitaValue.toString())) {
                existItem = true;
                validita.setSelectedIndex(i);
            }
        }
        if (!existItem) {
            throw new ValidationException("Dati Errati", "Scadenza fattura non valida");
        }
    }


    
    public void compilaFormFatturaAggiungi(Azienda aziendaCliente, Integer mese) throws ValidationException{
        resetFormFattura();
        List<DdT> lst=ddtManager.getAllDdT(aziendaCliente, mese,true,false);
        Result res=compilaTabellaFattura(lst, true);
        if(!lst.isEmpty()){
            int q = res.totCapi;
            float nt = res.totFattura;
            numCapiTot.setText(Integer.toString(q));
            netto.setText(StringUtils.formatNumber(nt));
            Float ivaDefault = Float.parseFloat(ConfigurationManager.getProperty(Property.IVA_DEFAULT));
            float iii=convertNullToBoolean(aziendaCliente.isTassabile())?(float) (nt*ivaDefault/100):0F;
            ivaPerc.setText(ivaDefault.toString());
            totIva.setText(StringUtils.formatNumber(iii));
            float ttt=nt+iii;
            totale.setText(StringUtils.formatNumber(ttt));

            numFatt.setText(Integer.toString(fatturaManager.getLastFattura()+1));
            cliente.setText(aziendaCliente.getNome());
            Date fatturaDay = DateUtils.getFatturaDay(lst.get(0).getData());
            dataEmissione.setText(DateUtils.formatDate(fatturaDay));
            Date scadenzaDay = DateUtils.calcolaScadenza(fatturaDay, Integer.parseInt((String) validita.getSelectedItem()));
            dataScadenza.setText(DateUtils.formatDate(scadenzaDay));
        }
    }
    
    private int getSelectedRow() throws NoSelectedRow{
        int selectedRow = tabellaFattura.getSelectedRow();
        if (selectedRow<0){
            throw new NoSelectedRow("Errore","Nessuna riga selezionata");
        }
        return selectedRow;
    }
    
    
    public <T> T getValueAtColumn(int row,TableColumn tableColumn){
        return (T)tabellaFattura.getValueAt(row, tableColumn.toInt()); 
    }
    public void setValueAtColumn(Object newValue, int row, TableColumn tableColumn){
        tabellaFattura.setValueAt(newValue, row, tableColumn.toInt());
    }
    public <T> T getSelectedValueAtColumn(TableColumn tableColumn) throws NoSelectedRow{
        return (T)tabellaFattura.getValueAt(getSelectedRow(), tableColumn.toInt()); 
    }
    public void setSelectedValueAtColumn(Object newValue, TableColumn tableColumn) throws NoSelectedRow{
        tabellaFattura.setValueAt(newValue, getSelectedRow(), tableColumn.toInt());
    }
    
    public Fattura salvaFattura() throws YetExistException, GenericExceptionToPrint, ValidationException{
        Fattura daSalvare = caricaFatturaDaForm();
        if (!fatturaManager.existsFattura(daSalvare.getId())){
            try{
                fatturaManager.registraFattura(daSalvare);
                return daSalvare;
            } catch (Exception e){
                throw new GenericExceptionToPrint("Errore", "Errore nella registrazione della fattura n° "+numFatt.getText()+".",e);
            }
        } else {
            throw new YetExistException("Errore","Esiste già una fattura con lo stesso numero.");
        }
    }
    
    public void modificaRiga() throws NoSelectedRow, ValidationException{
        String rifDdT=getSelectedValueAtColumn(TableColumn.RIF_DDT);
        String codice=getSelectedValueAtColumn(TableColumn.CODICE);
        String commessa = getSelectedValueAtColumn(TableColumn.COMMESSA);
        String descrizione=getSelectedValueAtColumn(TableColumn.DESCRIZIONE); 
        Boolean prot=getSelectedValueAtColumn(TableColumn.PROT);
        Boolean piazz=getSelectedValueAtColumn(TableColumn.PIAZZ); 
        Boolean pc=getSelectedValueAtColumn(TableColumn.PRIMO_CAPO);
        Boolean camp=getSelectedValueAtColumn(TableColumn.CAMP);
        Boolean intAde=getSelectedValueAtColumn(TableColumn.INT_ADE);
        Integer qta=getSelectedValueAtColumn(TableColumn.QTA);
        Float costoUni=getSelectedValueAtColumn(TableColumn.COSTO_UNI);
        CostoType costoType=null;
        TableColumn column=null;
        Float perPrezzoForm;
        if (costoUni==null){
            perPrezzoForm=getSelectedValueAtColumn(TableColumn.TOT_TEMPO);
            costoType=CostoType.TEMPO;
            column=TableColumn.TOT_TEMPO;
        } else {
            perPrezzoForm=getSelectedValueAtColumn(TableColumn.COSTO_UNI);
            costoType=CostoType.UNITARIO;
            column=TableColumn.TOT;
        }
        Float vecchioNetto=convertStringToFloat(netto.getText());
        Float daSottrarre = getSelectedValueAtColumn(column);
        Bene b=new Bene(codice, commessa, descrizione, qta, prot, camp, pc, piazz,intAde);
        LastSameBeneFatturatoInfos last = fatturaManager.getLastSameBeneFatturatoInfos(b);
        formPrezzoUtilitiesInterface.compilaForm(rifDdT, codice, commessa, descrizione, prot, piazz, pc, camp, intAde, qta, costoType, perPrezzoForm,last);
        formPrezzoUtilitiesInterface.showForm(FormPrezzoType.MODIFICA_RIGA);
        
        
        CostoType newCostoType = formPrezzoUtilitiesInterface.getCostoType();
        Float newPrezzo = null;
        Float newTotTempo=null;
        Float newTot=null;
        Float nuovoNetto=null;
        if (CostoType.TEMPO.equals(newCostoType)){
            newTotTempo=formPrezzoUtilitiesInterface.getPrezzo();
            nuovoNetto=vecchioNetto-daSottrarre+newTotTempo;
        } else {
            newPrezzo=formPrezzoUtilitiesInterface.getPrezzo();
            newTot=newPrezzo*qta;
            nuovoNetto=vecchioNetto-daSottrarre+newTot;
        }
        setSelectedValueAtColumn(newPrezzo, TableColumn.COSTO_UNI);
        setSelectedValueAtColumn(newTot, TableColumn.TOT);
        setSelectedValueAtColumn(newTotTempo, TableColumn.TOT_TEMPO);

        String oldIva = totIva.getText();
        boolean isTassabile=  !(oldIva.isEmpty() || new Float(0).equals(Float.parseFloat(oldIva)));
        Float nuovaIva= isTassabile?    nuovoNetto*convertStringToFloat(ivaPerc.getText())/100  :   0F;
        Float nuovoTot=nuovaIva+nuovoNetto;
        netto.setText(StringUtils.formatNumber(nuovoNetto));
        totIva.setText(StringUtils.formatNumber(nuovaIva));
        totale.setText(StringUtils.formatNumber(nuovoTot));
    }
    public String modificaFattura() throws GenericExceptionToPrint, ValidationException{
        Fattura daSalvare = caricaFatturaDaForm();
        try{
            fatturaManager.modificaFattura(daSalvare);
            return numFatt.getText();
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore", "Errore nella registrazione della fattura n° "+numFatt.getText()+".",e);
        }
    }
    
    public void annullaAggiunta(){
        if (Dialogs.showYesCancelDialog(form, "Siete sicuri di voler annullare l'operazione?", "Annullamento operazione")==1)
            hideForm();
    }
    protected void registraValidita(){
        if (!isValiditaRegistered){
            isValiditaRegistered=true;
            validitaLastSelectedObject=validita.getSelectedItem();
        }
    }
    protected void unregisterValidita(){
        isValiditaRegistered=false;
    }
    public void ricalcolaData() throws ParseException{
        Object selectedItem = validita.getSelectedItem();
        if (isValiditaRegistered && 
                !validitaLastSelectedObject.equals(selectedItem) &&
                !(validitaLastSelectedObject == null && selectedItem == null)){
            Date x=DateUtils.parseDate(dataEmissione.getText());
            Date scadenza=DateUtils.calcolaScadenza(x,Integer.parseInt((String)selectedItem));
            dataScadenza.setText(DateUtils.formatDate(scadenza));
            validitaLastSelectedObject=selectedItem;
        }
    }
    public void showForm(FormFatturaType formType){
        switch (formType) {
            case AGGIUNGI:
                fatturaAnnulla.setVisible(true);
                fatturaFatto.setVisible(false);
                fatturaModifica.setVisible(false);
                fatturaModificaRiga.setVisible(true);
                fatturaSalva.setVisible(true);
                abilitaFormFattura();
                registraValidita();
                break;
            case MODIFICA:
                fatturaAnnulla.setVisible(true);
                fatturaFatto.setVisible(false);
                fatturaModifica.setVisible(true);
                fatturaModificaRiga.setVisible(true);
                fatturaSalva.setVisible(false);
                abilitaFormFattura();
                registraValidita();
                break;
            case VISUALIZZA:
                fatturaAnnulla.setVisible(false);
                fatturaFatto.setVisible(true);
                fatturaModifica.setVisible(false);
                fatturaModificaRiga.setVisible(false);
                fatturaSalva.setVisible(false);
                disabilitaFormFattura();
                break;
        }
        form.setVisible(true);
    }
    
    protected void removeRows() {
        if (tabellaFattura.getRowCount()!=0){
            for (int i=tabellaFattura.getRowCount()-1;i>-1;i--)
                ((DefaultTableModel)tabellaFattura.getModel()).removeRow(i);
        }
    }
    
    protected Result compilaTabellaFattura (List<DdT> lst, boolean nuovaFattura) throws ValidationException{
        removeRows();
        int resQta=0;
        float resTot=0;
        for(int i=0; i<lst.size();i++){
            DdT d=lst.get(i);
            List<Bene> lstB=d.getBeni();
            for(int j=0;j<lstB.size();j++){
                ArrayList<Object> x=new ArrayList<Object>();
                Bene b=lstB.get(j);
                x.add(d.getId()+" del "+DateUtils.formatDate(d.getData()));
                x.add(b.getCodice());
                x.add(b.getCommessa());
                x.add(b.getDescrizione());
                x.add(b.getPrototipo());
                x.add(b.getCampionario());
                x.add(b.getPrimoCapo());
                x.add(b.getPiazzato());
                x.add(b.getInteramenteAdesivato());
                x.add(b.getQta());
                resQta+=b.getQta();
                Float prezzo;
                Float tot;
                Float totTempo;
                boolean isUnitario;
                if (nuovaFattura){
                    formPrezzoUtilitiesInterface.compilaForm(d, b,fatturaManager.getLastSameBeneFatturatoInfos(b));
                    formPrezzoUtilitiesInterface.showForm(FormPrezzoType.INSERISCI_PREZZO);
                    Float getPrezzo=formPrezzoUtilitiesInterface.getPrezzo();
                    isUnitario = CostoType.UNITARIO.equals(formPrezzoUtilitiesInterface.getCostoType());
                    prezzo = isUnitario ? getPrezzo : null;
                    tot = isUnitario ? getPrezzo*b.getQta() : null;
                    totTempo = isUnitario ? null : getPrezzo;
                } else {
                    Float getPrezzo=b.getPrezzo();
                    isUnitario = getPrezzo!=null && getPrezzo>0;
                    prezzo = isUnitario ? getPrezzo : null;
                    tot = isUnitario ? b.getTot() : null;
                    totTempo = isUnitario ? null : b.getTot();
                }
                resTot += isUnitario ? tot : totTempo;
                x.add(NumberUtils.roundNumber(prezzo));
                x.add(NumberUtils.roundNumber(tot));
                x.add(NumberUtils.roundNumber(totTempo));
                x.add(b.getId());
                ((DefaultTableModel)tabellaFattura.getModel()).addRow(x.toArray());
            }
        }
        return new Result(resQta,resTot);
    }
    
    protected Fattura caricaFatturaDaForm() throws ValidationException{
        
        if (tabellaFattura.getRowCount()>0){
            String[] splitted=((String)tabellaFattura.getValueAt(0, TableColumn.RIF_DDT.toInt())).split("/");
            int meseTemp=Integer.parseInt(splitted[1]);
            logger.debug("Chiamata in caricaFatturaDaForm, p.getAllDdT("+cliente.getText()+","+ meseTemp+")");
            Azienda clienteAzienda = aziendaManager.getAziendaPerNome(cliente.getText());
            List <DdT> listaDdT=ddtManager.getAllDdT(clienteAzienda, meseTemp,true,false);
            Map<Integer,Map<Long,Bene>> mapping=Utility.mapDdT(listaDdT);
            
            for (int i=0;i<tabellaFattura.getRowCount();i++){
                //Calcolo id DdT
                String tableValue=getValueAtColumn(i, TableColumn.RIF_DDT);
                Integer idDdT=Integer.parseInt(tableValue.substring(0, tableValue.indexOf(" ")));
                Long idBene=getValueAtColumn(i, TableColumn.RIGA_ID);
                Bene riga=mapping.get(idDdT).get(idBene);
                Float tot=getValueAtColumn(i, TableColumn.TOT_TEMPO);
                if (tot==null) {
                    tot = getValueAtColumn(i, TableColumn.TOT);
                    Float prezzo=getValueAtColumn(i, TableColumn.COSTO_UNI); 
                    riga.setPrezzo(prezzo);
                }
                else {
                    riga.setPrezzo(null);
                }
                riga.setTot(tot);
            }
            Fattura fattura = new Fattura(listaDdT,parseDateValue(dataEmissione, true),parseDateValue(dataScadenza, true),Integer.parseInt(numFatt.getText()),
                                        clienteAzienda,Float.parseFloat(netto.getText()),Float.parseFloat(ivaPerc.getText()),Float.parseFloat(totIva.getText()),Float.parseFloat(totale.getText()));
            String realIdFatturaText = realIdFattura.getText();
            if (realIdFatturaText != null && !realIdFatturaText.trim().isEmpty()){
                fattura.setRealId(Long.parseLong(realIdFatturaText));
            }
            return fattura;
        }
        return null;
    }

    protected void disabilitaFormFattura(){
        numCapiTot.setEditable(false);
        netto.setEditable(false);
        totIva.setEditable(false);
        totale.setEditable(false);
        cliente.setEditable(false);
        dataScadenza.setEditable(false);
        numFatt.setEditable(false);
        dataEmissione.setEditable(false);
    }
    
    protected void abilitaFormFattura(){
        numFatt.setEditable(true);
        dataEmissione.setEditable(true);
    }

    @Override
    public void hideForm(){
        ((JDialog)form).dispose();
        unregisterValidita();
    }
    
    protected class Result{
        protected int totCapi;
        protected float totFattura;

        public Result(int totCapi, float totFattura) {
            this.totCapi = totCapi;
            this.totFattura = totFattura;

        }
    }
}
