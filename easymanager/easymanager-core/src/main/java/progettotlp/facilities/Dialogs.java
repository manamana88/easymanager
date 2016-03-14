
package progettotlp.facilities;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Classe contenente una serie di metodi "standard" per la visualizzazione
 * di messaggi frequenti durante l'esecuzione dell'applicazione.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
public class Dialogs {
    
    /**
     * Mostra un messaggio di errore.
     * @param padre
     * @param testo
     * @param titolo 
     */
    public static void showErrorDialog(Component padre,String testo, String titolo){
        String property = System.getProperty("dialogsEnabled");
        if (property==null || property.equalsIgnoreCase("true")){
            ImageIcon icon = new ImageIcon("img/no.png");
            JOptionPane.showMessageDialog(padre,testo,titolo,JOptionPane.ERROR_MESSAGE, icon);
        }
    }
    
    /**
     * Mostra una finestra di avvertimento.
     * @param padre
     * @param testo
     * @param titolo 
     */
    public static void showOkDialog(Component padre,String testo, String titolo){
        String property = System.getProperty("dialogsEnabled");
        if (property==null || property.equalsIgnoreCase("true")){
            ImageIcon icon = new ImageIcon("img/ok.png");
            JOptionPane.showMessageDialog(padre,testo,titolo,JOptionPane.ERROR_MESSAGE, icon);
        }
    }
    
    /**
     * Mostra una finestra "SI/ANNULLA".
     * @param padre
     * @param testo
     * @param titolo
     * @return L'esito dell'operazione (SI o ANNULLA).
     */
    public static int showYesCancelDialog(Component padre,String testo, String titolo){
        Object[] options = {"Annulla","Si"};
        return JOptionPane.showOptionDialog(padre,testo,titolo,
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[1]);
    }
    
}
