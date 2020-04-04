/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import java.math.BigDecimal;

/**
 *
 * @author vincenzo
 */
public class NumberUtils {

	public static BigDecimal floatToBigDecimal(Float x) {
		String string = Float.toString(x);
		return new BigDecimal(string);
	}
	
	public static BigDecimal doubleToBigDecimal(Double x) {
		String string = Double.toString(x);
		return new BigDecimal(string);
	}
	
    public static Float roundNumber(Float x){
        if (x==null)
            return null;
        x*=100;
        int rounded = Math.round(x);
        return new Float(rounded)/100F;
    }

    public static Double roundNumber(Double x){
        if (x==null){
            return null;
        }
        x*=100;
        long rounded = Math.round(x);
        return new Double(rounded)/100D;
    }

}
