/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.text.ParseException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Before;
import javax.swing.JDialog;
import org.junit.Test;
import progettotlp.facilities.DateUtils;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormStatisticheRisultatiUtilitiesTest {
    FormStatisticheRisultatiUtilities form;

    public FormStatisticheRisultatiUtilitiesTest() {
         form = new FormStatisticheRisultatiUtilities(new JDialog());
    }

    @Before
    public void setUp(){
        UiTestUtilities.initializeClass(form, new ArrayList<Class<? extends Object>>());
    }

    @Test
    public void testMergeKeys() throws ParseException {
        assertEquals(0, form.aggregateKeys(null, null).size());

        Date firstMarch = DateUtils.parseDate("01/03/2012");
        Date firstMarchDifferentYear = DateUtils.parseDate("01/03/2011");
        Date secondMarch = DateUtils.parseDate("02/03/2012");
        Date thirdMarch = DateUtils.parseDate("03/03/2012");
        Date firstApril = DateUtils.parseDate("01/04/2012");
        Set<Date> firstSet = new HashSet<Date>();
        Set<Date> secondSet = new HashSet<Date>();

        assertEquals(0,form.aggregateKeys(firstSet,secondSet).size());
        firstSet.add(firstMarch);
        secondSet.add(firstMarch);
        assertEquals(1,form.aggregateKeys(firstSet,secondSet).size());
        firstSet.add(thirdMarch);
        secondSet.add(secondMarch);
        assertEquals(1, form.aggregateKeys(firstSet, secondSet).size());
        /*********************FIRST REAL TEST******************************/
        firstSet.add(firstApril);

        TreeMap<String, TreeSet<Date>> mergeKeys = form.aggregateKeys(firstSet, secondSet);
        assertEquals(2, mergeKeys.size());
        
        NavigableSet<String> navigableKeySet = mergeKeys.navigableKeySet();
        Iterator<String> iterator = navigableKeySet.iterator();
        String next = iterator.next();
        assertEquals("Marzo", next);
        TreeSet<Date> get = mergeKeys.get(next);
        assertEquals(3,get.size());
        Iterator<Date> treeSetIterator = get.iterator();
        assertEquals(firstMarch, treeSetIterator.next());
        assertEquals(secondMarch, treeSetIterator.next());
        assertEquals(thirdMarch, treeSetIterator.next());

        next = iterator.next();
        assertEquals("Aprile", next);
        get = mergeKeys.get(next);
        assertEquals(1,get.size());
        treeSetIterator = get.iterator();
        assertEquals(firstApril, treeSetIterator.next());
        /*********************SECOND REAL TEST******************************/
        firstSet = new HashSet<Date>();
        secondSet = new HashSet<Date>();
        firstSet.add(firstApril);
        firstSet.add(firstMarch);
        secondSet.add(firstMarchDifferentYear);

        mergeKeys = form.aggregateKeys(firstSet, secondSet);
        assertEquals(2, mergeKeys.size());

        navigableKeySet = mergeKeys.navigableKeySet();
        iterator = navigableKeySet.iterator();
        next = iterator.next();
        assertEquals("Marzo", next);
        get = mergeKeys.get(next);
        assertEquals(2,get.size());
        treeSetIterator = get.iterator();
        assertEquals(firstMarchDifferentYear, treeSetIterator.next());
        assertEquals(firstMarch, treeSetIterator.next());

        next = iterator.next();
        assertEquals("Aprile", next);
        get = mergeKeys.get(next);
        assertEquals(1,get.size());
        treeSetIterator = get.iterator();
        assertEquals(firstApril, treeSetIterator.next());
    }

    @Test
    public void testCompilaRisultati() {
    }

}