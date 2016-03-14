/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import org.slf4j.Logger;
import progettotlp.persistenza.FatturaManager;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import org.slf4j.LoggerFactory;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.facilities.DateUtils;

/**
 *
 * @author vincenzo
 */
public class FormSceltaAziendaUtilities extends AbstractFormUtilities{
    
    private Logger logger = LoggerFactory.getLogger(FormSceltaAziendaUtilities.class);
    public static enum FormSceltaAziendaType{CANCELLA,FATTURA,MODIFICA,VISUALIZZA};
    
    protected JTextField mese;
    protected JList listaAziende;
    protected JButton aziendaScegliCancella;
    protected JButton aziendaScegliVisualizza;
    protected JButton aziendaScegliFattura;
    protected JButton aziendaScegliModifica;
    
    protected AziendaManager aziendaManager;
    protected DdTManager ddTManager;
    protected FatturaManager fatturaManager;

    public FormSceltaAziendaUtilities(JInternalFrame formSceltaAzienda, AziendaManager aziendaManager,DdTManager ddtManager,FatturaManager fatturaManager) {
        this.aziendaManager=aziendaManager;
        this.ddTManager=ddtManager;
        this.fatturaManager=fatturaManager;
        init(formSceltaAzienda);
    }
    
    /**
     * Metodo che popola la lista per selezionare un'Azienda esistente.
     */
    protected void caricaLista(){
        DefaultListModel x= new DefaultListModel();
        List<Azienda> l=aziendaManager.getAziendeNonPrincipali();
        for (Azienda azienda:l){
            x.addElement(azienda.getNome());
        }
        listaAziende.setModel(x);
    }
    
    public void caricaListaPerEmissioneFattura(int selectedMese, String meseString){
        DefaultListModel x = new DefaultListModel();
        
        List<Azienda> lst=aziendaManager.getAziendeNonPrincipali();
        for (Azienda a: lst){
            if (!ddTManager.isEmptyDdTListMese(selectedMese, a)){
                if (!fatturaManager.existsFattura(selectedMese, a))
                    x.addElement(a.getNome());
            }
        }
        mese.setText(meseString);
        listaAziende.setModel(x);
        listaAziende.clearSelection();
    }
    
    public String getSelectedAzienda() throws NoSelectedRow{
        int selectedIndex = getSelectedIndex();
        return (String)listaAziende.getModel().getElementAt(selectedIndex);
    }

    private int getSelectedIndex() throws NoSelectedRow {
        int selectedIndex = listaAziende.getSelectedIndex();
        if (selectedIndex<0){
            throw new NoSelectedRow("Errore", "Nessuna azienda selezionata");
        }
        return selectedIndex;
    }
    
    public int getHiddenSelectedMeseInt(){
        String hiddenSelectedMeseString = getHiddenSelectedMeseString();
        return DateUtils.parseMonthString(hiddenSelectedMeseString);
    }
    
    public String getHiddenSelectedMeseString(){
        return mese.getText();
    }
    
    public void removeSelectedAzienda() throws NoSelectedRow{
        int selectedIndex = getSelectedIndex();
        ((DefaultListModel)listaAziende.getModel()).remove(selectedIndex);
    }
    
    public void cancellaAzienda() throws NoSelectedRow, GenericExceptionToPrint{
        String selectedAzienda = getSelectedAzienda();
        Azienda scelta=aziendaManager.getAziendaPerNome(selectedAzienda);
        try{
            aziendaManager.cancellaAzienda(scelta);
            ((DefaultListModel)listaAziende.getModel()).removeElementAt(listaAziende.getSelectedIndex());
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore cancellazione", "Errore, azienda non eliminata dal sistema.\nProbabilmente ci sono dei DdT associati ad essa",e);
        }
    }
    
    public void showForm(FormSceltaAziendaType formType){
        switch (formType) {
            case CANCELLA:
                caricaLista();
                aziendaScegliCancella.setVisible(true);
                aziendaScegliFattura.setVisible(false);
                aziendaScegliModifica.setVisible(false);
                aziendaScegliVisualizza.setVisible(false);
                break;
            case FATTURA:
                aziendaScegliCancella.setVisible(false);
                aziendaScegliFattura.setVisible(true);
                aziendaScegliModifica.setVisible(false);
                aziendaScegliVisualizza.setVisible(false);
                break;
            case MODIFICA:
                caricaLista();
                aziendaScegliCancella.setVisible(false);
                aziendaScegliFattura.setVisible(false);
                aziendaScegliModifica.setVisible(true);
                aziendaScegliVisualizza.setVisible(false);
                break;
            case VISUALIZZA:
                caricaLista();
                aziendaScegliCancella.setVisible(false);
                aziendaScegliFattura.setVisible(false);
                aziendaScegliModifica.setVisible(false);
                aziendaScegliVisualizza.setVisible(true);
                break;
        }
        listaAziende.clearSelection();
        form.setVisible(true);
    }
    
    
}
