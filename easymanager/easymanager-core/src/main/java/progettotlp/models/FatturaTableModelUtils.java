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
public final class FatturaTableModelUtils{

    public static TableModel getDefaultTableModel(){
        return new DefaultTableModel(new Object[0][10],new String[]{"Rif. DdT n°", "Cod.", "Commessa", "Descrizione", "PROT", "CAMP", "P_C", "PIAZZ", "IA", "n° capi", "Costo cad.", "Totale", "Totale", "Id"}){
            Class[] types = new Class[]{
                String.class, String.class, String.class, String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Integer.class, Float.class, Float.class, Float.class, Long.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
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

        TableColumn rifDdTTableColumn = new TableColumn(0, 100);
        rifDdTTableColumn.setResizable(false);
        rifDdTTableColumn.setHeaderValue("Rif. DdT n°");
        TableColumn codiceTableColumn = new TableColumn(1, 100);
        codiceTableColumn.setResizable(false);
        codiceTableColumn.setHeaderValue("Codice");
        TableColumn commessaTableColumn = new TableColumn(2, 150);
        commessaTableColumn.setResizable(false);
        commessaTableColumn.setHeaderValue("Commessa");
        TableColumn descrizioneTableColumn = new TableColumn(3, 180);
        descrizioneTableColumn.setResizable(false);
        descrizioneTableColumn.setHeaderValue("Descrizione");
        TableColumn protoTableColumn = new TableColumn(4, 40);
        protoTableColumn.setResizable(false);
        protoTableColumn.setHeaderValue("PROT");
        TableColumn campTableColumn = new TableColumn(5, 40);
        campTableColumn.setResizable(false);
        campTableColumn.setHeaderValue("CAMP");
        TableColumn pcTableColumn = new TableColumn(6, 40);
        pcTableColumn.setResizable(false);
        pcTableColumn.setHeaderValue("PC");
        TableColumn piazzTableColumn = new TableColumn(7, 40);
        piazzTableColumn.setResizable(false);
        piazzTableColumn.setHeaderValue("PIAZ");
        TableColumn iaTableColumn = new TableColumn(8, 40);
        iaTableColumn.setResizable(false);
        iaTableColumn.setHeaderValue("IA");
        TableColumn qtaTableColumn = new TableColumn(9, 50);
        qtaTableColumn.setResizable(false);
        qtaTableColumn.setHeaderValue("n° capi");
        TableColumn costoUniTableColumn = new TableColumn(10, 60);
        costoUniTableColumn.setResizable(false);
        costoUniTableColumn.setHeaderValue("Costo cu.");
        TableColumn totaleTableColumn = new TableColumn(11, 60);
        totaleTableColumn.setResizable(false);
        totaleTableColumn.setHeaderValue("Totale");
        TableColumn totaleTempoTableColumn = new TableColumn(12, 90);
        totaleTempoTableColumn.setResizable(false);
        totaleTempoTableColumn.setHeaderValue("Totale a tempo");
        TableColumn idTableColumn = new TableColumn(13, 0);
        idTableColumn.setResizable(false);
        idTableColumn.setHeaderValue("ID");

        defaultTableColumnModel.addColumn(rifDdTTableColumn);
        defaultTableColumnModel.addColumn(codiceTableColumn);
        defaultTableColumnModel.addColumn(commessaTableColumn);
        defaultTableColumnModel.addColumn(descrizioneTableColumn);
        defaultTableColumnModel.addColumn(protoTableColumn);
        defaultTableColumnModel.addColumn(campTableColumn);
        defaultTableColumnModel.addColumn(pcTableColumn);
        defaultTableColumnModel.addColumn(piazzTableColumn);
        defaultTableColumnModel.addColumn(iaTableColumn);
        defaultTableColumnModel.addColumn(qtaTableColumn);
        defaultTableColumnModel.addColumn(costoUniTableColumn);
        defaultTableColumnModel.addColumn(totaleTableColumn);
        defaultTableColumnModel.addColumn(totaleTempoTableColumn);
        defaultTableColumnModel.addColumn(idTableColumn);

        return defaultTableColumnModel;
    }
    
}
