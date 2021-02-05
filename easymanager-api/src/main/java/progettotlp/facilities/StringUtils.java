/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import java.text.NumberFormat;
import java.util.Locale;


/**
 *
 * @author vincenzo
 */
public final class StringUtils {

    public static String formatNumber(Object x){
        NumberFormat formatter= NumberFormat.getNumberInstance(Locale.ENGLISH);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        formatter.setGroupingUsed(false);
        return formatter.format(x);
    }
    
    public static String capitalise(String string){
        if (string == null){
            return null;
        } 
        if (string.isEmpty()){
            return string;
        }
        StringBuilder s = new StringBuilder();
        s.append(Character.toUpperCase(string.charAt(0)));
        if (string.length() > 1){
            s.append(string.substring(1));
        }
        return s.toString();
    }
}
