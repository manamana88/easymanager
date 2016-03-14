/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.StringUtils;
import progettotlp.models.StatisticheRisultatiTableModelUtils;
import progettotlp.statistiche.StatisticheConfronto;
import progettotlp.statistiche.StatisticheFattura;
import progettotlp.ui.FormStatisticheUtilities.FormRicercaType;

/**
 *
 * @author sys0
 */
public class FormStatisticheRisultatiUtilities extends AbstractFormUtilities{

    protected JTable risultatiStatistiche;
    
    public FormStatisticheRisultatiUtilities(JDialog form) {
        init(form);
    }

    protected TreeMap<String,TreeSet<Date>> aggregateKeys(Set<Date> first, Set<Date> second){
        TreeMap<String,TreeSet<Date>> result= new TreeMap<String, TreeSet<Date>>(DateUtils.getMonstStringComparator());
        TreeSet<Date> treeSet = new TreeSet<Date>();
        if (first!=null){
            treeSet.addAll(first);
        }
        if (second!=null){
            treeSet.addAll(second);
        }
        for (Date d : treeSet){
            String monthString = DateUtils.getMonthString(d);
            TreeSet<Date> tempTreeSet=result.get(monthString);
            if (tempTreeSet == null){
                tempTreeSet = new TreeSet<Date>();
            }
            for (Date dTemp:treeSet){
                if (monthString.equals(DateUtils.getMonthString(dTemp))){
                    tempTreeSet.add(dTemp);
                }
            }
            result.put(monthString, tempTreeSet);
        }
        return result;
    }

    protected String getCapiSum(TreeSet<Date> dates, Map<Date,List<StatisticheFattura>> list){
        Long sum=0L;
        if (dates != null && list!=null){
            for (Date d : dates){
                List<StatisticheFattura> retrieved = list.get(d);
                if (retrieved != null){
                    for (StatisticheFattura stats : retrieved){
                        sum += stats.getCapiTagliati();
                    }
                }
            }
        }
        return sum.toString();
    }

    protected String getFatturatoConIvaSum(TreeSet<Date> dates, Map<Date,List<StatisticheFattura>> list){
        Float sum=0F;
        if (dates != null && list!=null){
            for (Date d : dates){
                List<StatisticheFattura> retrieved = list.get(d);
                if (retrieved != null){
                    for (StatisticheFattura stats : retrieved){
                        sum += stats.getTotale();
                    }
                }
            }
        }
        return StringUtils.formatNumber(sum);
    }

    protected String getFatturatoNoIvaSum(TreeSet<Date> dates, Map<Date,List<StatisticheFattura>> list){
        Float sum=0F;
        if (dates != null && list!=null){
            for (Date d : dates){
                List<StatisticheFattura> retrieved = list.get(d);
                if (retrieved != null){
                    for (StatisticheFattura stats : retrieved){
                        sum += stats.getNetto();
                    }
                }
            }
        }
        return StringUtils.formatNumber(sum);
    }

    protected void compilaRisultati(FormRicercaType ricercaType, StatisticheConfronto confronto, boolean isSemplice){
        DefaultTableModel model = (DefaultTableModel)risultatiStatistiche.getModel();
        Map<Date, List<StatisticheFattura>> statistichePrimoPeriodo = confronto.getStatistichePrimoPeriodo();
        Map<Date, List<StatisticheFattura>> statisticheSecondoPeriodo = confronto.getStatisticheSecondoPeriodo();
        TreeMap<String, TreeSet<Date>> mergeKeys;
        if (isSemplice){
            mergeKeys= aggregateKeys(statistichePrimoPeriodo.keySet(), null);
        } else {
            mergeKeys= aggregateKeys(statistichePrimoPeriodo.keySet(), statisticheSecondoPeriodo.keySet());
        }
        for (String month: mergeKeys.keySet()){
            TreeSet<Date> dates = mergeKeys.get(month);
            switch (ricercaType){
                case CAPI_TAGLIATI:
                    model.addRow(new String[]{
                        month,
                        getCapiSum( dates, statistichePrimoPeriodo),
                        getCapiSum( dates, statisticheSecondoPeriodo)});
                    break;
                case FATTURATO_CON_IVA:
                    model.addRow(new String[]{
                        month,
                        getFatturatoConIvaSum( dates, statistichePrimoPeriodo),
                        getFatturatoConIvaSum( dates, statisticheSecondoPeriodo)});
                    break;
                case FATTURATO_NO_IVA:
                    model.addRow(new String[]{
                        month,
                        getFatturatoNoIvaSum( dates, statistichePrimoPeriodo),
                        getFatturatoNoIvaSum( dates, statisticheSecondoPeriodo)});
                    break;
            }

            
        }
    }

    public void show(FormRicercaType ricercaType, StatisticheConfronto confronto, boolean isSemplice){
        String[] columns;
        switch (ricercaType){
            case CAPI_TAGLIATI:
                columns=isSemplice?new String[]{"Dati", "Capi Periodo 1"}:new String[]{"Dati", "Capi Periodo 1", "Capi Periodo 2"};
                break;
            case FATTURATO_CON_IVA:
                columns=isSemplice?new String[]{"Dati", "Fatturato Periodo 1"}:new String[]{"Dati", "Fatturato Periodo 1", "Fatturato Periodo 2"};
                break;
            case FATTURATO_NO_IVA:
                columns=isSemplice?new String[]{"Dati", "Fatturato Periodo 1"}:new String[]{"Dati", "Fatturato Periodo 1", "Fatturato Periodo 2"};
                break;
            default:
                columns=isSemplice?new String[]{"Dati", "Periodo 1"}:new String[]{"Dati", "Periodo 1", "Periodo 2"};
        }
        risultatiStatistiche.setModel(StatisticheRisultatiTableModelUtils.buildCustomTableModel(columns));
        risultatiStatistiche.setColumnModel(StatisticheRisultatiTableModelUtils.buildCustomTableColumnModel(columns));
        compilaRisultati(ricercaType, confronto,isSemplice);
        form.setSize(680,330);
        form.setVisible(true);
    }

}
