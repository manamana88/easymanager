/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static progettotlp.facilities.NumberUtils.scale;

/**
 *
 * @author vincenzo
 */
public class NumberUtilsTest {

    @Test
    public void scaleTest() {
        assertEquals(new BigDecimal("1.23"), scale(new BigDecimal("1.230")));
        assertEquals(new BigDecimal("1.23"), scale(new BigDecimal("1.231")));
        assertEquals(new BigDecimal("1.23"), scale(new BigDecimal("1.234")));
        assertEquals(new BigDecimal("1.23"), scale(new BigDecimal("1.235")));
        assertEquals(new BigDecimal("1.24"), scale(new BigDecimal("1.236")));
        assertEquals(new BigDecimal("1.24"), scale(new BigDecimal("1.239")));
    }
}