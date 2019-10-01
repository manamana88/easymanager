/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class NumberUtilsTest {

    @Test
    public void testRoundNumber_Float() {
        assertEquals(new Float(3.5), NumberUtils.roundNumber(3.5F));
        assertEquals(new Float(3), NumberUtils.roundNumber(3F));
        assertEquals(new Float(3.55), NumberUtils.roundNumber(3.55F));
        assertEquals(new Float(3.56), NumberUtils.roundNumber(3.555F));
        assertEquals(new Float(3.55), NumberUtils.roundNumber(3.554F));
    }

    @Test
    public void testRoundNumber_Double() {
        assertEquals(new Double(3.5), NumberUtils.roundNumber(3.5D));
        assertEquals(new Double(3), NumberUtils.roundNumber(3D));
        assertEquals(new Double(3.55), NumberUtils.roundNumber(3.55D));
        assertEquals(new Double(3.56), NumberUtils.roundNumber(3.555D));
        assertEquals(new Double(3.55), NumberUtils.roundNumber(3.554D));
    }

}