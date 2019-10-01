/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.test;

import org.junit.Before;

import progettotlp.Constants;

/**
 *
 * @author vincenzo
 */
public class AnnualTest {

    @Before
    public void setYear(){
        System.setProperty(Constants.CURRENT_YEAR_PROPERTY, "2012");
    }

}
