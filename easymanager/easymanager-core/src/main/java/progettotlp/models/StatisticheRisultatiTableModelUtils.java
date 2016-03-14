/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.models;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author vincenzo
 */
public final class StatisticheRisultatiTableModelUtils{

    public static TableModel getDefaultTableModel(){
        return buildCustomTableModel("Data","Periodo1","Periodo2");
    }

    public static TableColumnModel getDefaultTableColumnModel(){
        return buildCustomTableColumnModel("Data","Periodo1","Periodo2");
    }

    public static TableModel buildCustomTableModel(String ... columns){
        final int length = columns.length;
        return new DefaultTableModel(new Object[0][length],columns){
            Class[] types = new Class[length];
            boolean[] canEdit = new boolean[length];

            {
                for (int i=0; i<length; i++){
                    types[i]=String.class;
                    canEdit[i]=false;
                }
            }

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }
    
    public static TableColumnModel buildCustomTableColumnModel(String ... columns){
        DefaultTableColumnModel defaultTableColumnModel = new DefaultTableColumnModel();

        for (int i = 0; i<columns.length; i++){
            TableColumn tableColumn = new TableColumn(i, 150);
            tableColumn.setResizable(true);
            tableColumn.setHeaderValue(columns[i]);
            defaultTableColumnModel.addColumn(tableColumn);
        }

        return defaultTableColumnModel;
    }

}