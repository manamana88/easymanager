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
public final class SceltaDdTTableModelUtils{

    public static TableModel getDefaultTableModel(){
        return new DefaultTableModel(new Object[0][3],new String[]{"ID", "Data", "Cliente"}){
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false
            };

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

    public static TableColumnModel getDefaultTableColumnModel(){
        DefaultTableColumnModel defaultTableColumnModel = new DefaultTableColumnModel();

        TableColumn idTableColumn = new TableColumn(0, 166);
        idTableColumn.setResizable(false);
        idTableColumn.setHeaderValue("ID");
        TableColumn dataTableColumn = new TableColumn(1, 166);
        dataTableColumn.setResizable(false);
        dataTableColumn.setHeaderValue("Data");
        TableColumn clienteTableColumn = new TableColumn(2, 166);
        clienteTableColumn.setResizable(false);
        clienteTableColumn.setHeaderValue("Cliente");

        defaultTableColumnModel.addColumn(idTableColumn);
        defaultTableColumnModel.addColumn(dataTableColumn);
        defaultTableColumnModel.addColumn(clienteTableColumn);

        return defaultTableColumnModel;
    }
    
}
