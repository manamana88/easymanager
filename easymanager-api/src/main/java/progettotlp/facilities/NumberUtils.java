/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import progettotlp.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	
    public static BigDecimal scale(BigDecimal bigDecimal){
	    return bigDecimal.setScale(Constants.DEFAULT_SCALE, RoundingMode.HALF_DOWN);
    }

}
