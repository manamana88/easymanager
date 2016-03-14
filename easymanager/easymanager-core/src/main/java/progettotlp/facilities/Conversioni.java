/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.facilities;

/**
 *
 * @author Vincenzo Barrea, Alessio Felicioni
 */
public class Conversioni {
    
    /**
     * Metodo che converte un valore booleano in una stringa.
     * @param k
     * @return "Y" se il valore passato è true, "N" altrimenti.
     */
    public static String boolToYN(boolean k) {
        if (k) {
            return "Y";
        } else {
            return "N";
        }
    }
    
    /**
     * Metodo che converte una stringa in un valore booleano.
     * @param k
     * @return true se la stringa passata è "Y", false altrimenti.
     */
    public static Boolean YNToBool(String k){
        if (!k.equals("Y")&&!k.equals("N")) return null;
        if (k.equals("Y")) return true;
        else return false;
    }
    
}
