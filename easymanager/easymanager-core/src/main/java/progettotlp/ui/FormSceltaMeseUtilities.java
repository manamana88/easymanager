/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import progettotlp.facilities.DateUtils;

/**
 *
 * @author vincenzo
 */
public class FormSceltaMeseUtilities extends AbstractFormUtilities{
    
    protected JComboBox elencoMesi;
    protected JButton sceltaMeseOk;
    
    public FormSceltaMeseUtilities(JInternalFrame formSceltaMese) {
        init(formSceltaMese);
    }
    
    public int getSelectedMeseInt(){
        return DateUtils.parseMonthString(getSelectedMeseString());
    }
    
    public String getSelectedMeseString(){
        return (String)elencoMesi.getSelectedItem();
    }
    
    public void showForm(){
        Date x=new Date();
        elencoMesi.removeAllItems();
        String currentMonth=DateUtils.getMonthString(x);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(x);
        c.add(Calendar.MONTH, -1);
        String previousMonth=DateUtils.getMonthString(c.getTime());
        c.add(Calendar.MONTH, 2);
        String afterMonth=DateUtils.getMonthString(c.getTime());
        elencoMesi.addItem(previousMonth);
        elencoMesi.addItem(currentMonth);
        elencoMesi.addItem(afterMonth);
        elencoMesi.setSelectedIndex(0);
        form.setVisible(true);
    }
    
}
