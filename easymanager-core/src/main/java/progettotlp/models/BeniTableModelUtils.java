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
public final class BeniTableModelUtils{

    public static TableModel getDefaultTableModel(){
        return new DefaultTableModel(new Object[10][10],new String[]{"Codice", "Commessa", "Descrizione", "Q.ta", "Proto", "Camp", "PC", "Piazz", "IA", "ID"}){
            Class[] types = new Class[]{
                String.class, String.class, String.class, Integer.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Long.class
            };
            boolean[] canEdit = new boolean[]{
                true, true, true, true, true, true, true, true, true, false
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

        TableColumn codiceTableColumn = new TableColumn(0, 100);
        codiceTableColumn.setResizable(false);
        codiceTableColumn.setHeaderValue("Codice");
        TableColumn commessaTableColumn = new TableColumn(1, 150);
        commessaTableColumn.setResizable(false);
        commessaTableColumn.setHeaderValue("Commessa");
        TableColumn descrizioneTableColumn = new TableColumn(2, 150);
        descrizioneTableColumn.setResizable(false);
        descrizioneTableColumn.setHeaderValue("Descrizione");
        TableColumn qtaTableColumn = new TableColumn(3, 50);
        qtaTableColumn.setResizable(false);
        qtaTableColumn.setHeaderValue("Q.ta");
        TableColumn protoTableColumn = new TableColumn(4, 50);
        protoTableColumn.setResizable(false);
        protoTableColumn.setHeaderValue("Proto");
        TableColumn campTableColumn = new TableColumn(5, 50);
        campTableColumn.setResizable(false);
        campTableColumn.setHeaderValue("Camp");
        TableColumn pcTableColumn = new TableColumn(6, 50);
        pcTableColumn.setResizable(false);
        pcTableColumn.setHeaderValue("PC");
        TableColumn piazzTableColumn = new TableColumn(7, 50);
        piazzTableColumn.setResizable(false);
        piazzTableColumn.setHeaderValue("Piazz");
        TableColumn iaTableColumn = new TableColumn(8, 50);
        iaTableColumn.setResizable(false);
        iaTableColumn.setHeaderValue("IA");
        TableColumn idTableColumn = new TableColumn(9, 0);
        idTableColumn.setResizable(false);
        idTableColumn.setHeaderValue("ID");

        defaultTableColumnModel.addColumn(codiceTableColumn);
        defaultTableColumnModel.addColumn(commessaTableColumn);
        defaultTableColumnModel.addColumn(descrizioneTableColumn);
        defaultTableColumnModel.addColumn(qtaTableColumn);
        defaultTableColumnModel.addColumn(protoTableColumn);
        defaultTableColumnModel.addColumn(campTableColumn);
        defaultTableColumnModel.addColumn(pcTableColumn);
        defaultTableColumnModel.addColumn(piazzTableColumn);
        defaultTableColumnModel.addColumn(iaTableColumn);
        defaultTableColumnModel.addColumn(idTableColumn);

        return defaultTableColumnModel;
    }
    
}
