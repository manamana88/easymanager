/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.Date;
import java.util.Map;
import java.util.Arrays;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import progettotlp.facilities.DateUtils;
import progettotlp.statistiche.StatisticheConfronto;
import progettotlp.statistiche.StatisticheFattura;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class StatisticheManagerImplFunctionalTest extends AbstractTest{
    protected StatisticheManagerImpl statisticheManagerImpl;

    @Before
    public void setup(){
        if (statisticheManagerImpl!=null){
            statisticheManagerImpl.close();
        }
        statisticheManagerImpl=new StatisticheManagerImpl(properties);
    }

    @Test
    public void simpleSearchTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareStatisticheFunctionalTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        Map<Date, List<StatisticheFattura>> simpleSearch = statisticheManagerImpl.simpleSearch(DateUtils.parseDate("01/01/2012"), DateUtils.parseDate("01/02/2012"), null);
        assertEquals(0,simpleSearch.size());
        simpleSearch = statisticheManagerImpl.simpleSearch(DateUtils.parseDate("30/03/2012"), DateUtils.parseDate("01/04/2012"), null);
        assertEquals(2,simpleSearch.size());
        List<StatisticheFattura> firstDateStatistics = simpleSearch.get(DateUtils.parseDate("01/04/2012"));
        List<StatisticheFattura> secondDateStatistics = simpleSearch.get(DateUtils.parseDate("30/03/2012"));
        assertNotNull(firstDateStatistics);
        assertEquals(1, firstDateStatistics.size());
        assertNotNull(secondDateStatistics);
        assertEquals(2, secondDateStatistics.size());
        for (StatisticheFattura s : firstDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 3:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("100"),new BigDecimal("21"),new BigDecimal("121"), new BigDecimal("15"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
        for (StatisticheFattura s : secondDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 1:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("300"),new BigDecimal("63"),new BigDecimal("363"), new BigDecimal("40"));
                    break;
                case 2:
                    assertEqualsStatistiche(s, "azienda1", new BigDecimal("200"),new BigDecimal("42"),new BigDecimal("242"), new BigDecimal("25"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
        simpleSearch = statisticheManagerImpl.simpleSearch(DateUtils.parseDate("30/03/2012"), DateUtils.parseDate("01/04/2012"), Arrays.asList("azienda2"));
        assertEquals(2,simpleSearch.size());
        firstDateStatistics = simpleSearch.get(DateUtils.parseDate("30/03/2012"));
        secondDateStatistics = simpleSearch.get(DateUtils.parseDate("01/04/2012"));
        assertNotNull(firstDateStatistics);
        assertEquals(1, firstDateStatistics.size());
        assertNotNull(secondDateStatistics);
        assertEquals(1, secondDateStatistics.size());
        for (StatisticheFattura s : firstDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 1:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("300"),new BigDecimal("63"),new BigDecimal("363"), new BigDecimal("40"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
        for (StatisticheFattura s : secondDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 3:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("100"),new BigDecimal("21"),new BigDecimal("121"), new BigDecimal("15"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
    }

    @Test
    public void advancedSearchTest() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareStatisticheFunctionalTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        StatisticheConfronto advancedSearch = statisticheManagerImpl.advancedSearch(DateUtils.parseDate("01/01/2012"), DateUtils.parseDate("01/02/2012"), DateUtils.parseDate("01/03/2010"), DateUtils.parseDate("30/03/2010"), null);
        assertEquals(0,advancedSearch.getStatistichePrimoPeriodo().size());
        assertEquals(0,advancedSearch.getStatisticheSecondoPeriodo().size());
        /*********************FIRST SEARCH************************************/
        advancedSearch = statisticheManagerImpl.advancedSearch(DateUtils.parseDate("30/03/2012"), DateUtils.parseDate("01/04/2012"), DateUtils.parseDate("01/03/2011"), DateUtils.parseDate("30/03/2011"), null);
        Map<Date, List<StatisticheFattura>> statistichePrimoPeriodo = advancedSearch.getStatistichePrimoPeriodo();
        Map<Date, List<StatisticheFattura>> statisticheSecondoPeriodo = advancedSearch.getStatisticheSecondoPeriodo();
        assertEquals(2,statistichePrimoPeriodo.size());
        assertEquals(1,statisticheSecondoPeriodo.size());
        
                        /********CHECK PRIMO PERIODO*********/
        List<StatisticheFattura> firstDateStatistics = statistichePrimoPeriodo.get(DateUtils.parseDate("30/03/2012"));
        List<StatisticheFattura> secondDateStatistics = statistichePrimoPeriodo.get(DateUtils.parseDate("01/04/2012"));
        assertNotNull(firstDateStatistics);
        assertEquals(2, firstDateStatistics.size());
        assertNotNull(secondDateStatistics);
        assertEquals(1, secondDateStatistics.size());
        for (StatisticheFattura s : firstDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 1:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("300"),new BigDecimal("63"),new BigDecimal("363"), new BigDecimal("40"));
                    break;
                case 2:
                    assertEqualsStatistiche(s, "azienda1", new BigDecimal("200"),new BigDecimal("42"),new BigDecimal("242"), new BigDecimal("25"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
        for (StatisticheFattura s : secondDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 3:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("100"),new BigDecimal("21"),new BigDecimal("121"), new BigDecimal("15"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
                        /********CHECK SECONDO PERIODO*********/
        List<StatisticheFattura> statistiche = statisticheSecondoPeriodo.get(DateUtils.parseDate("30/03/2011"));
        assertNotNull(statistiche);
        for (StatisticheFattura s : statistiche){
            Integer id = s.getId();
            switch (id){
                case 1:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("400"),new BigDecimal("84"),new BigDecimal("484"), new BigDecimal("0"));
                    break;
                default:
                    fail("Unexpected id");
            }
        }

        /*********************SECOND SEARCH************************************/
        advancedSearch = statisticheManagerImpl.advancedSearch(DateUtils.parseDate("30/03/2012"), DateUtils.parseDate("01/04/2012"), DateUtils.parseDate("01/03/2011"), DateUtils.parseDate("30/03/2011"), Arrays.asList("azienda2"));
        statistichePrimoPeriodo = advancedSearch.getStatistichePrimoPeriodo();
        statisticheSecondoPeriodo = advancedSearch.getStatisticheSecondoPeriodo();
        assertEquals(2,statistichePrimoPeriodo.size());
        assertEquals(1,statisticheSecondoPeriodo.size());

                        /********CHECK PRIMO PERIODO*********/
        firstDateStatistics = statistichePrimoPeriodo.get(DateUtils.parseDate("30/03/2012"));
        secondDateStatistics = statistichePrimoPeriodo.get(DateUtils.parseDate("01/04/2012"));
        assertNotNull(firstDateStatistics);
        assertEquals(1, firstDateStatistics.size());
        assertNotNull(secondDateStatistics);
        assertEquals(1, secondDateStatistics.size());
        for (StatisticheFattura s : firstDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 1:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("300"),new BigDecimal("63"),new BigDecimal("363"), new BigDecimal("40"));
                    break;
                case 2:
                    assertEqualsStatistiche(s, "azienda1", new BigDecimal("200"),new BigDecimal("42"),new BigDecimal("242"), new BigDecimal("25"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
        for (StatisticheFattura s : secondDateStatistics){
            Integer id = s.getId();
            switch (id){
                case 3:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("100"),new BigDecimal("21"),new BigDecimal("121"), new BigDecimal("15"));
                    break;
                default:
                    fail("Unexpected id: "+id);
            }
        }
                        /********CHECK SECONDO PERIODO*********/
        statistiche = statisticheSecondoPeriodo.get(DateUtils.parseDate("30/03/2011"));
        assertNotNull(statistiche);
        for (StatisticheFattura s : statistiche){
            Integer id = s.getId();
            switch (id){
                case 1:
                    assertEqualsStatistiche(s, "azienda2", new BigDecimal("400"),new BigDecimal("84"),new BigDecimal("484"), new BigDecimal("0"));
                    break;
                default:
                    fail("Unexpected id");
            }
        }
    }

    private void assertEqualsStatistiche(StatisticheFattura s, String nome, BigDecimal netto, BigDecimal iva, BigDecimal totale, BigDecimal capiTagliati){
        assertEquals(nome, s.getCliente().getNome());
        assertTrue(netto.compareTo(s.getNetto()) == 0);
        assertTrue(iva.compareTo(s.getIva()) == 0);
        assertTrue(totale.compareTo(s.getTotale()) == 0);
        assertTrue(capiTagliati.compareTo(s.getCapiTagliati()) == 0);
    }
}
