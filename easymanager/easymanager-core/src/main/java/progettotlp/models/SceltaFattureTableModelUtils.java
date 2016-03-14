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
public final class SceltaFattureTableModelUtils{

    public static TableModel getDefaultTableModel(){
        return new DefaultTableModel(new Object[0][4],new String[]{"ID", "Cliente", "Emissione", "Scadenza"}){
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
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

        TableColumn idTableColumn = new TableColumn(0, 70);
        idTableColumn.setResizable(false);
        idTableColumn.setHeaderValue("ID");
        TableColumn clienteTableColumn = new TableColumn(1, 130);
        clienteTableColumn.setResizable(false);
        clienteTableColumn.setHeaderValue("Cliente");
        TableColumn emissioneTableColumn = new TableColumn(2, 100);
        emissioneTableColumn.setResizable(false);
        emissioneTableColumn.setHeaderValue("Emissione");
        TableColumn scadenzaTableColumn = new TableColumn(3, 100);
        scadenzaTableColumn.setResizable(false);
        scadenzaTableColumn.setHeaderValue("Scadenza");

        defaultTableColumnModel.addColumn(idTableColumn);
        defaultTableColumnModel.addColumn(clienteTableColumn);
        defaultTableColumnModel.addColumn(emissioneTableColumn);
        defaultTableColumnModel.addColumn(scadenzaTableColumn);

        return defaultTableColumnModel;
    }
    
}
