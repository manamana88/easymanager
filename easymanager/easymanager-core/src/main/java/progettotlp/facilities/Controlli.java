
package progettotlp.facilities;

import java.util.regex.*;

/**
 * Classe contenente una serie di metodi per eseguire controlli 
 * sulla correttezza di un indirizzo email.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
public class Controlli {
    private static final String MAIL = "[a-zA-Z0-9][a-zA-Z0-9.-_]+@[a-zA-Z0-9.-_]+\\.[a-zA-Z]{2,4}";
    private static final String IVA = "\\d{11}";
    private static final String CODFIS = IVA+"||[a-zA-Z]{6}\\d{2}[a-zA-Z]\\d{2}[a-zA-Z]\\d{3}[a-zA-Z]";
    private static final String CODICE_FATTURA_PA = "\\d{6,7}";
    
    private static boolean generalCheck(String regex, String toCheck, boolean obbligatorio){
        boolean isEmpty = toCheck == null || toCheck.trim().isEmpty();
        if (isEmpty)
            return !obbligatorio;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(toCheck);
        if (matcher.matches())
            return true;
        return false;
    }
    /**
     * Metodo che sfrutta la definizione di un'espressione regolare per controllare
     * che l'indirizzo email sia scritto rispettando la convenzione.
     * @param mail
     * @return true se l'indirizzo è scritto in formato corretto, false altrimenti.
     */
    public static boolean checkMail(String mail, boolean obbligatorio) {
        return generalCheck(MAIL, mail, obbligatorio);
    }
    public static boolean checkCodFIS(String codFis, boolean obbligatorio){
        return generalCheck(CODFIS, codFis, obbligatorio);
    }
    public static boolean checkIva(String iva, boolean obbligatorio){
        return generalCheck(IVA, iva, obbligatorio);
    }
    public static boolean checkCodiceFatturaPa(String codiceFatturaPa, boolean obbligatorio){
    	return generalCheck(CODICE_FATTURA_PA, codiceFatturaPa, obbligatorio);
    }
    
}
