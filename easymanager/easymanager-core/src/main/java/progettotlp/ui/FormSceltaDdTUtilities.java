/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import progettotlp.classes.DdT;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.exceptions.toprint.CantRetrieveException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.facilities.DateUtils;
import progettotlp.print.TemplateResult;

/**
 *
 * @author vincenzo
 */
public class FormSceltaDdTUtilities extends AbstractFormUtilities{
    private static final String STORING_FOLDER_PATH_DDT="C:"+File.separator+"EasyManager"+File.separator+"DDT";
    private Logger logger = LoggerFactory.getLogger(FormSceltaDdTUtilities.class);
    public static enum FormSceltaDdTType{CANCELLA,MODIFICA,STAMPA,VISUALIZZA};
    
    protected JTable tabellaDdT;
    protected JButton ddtScegliCancella;
    protected JButton ddtScegliModifica;
    protected JButton ddtScegliStampa;
    protected JButton ddtScegliVisualizza;
    
    protected DdTManager ddtManager;
    protected AziendaManager aziendaManager;

    public FormSceltaDdTUtilities(JInternalFrame formSceltaAzienda, AziendaManager aziendaManager, DdTManager ddtManager) {
        this.aziendaManager=aziendaManager;
        this.ddtManager=ddtManager;
        init(formSceltaAzienda);
        File storingFolderDDT=new File(STORING_FOLDER_PATH_DDT);
        storingFolderDDT.mkdirs();
    }
    
    public void removeSelectedRow() throws NoSelectedRow{
        int selectedRow = getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)tabellaDdT.getModel();
        model.removeRow(selectedRow);
    }

    private int getSelectedRow() throws NoSelectedRow {
        int selectedRow = tabellaDdT.getSelectedRow();
        if (selectedRow<0){
            throw new NoSelectedRow("Errore","Nessun DdT selezionato");
        }
        return selectedRow;
    }
    
    public Integer getSelectedDdTId() throws NoSelectedRow{
        return (Integer)tabellaDdT.getValueAt(getSelectedRow(), 0);
    }
    
    public void cancellaSelectedDdT() throws NoSelectedRow, GenericExceptionToPrint{
        Integer selectedDdTId = getSelectedDdTId();
        try{
            ddtManager.cancellaDdT(selectedDdTId);
            removeSelectedRow();
        }catch (Exception e){
            throw new GenericExceptionToPrint("Errore","Siamo spiacenti, si è verificato un errore."+'\n'+"Impossibile cancellare il DdT",e);
        }
    }
    
    public String stampaSelectedDdT() throws NoSelectedRow, CantRetrieveException, GenericExceptionToPrint{
        DdT toPrint=ddtManager.getDdTById(getSelectedDdTId(),true,false);
        if (toPrint!=null){
            try {
                TemplateResult res=new TemplateResult();
                Date data=toPrint.getData();
                String directoryPath=STORING_FOLDER_PATH_DDT+File.separator+DateUtils.getYear(data)+File.separator+DateUtils.getMonthString(data);
                File directory=new File(directoryPath);
                directory.mkdirs();
                String filepath=directoryPath+File.separator+toPrint.getId()+" - "+toPrint.getCliente().getNome()+".html";
                File ddTFile=new File(filepath);
                HashMap h=new HashMap();
                h.put("ddt", toPrint);
                h.put("azienda",aziendaManager.getAziendaPrincipale());
                res.activate("ddt.html", h, new FileOutputStream(ddTFile));
                return directoryPath;
            } catch (Exception ex) {
                throw new GenericExceptionToPrint("Errore","Siamo spiacenti si è verificato un errore."+'\n'+"Impossibile stampare il DdT",ex);
            }
        } else{
            throw new CantRetrieveException("Errore","Siamo spiacenti si è verificato un errore."+'\n'+"Impossibile recuperare il DdT");
        }
    }
    
    /**
     * Metodo che resetta la selezione di un DdT nella tabella.
     */
    protected void resetSceltaDdT(){
        for (int i=tabellaDdT.getRowCount()-1;i>-1;i--){
            ((DefaultTableModel)tabellaDdT.getModel()).removeRow(i);
        }
        tabellaDdT.clearSelection();
    }
    
    /**
     * Metodo per compilare la lista per la scelta dei DdT disponibili.
     */
    protected void compilaListaSceltaDdT(){
        resetSceltaDdT();
        List<DdT> lst=ddtManager.getAllDdT(false,false);
        
        for (DdT d : lst){
            ArrayList<Object> x=new ArrayList<Object>();
            x.add(d.getId());
            x.add(DateUtils.formatDate(d.getData()));
            x.add(d.getCliente().getNome());
            ((DefaultTableModel)tabellaDdT.getModel()).addRow(x.toArray());
        }
    }
    
    public void showForm(FormSceltaDdTType formType){
        compilaListaSceltaDdT();
        switch (formType) {
            case CANCELLA:
                ddtScegliCancella.setVisible(true);
                ddtScegliModifica.setVisible(false);
                ddtScegliStampa.setVisible(false);
                ddtScegliVisualizza.setVisible(false);
                break;
            case MODIFICA:
                ddtScegliCancella.setVisible(false);
                ddtScegliModifica.setVisible(true);
                ddtScegliStampa.setVisible(false);
                ddtScegliVisualizza.setVisible(false);
                break;
            case STAMPA:
                ddtScegliCancella.setVisible(false);
                ddtScegliModifica.setVisible(false);
                ddtScegliStampa.setVisible(true);
                ddtScegliVisualizza.setVisible(false);
                break;
            case VISUALIZZA:
                ddtScegliCancella.setVisible(false);
                ddtScegliModifica.setVisible(false);
                ddtScegliStampa.setVisible(false);
                ddtScegliVisualizza.setVisible(true);
                break;
        }
        form.setVisible(true);
    }
    
}
