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
public class ConversioniTest {

    @Test
    public void testBoolToYN() {
        assertEquals("Y", Conversioni.boolToYN(true));
        assertEquals("N", Conversioni.boolToYN(false));
    }

    @Test
    public void testYNToBool() {
        assertEquals(true, Conversioni.YNToBool("Y"));
        assertEquals(false, Conversioni.YNToBool("N"));
    }

}