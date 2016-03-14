/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;

/**
 *
 * @author vincenzo
 */
public class MyTest {

    @Test
    public void test(){

        Calendar instance = GregorianCalendar.getInstance();
        instance.set(2012, 0, 35);
        System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(instance.getTime()));
    }
}
