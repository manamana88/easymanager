/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import progettotlp.ProgettoTLPView;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.Utility;

/**
 *
 * @author vincenzo
 */
public class FormSceltaAnnoUtilities extends AbstractFormUtilities{
    
    protected JComboBox anno;
    protected JButton sceltaAnnoOk;
    protected JButton sceltaAnnoAnnulla;
    
    public FormSceltaAnnoUtilities(JInternalFrame formSceltaMese) {
        init(formSceltaMese);
    }

    public int getSelectedAnnoInt(){
        return (Integer)anno.getSelectedItem();
    }

    protected void compilaFormAnno() throws NumberFormatException {
        anno.removeAllItems();
        Date x = new Date();
        int currentAnno = DateUtils.getYear(x);
        int settedAnno = Utility.getSelectedAnno();
        for (int i = currentAnno - 4; i <= currentAnno; i++) {
            anno.addItem(i);
        }
        anno.setSelectedItem(settedAnno);
    }
    
    public void showForm(){
        compilaFormAnno();
        form.setVisible(true);
    }

    public void setAnno() {
        System.setProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY, getSelectedAnnoInt()+"");
    }
    
}
