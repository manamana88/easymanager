/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import java.text.ParseException;
import java.util.Date;

import progettotlp.Constants;
import progettotlp.exceptions.toprint.ValidationException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class DateUtilsTest {
	
	@Before
    public void setYear(){
        System.setProperty(Constants.CURRENT_YEAR_PROPERTY, "2012");
    }
	
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void testSetMidnight() throws Exception{
        Date parse = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2013 12:15:15");
        Date parse2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2013 00:00:00");
        assertEquals(parse2, DateUtils.setMidnight(parse));
    }

    @Test
    public void testGetDate() throws Exception {
        assertEquals(simpleDateFormat.parse("01/01/2012"),DateUtils.getDate(1, 1, 2012));
        assertEquals(simpleDateFormat.parse("29/02/2012"),DateUtils.getDate(29, 2, 2012));
        try{
            DateUtils.getDate(32, 1, 2012);
            fail();
        } catch (ValidationException e){}
    }

    @Test
    public void testGetTimeFrame() throws ParseException{
        Date emissione = simpleDateFormat.parse("01/01/2013");
        Date scadenza = simpleDateFormat.parse("31/01/2013");
        Integer timeFrame = DateUtils.getTimeFrame(emissione,scadenza);
        assertEquals(new Integer(30), timeFrame);
        emissione = simpleDateFormat.parse("31/01/2013");
        scadenza = simpleDateFormat.parse("01/04/2013");
        timeFrame = DateUtils.getTimeFrame(emissione,scadenza);
        assertEquals(new Integer(60), timeFrame);
    }

    @Test
    public void testCalcolaScadenza() throws Exception {
        Date test1 = simpleDateFormat.parse("01/01/2012");
        Date result1=simpleDateFormat.parse("02/01/2012");
        int scadenza1=1;

        Date test2 = simpleDateFormat.parse("01/01/2012");
        Date result2=simpleDateFormat.parse("01/03/2012");
        int scadenza2=60;

        assertEquals(result1,DateUtils.calcolaScadenza(test1, scadenza1));
        assertEquals(result2,DateUtils.calcolaScadenza(test2, scadenza2));
    }
    
    @Test
    public void testGetFirstDayOfMonth() throws ParseException{
    	Date result = simpleDateFormat.parse("01/01/2012");
    	Date test=simpleDateFormat.parse("31/01/2012");
    	assertEquals(result,DateUtils.getFirstDayOfMonth(test));
    	
    	result = simpleDateFormat.parse("01/02/2012");
    	test =simpleDateFormat.parse("29/02/2012");
    	assertEquals(result,DateUtils.getFirstDayOfMonth(test));
    	
    	result = simpleDateFormat.parse("1/09/2012");
    	test =simpleDateFormat.parse("28/09/2012");
    	assertEquals(result,DateUtils.getFirstDayOfMonth(test));
    	
    	result = simpleDateFormat.parse("01/09/2012");
    	test =simpleDateFormat.parse("01/09/2012");
    	assertEquals(result,DateUtils.getFirstDayOfMonth(test));
    }

    @Test
    public void testGetFatturaDay() throws ParseException{
        Date test = simpleDateFormat.parse("01/01/2012");
        Date result=simpleDateFormat.parse("31/01/2012");
        assertEquals(result,DateUtils.getFatturaDay(test));

        test = simpleDateFormat.parse("01/02/2012");
        result=simpleDateFormat.parse("29/02/2012");
        assertEquals(result,DateUtils.getFatturaDay(test));

        test = simpleDateFormat.parse("1/09/2012");
        result=simpleDateFormat.parse("28/09/2012");
        assertEquals(result,DateUtils.getFatturaDay(test));

        test = simpleDateFormat.parse("30/09/2012");
        result=simpleDateFormat.parse("28/09/2012");
        assertEquals(result,DateUtils.getFatturaDay(test));
    }
}