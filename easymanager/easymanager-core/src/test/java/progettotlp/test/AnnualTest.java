/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.test;

import org.junit.Before;
import org.junit.Ignore;
import progettotlp.ProgettoTLPView;

/**
 *
 * @author vincenzo
 */
@Ignore
public class AnnualTest {

    @Before
    public void setYear(){
        System.setProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY, "2012");
    }

}
