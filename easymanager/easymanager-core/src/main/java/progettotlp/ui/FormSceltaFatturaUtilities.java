/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import progettotlp.facilities.FatturaUtilities;
import progettotlp.facilities.DateUtils;
import java.util.Date;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.util.Map.Entry;
import java.io.File;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.FatturaManager;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.toprint.CantRetrieveException;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.print.BeneFattura;
import progettotlp.print.FatturaPrinter;
import progettotlp.print.TemplateResult;

import static progettotlp.facilities.Utility.*;
/**
 *
 * @author vincenzo
 */
public class FormSceltaFatturaUtilities extends AbstractFormUtilities{
    
    public enum FormSceltaFatturaType{CANCELLA,MODIFICA,STAMPA,VISUALIZZA};

    public static final Integer ROWS_PER_PAGE=35;
    private static final String STORING_FOLDER_PATH_FATTURE="C:"+File.separator+"EasyManager"+File.separator+"FATTURE";
    
    protected JTable tabellaFatture;
    protected JButton fatturaScegliCancella;
    protected JButton fatturaScegliModifica;
    protected JButton fatturaScegliStampa;
    protected JButton fatturaScegliVisualizza;
    
    protected AziendaManager aziendaManager;
    protected FatturaManager fatturaManager;

    public FormSceltaFatturaUtilities(JInternalFrame formSceltaFattura,AziendaManager aziendaManager, FatturaManager fatturaManager) {
        this.aziendaManager=aziendaManager;
        this.fatturaManager=fatturaManager;
        init(formSceltaFattura);
        File storingFolderFATTURE=new File(STORING_FOLDER_PATH_FATTURE);
        storingFolderFATTURE.mkdirs();
    }
   
    protected void resetSceltaFattura(){
        for (int i=tabellaFatture.getRowCount()-1;i>-1;i--)
            ((DefaultTableModel)tabellaFatture.getModel()).removeRow(i);
    }
    
    
    protected void compilaSceltaFattura(){
        List<Fattura> lst=fatturaManager.getAllFatture(false,false);
        for (int i=0; i<lst.size();i++){
            Fattura f=lst.get(i);
            ArrayList<Object> x=new ArrayList<Object>();
            x.add(f.getId());
            x.add(f.getCliente().getNome());
            x.add(DateUtils.formatDate(f.getEmissione()));
            x.add(DateUtils.formatDate(f.getScadenza()));
            ((DefaultTableModel)tabellaFatture.getModel()).addRow(x.toArray());
        }
    }
    public int getSelectedFatturaId() throws NoSelectedRow{
        int selectedRow = tabellaFatture.getSelectedRow();
        if (selectedRow == -1){
            throw new NoSelectedRow("Errore","Nessuna fattura selezionata");
        }
        return (Integer)((DefaultTableModel)tabellaFatture.getModel()).getValueAt(selectedRow, 0);
    }
    public Fattura getSelectedFattura(boolean initializeDdT, boolean initializeBeni) throws NoSelectedRow, CantRetrieveException{
        int selectedFatturaId = getSelectedFatturaId();
        Fattura result = fatturaManager.getFattura(selectedFatturaId,initializeDdT,initializeBeni);
        if (result==null){
            throw new CantRetrieveException("Errore", "Impossibile recuperare la fattura");
        }
        return result;
    }
    public void cancellaFattura() throws NoSelectedRow, GenericExceptionToPrint{
        int selectedFatturaId = getSelectedFatturaId();
        try{
            fatturaManager.cancellaFattura(selectedFatturaId);
            ((DefaultTableModel)tabellaFatture.getModel()).removeRow(tabellaFatture.getSelectedRow());
        }catch (Exception e){
            throw new GenericExceptionToPrint("Errore", "Siamo spiacenti, si è verificato un errore."+'\n'+"Impossibile cancellare la fattura",e);
        }
    }
    public String stampaFattura() throws NoSelectedRow, CantRetrieveException, GenericExceptionToPrint{
        File directory = null;
        try {
            Fattura toPrint = getSelectedFattura(true, true);
            directory = new File(FatturaUtilities.getDirectoryPath(toPrint,
                    STORING_FOLDER_PATH_FATTURE));
            directory.mkdirs();
            FatturaPrinter.printPage(toPrint,
                    this.aziendaManager.getAziendaPrincipale(),
                    directory.getAbsolutePath(),
                    FatturaUtilities.getFileName(toPrint), false);
        } catch (Exception ex) {
            throw new GenericExceptionToPrint("Errore", "Siamo spiacenti, si è verificato un errore."+'\n'+"Impossibile stampare la fattura",ex);
        }
        return directory.getAbsolutePath();
    }
    public void showForm(FormSceltaFatturaType formType){
        resetSceltaFattura();
        compilaSceltaFattura();
        switch (formType) {
            case CANCELLA:
                fatturaScegliCancella.setVisible(true);
                fatturaScegliModifica.setVisible(false);
                fatturaScegliStampa.setVisible(false);
                fatturaScegliVisualizza.setVisible(false);
                break;
            case MODIFICA:
                fatturaScegliCancella.setVisible(false);
                fatturaScegliModifica.setVisible(true);
                fatturaScegliStampa.setVisible(false);
                fatturaScegliVisualizza.setVisible(false);
                break;
            case STAMPA:
                resetSceltaFattura();
                compilaSceltaFattura();
                fatturaScegliCancella.setVisible(false);
                fatturaScegliModifica.setVisible(false);
                fatturaScegliStampa.setVisible(true);
                fatturaScegliVisualizza.setVisible(false);
                break;
            case VISUALIZZA:
                fatturaScegliCancella.setVisible(false);
                fatturaScegliModifica.setVisible(false);
                fatturaScegliStampa.setVisible(false);
                fatturaScegliVisualizza.setVisible(true);
                break;
        }
        form.setVisible(true);
    }
    
}
