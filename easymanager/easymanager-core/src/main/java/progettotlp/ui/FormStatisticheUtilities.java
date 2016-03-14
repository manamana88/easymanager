/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.DateUtils;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.StatisticheManager;
import progettotlp.statistiche.StatisticheConfronto;

/**
 *
 * @author sys0
 */
public class FormStatisticheUtilities extends AbstractFormUtilities{
    public static enum FormStatisticheType{SEMPLICE,CONFRONTA_PERIODI};
    public static enum FormRicercaType{CAPI_TAGLIATI,FATTURATO_CON_IVA,FATTURATO_NO_IVA};
    protected StatisticheManager statisticsManager;
    protected JTextField startDate;
    protected JTextField endDate;
    protected JLabel confrontoLabel;
    protected JTextField startDateConfronto;
    protected JTextField endDateConfronto;
    protected JList listaAziendeStatistiche;
    protected JRadioButton capiTagliatiRadioButton;
    protected JRadioButton fatturatoNoIvaRadioButton;
    protected JRadioButton fatturatoConIvaRadioButton;
    protected JButton avviaRicerca;
    protected JButton resetForm;
    protected ButtonGroup statisticheType;

    protected AziendaManager aziendaManager;
    protected boolean isSemplice;

    public FormStatisticheUtilities(JDialog form, StatisticheManager statisticsManager, AziendaManager aziendaManager) {
        this.statisticsManager=statisticsManager;
        this.aziendaManager=aziendaManager;
        init(form);
        statisticheType=new ButtonGroup();
        statisticheType.add(capiTagliatiRadioButton);
        statisticheType.add(fatturatoNoIvaRadioButton);
        statisticheType.add(fatturatoConIvaRadioButton);
    }

    protected void compilaListaAziende(){
        DefaultListModel listModel=new DefaultListModel();
        List<Azienda> aziende = aziendaManager.getAziendeNonPrincipali();
        for (Azienda a : aziende){
            listModel.addElement(a.getNome());
        }
        listaAziendeStatistiche.setModel(listModel);
    }

    protected List<String> caricaNomiAziendeSelezionate(){
        List<String> nomiAziende = new ArrayList<String>();
        if (!listaAziendeStatistiche.isSelectionEmpty()){
            for (Object o : listaAziendeStatistiche.getSelectedValues()){
                nomiAziende.add((String)o);
            }
        }
        return nomiAziende;
    }

    public FormRicercaType getRicercaType() throws ValidationException{
        boolean isCapiTagliatiSelected = capiTagliatiRadioButton.isSelected();
        boolean isFatturatoConIvaSelected = fatturatoConIvaRadioButton.isSelected();
        boolean isFatturatoNoIvaSelected = fatturatoNoIvaRadioButton.isSelected();

        if (isCapiTagliatiSelected){
            return FormRicercaType.CAPI_TAGLIATI;
        } else if (isFatturatoConIvaSelected){
            return FormRicercaType.FATTURATO_CON_IVA;
        } else if (isFatturatoNoIvaSelected){
            return FormRicercaType.FATTURATO_NO_IVA;
        }
        throw new ValidationException("Non hai scelto cosa controllare");
    }

    public boolean isRicercaSemplice(){
        return !startDateConfronto.isVisible();
    }

    public StatisticheConfronto avviaRicerca() throws ValidationException{
        Date startDateValue = parseDateValue(startDate, true);
        Date endDateValue = parseDateValue(endDate, true);
        if (DateUtils.getTimeFrame(startDateValue,endDateValue)>365){
            throw new ValidationException("Non è possibile selezionare un lasso di tempo superiore ai 365 giorni.");
        }
        List<String> nomiAziendeSelezionate = caricaNomiAziendeSelezionate();
        if (isSemplice){
            return new StatisticheConfronto(statisticsManager.simpleSearch(startDateValue, endDateValue, nomiAziendeSelezionate), null);
        } else {
            Date startDateConfrontoValue = parseDateValue(startDateConfronto, true);
            Date endDateConfrontoValue = parseDateValue(endDateConfronto, true);
            if (DateUtils.getTimeFrame(startDateConfrontoValue,endDateConfrontoValue)>365){
                throw new ValidationException("Non è possibile selezionare un lasso di tempo superiore ai 365 giorni.");
            }
            return statisticsManager.advancedSearch(startDateValue, endDateValue, startDateConfrontoValue, endDateConfrontoValue, nomiAziendeSelezionate);
        }
    }

    public void resetForm(){
        startDate.setText(null);
        endDate.setText(null);
        startDateConfronto.setText(null);
        endDateConfronto.setText(null);
        listaAziendeStatistiche.clearSelection();
        capiTagliatiRadioButton.setSelected(false);
        fatturatoConIvaRadioButton.setSelected(false);
        fatturatoNoIvaRadioButton.setSelected(false);
        statisticheType.clearSelection();
    }

    public void show(FormStatisticheType type){
        compilaListaAziende();
        resetForm();
        switch (type){
            case SEMPLICE:
                isSemplice=true;
                confrontoLabel.setVisible(false);
                startDateConfronto.setVisible(false);
                endDateConfronto.setVisible(false);
                break;
            case CONFRONTA_PERIODI:
                isSemplice=false;
                confrontoLabel.setVisible(true);
                startDateConfronto.setVisible(true);
                endDateConfronto.setVisible(true);
                break;
        }
        form.setSize(350, 420);
        form.setVisible(true);
    }

}
