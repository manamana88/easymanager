/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class StringUtilsTest {

    @Test
    public void testFormatNumber() {
        assertEquals("1.00",StringUtils.formatNumber(1));
        assertEquals("2.00",StringUtils.formatNumber(2));
        assertEquals("1.74",StringUtils.formatNumber(1.74326432));
        assertEquals("1.30",StringUtils.formatNumber(1.3));
        assertEquals("121.75",StringUtils.formatNumber(121.74726432));
    }

}