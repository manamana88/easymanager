/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.facilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import progettotlp.exceptions.toprint.ValidationException;

/**
 *
 * @author vincenzo
 */
public class DateUtils {

	public static final String HQL_FORMAT = "yyyy-MM-dd";
    public static final String STANDARD_FORMAT = "dd/MM/yyyy";
	private static final Long SECONDS_IN_A_DAY=24L * 3600L * 1000L;

    public static Date setMidnight(Date d){
        Calendar instance = GregorianCalendar.getInstance();
        instance.clear();
        instance.set(Calendar.YEAR, getYear(d));
        instance.set(Calendar.MONTH, getMonth(d)-1);
        instance.set(Calendar.DAY_OF_MONTH, getDay(d));
        return instance.getTime();
    }

    public static Integer getTimeFrame(Date emissione, Date scadenza) {
        scadenza = DateUtils.setMidnight(scadenza);
        emissione = DateUtils.setMidnight(emissione);
        Long validitaValue = scadenza.getTime() - emissione.getTime();
        Double result = validitaValue.doubleValue()/SECONDS_IN_A_DAY;
        return (int)Math.round(result);
    }

    public static Date getDate(int giorno, int mese, int anno) throws ValidationException{
        Calendar instance = GregorianCalendar.getInstance();
        instance.clear();
        instance.set(anno, mese-1, giorno);
        if (instance.get(Calendar.YEAR)!=anno || instance.get(Calendar.MONTH)!=mese-1 || instance.get(Calendar.DAY_OF_MONTH)!=giorno){
            throw new ValidationException("Errore", "Data non valida");
        }
        return instance.getTime();
    }

    public static Date calcolaScadenza(Date date, int scadenza){
        Calendar c = toGregorianCalendar(date);
        c.add(Calendar.DAY_OF_YEAR, scadenza);
        return c.getTime();
    }

    public static Date parseDate(String date) throws ParseException{
        try {
            return new SimpleDateFormat(STANDARD_FORMAT).parse(date);
        } catch (ParseException ex) {}
        return new SimpleDateFormat("dd-MM-yyyy").parse(date);
    }

    public static Date parseDateAndTime(String date) throws ParseException{
        try {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date);
        } catch (ParseException ex) {}
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
    }

    public static String formatDate(Date d){
        return new SimpleDateFormat(STANDARD_FORMAT).format(d);
    }
    
    public static String formatHQLDate(Date d){
    	return new SimpleDateFormat(HQL_FORMAT).format(d);
    }
    
    public static Date getFirstDayOfMonth(Date d){
    	Calendar instance = toGregorianCalendar(d);
    	instance.set(Calendar.DAY_OF_MONTH, 1);
    	return instance.getTime();
    }

    public static Date getFatturaDay(Date d){
        Calendar instance = toGregorianCalendar(d);
        instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
        int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
        while (dayOfWeek==Calendar.SUNDAY || dayOfWeek==Calendar.SATURDAY){
            instance.set(Calendar.DAY_OF_MONTH, instance.get(Calendar.DAY_OF_MONTH)-1);
            dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
        }
        return instance.getTime();
    }

    public static int getDay(Date d){
        return getDateInfo(d, Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date d){
        return getDateInfo(d, Calendar.MONTH)+1;
    }

    public static String getMonthString(Date d){
        return parseMonthInt(getMonth(d));
    }

    public static int parseMonthString(String month){
        int result=0;
            if( month.equals("Gennaio"))  result = 1;
            if( month.equals("Febbraio"))  result = 2;
            if( month.equals("Marzo"))  result = 3;
            if( month.equals("Aprile"))  result = 4;
            if( month.equals("Maggio"))  result = 5;
            if( month.equals("Giugno"))  result = 6;
            if( month.equals("Luglio"))  result = 7;
            if( month.equals("Agosto"))  result = 8;
            if( month.equals("Settembre"))  result = 9;
            if( month.equals("Ottobre")) result = 10;
            if( month.equals("Novembre")) result = 11;
            if( month.equals("Dicembre")) result = 12;
        return result;
    }

    public static String parseMonthInt(int month){
        String result=null;
        switch (month) {
            case 1:  result = "Gennaio";       break;
            case 2:  result = "Febbraio";      break;
            case 3:  result = "Marzo";         break;
            case 4:  result = "Aprile";        break;
            case 5:  result = "Maggio";        break;
            case 6:  result = "Giugno";        break;
            case 7:  result = "Luglio";        break;
            case 8:  result = "Agosto";        break;
            case 9:  result = "Settembre";     break;
            case 10: result = "Ottobre";       break;
            case 11: result = "Novembre";      break;
            case 12: result = "Dicembre";      break;
        }
        return result;
    }

    public static int getYear(Date d){
        return getDateInfo(d, Calendar.YEAR);
    }

	public static GregorianCalendar toGregorianCalendar(Date date) {
		GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
        c.setTime(date);
		return c;
	}
    
    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date) throws DatatypeConfigurationException {
    	GregorianCalendar gregorianCalendar = toGregorianCalendar(date);
    	DatatypeFactory newInstance = DatatypeFactory.newInstance();
		return newInstance.newXMLGregorianCalendar(gregorianCalendar);
    }
	
    private static int getDateInfo(Date d, int field){
        Calendar c = toGregorianCalendar(d);
        return c.get(field);
    }

    public static Comparator<String> getMonstStringComparator(){
        return new Comparator<String>() {

        	public int compare(String o1, String o2) {
                Integer parseMonthString = parseMonthString(o1);
                return parseMonthString.compareTo(parseMonthString(o2));
            }
        };
    }
    
}
