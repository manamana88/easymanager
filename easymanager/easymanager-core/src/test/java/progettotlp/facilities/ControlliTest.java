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
public class ControlliTest {
    
    @Test
    public void testCheckMail() {
        assertFalse(Controlli.checkMail(null, true));
        assertFalse(Controlli.checkMail("", true));
        assertTrue(Controlli.checkMail(null, false));
        assertTrue(Controlli.checkMail("", false));
        assertFalse(Controlli.checkMail("A", true));
        assertFalse(Controlli.checkMail("poaspoaspof.vdspo.po", true));
        assertTrue(Controlli.checkMail("vinci.88@tisclai.it", true));
    }

    @Test
    public void testCheckCodFIS() {
        assertFalse(Controlli.checkCodFIS(null, true));
        assertFalse(Controlli.checkCodFIS("", true));
        assertTrue(Controlli.checkCodFIS(null, false));
        assertTrue(Controlli.checkCodFIS("", false));
        assertFalse(Controlli.checkCodFIS("A", true));
        assertFalse(Controlli.checkCodFIS("ABCDEFGHILLOILIO", true));
        assertFalse(Controlli.checkCodFIS("01234567", true));
        assertTrue(Controlli.checkCodFIS("01234567890", true));
        assertTrue(Controlli.checkCodFIS("BBRVCN88M20G482K", true));
    }

    @Test
    public void testCheckIva() {
        assertFalse(Controlli.checkIva(null, true));
        assertFalse(Controlli.checkIva("", true));
        assertTrue(Controlli.checkIva(null, false));
        assertTrue(Controlli.checkIva("", false));
        assertFalse(Controlli.checkIva("A", true));
        assertFalse(Controlli.checkIva("01234567", true));
        assertTrue(Controlli.checkIva("01234567890", true));
    }
}
