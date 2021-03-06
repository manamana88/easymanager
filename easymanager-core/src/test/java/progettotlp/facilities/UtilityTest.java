/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.facilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import progettotlp.Constants;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.NotSameClassException;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.print.BeneFattura;

/**
 *
 * @author Vincenzo
 */
public class UtilityTest {

    @Test
    public void getSelectedAnnoTest(){
    	System.clearProperty(Constants.CURRENT_YEAR_PROPERTY);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Assert.assertEquals(year, Utility.getSelectedAnno());
        System.setProperty(Constants.CURRENT_YEAR_PROPERTY, ""+2010);
        Assert.assertEquals(2010, Utility.getSelectedAnno());
    }
    @Test
    public void capitalizeStringTest(){
        Assert.assertEquals("Ciao", Utility.capitalizeString("ciao"));
        Assert.assertEquals("Ciao", Utility.capitalizeString("Ciao"));
        Assert.assertEquals("CIao", Utility.capitalizeString("CIao"));
    }
    @Test
    public void getGetterMethodNameTest(){
        Assert.assertEquals("getCiao", Utility.getGetterMethodName("ciao",false));
        Assert.assertEquals("getCiao", Utility.getGetterMethodName("Ciao",false));
        Assert.assertEquals("getCIao", Utility.getGetterMethodName("CIao",false));
    }
    @Test
    public void getSetterMethodNameTest(){
        Assert.assertEquals("setCiao", Utility.getSetterMethodName("ciao"));
        Assert.assertEquals("setCiao", Utility.getSetterMethodName("Ciao"));
        Assert.assertEquals("setCIao", Utility.getSetterMethodName("CIao"));
    }
    @Test
    public void getSetterMethodTest() throws NoSuchMethodException{
        Class<?> testClass=ReflectionClassTest.class;
        Assert.assertNotNull(Utility.getSetterMethod(testClass, "id", Integer.class));
        Assert.assertNotNull(Utility.getSetterMethod(testClass, "ok", Boolean.class));
        Assert.assertNotNull(Utility.getSetterMethod(testClass, "hidden", Boolean.class));
        try{
            Utility.getSetterMethod(testClass, "id", Double.class);
            Assert.fail();
        } catch (NoSuchMethodException e){}
        try{
            Utility.getSetterMethod(testClass, "falsaProperty");
            Assert.fail();
        } catch (NoSuchMethodException e){}
    }
    @Test
    public void getGetterMethodTest() throws NoSuchMethodException{
        Class<?> testClass=ReflectionClassTest.class;
        Assert.assertNotNull(Utility.getGetterMethod(testClass, "id"));
        Assert.assertNotNull(Utility.getGetterMethod(testClass, "ok"));
        Assert.assertNotNull(Utility.getGetterMethod(testClass, "hidden"));
        try{
            Utility.getGetterMethod(testClass, "id", Double.class);
            Assert.fail();
        } catch (NoSuchMethodException e){}
        try{
            Utility.getGetterMethod(testClass, "falsaProperty");
            Assert.fail();
        } catch (NoSuchMethodException e){}
    }
    @Test
    public void copyPropertiesTest() throws NotSameClassException{
        ReflectionClassTest source= new ReflectionClassTest(true);
        ReflectionClassTest target=new ReflectionClassTest(false);
        ArrayList<String> ignored = new ArrayList<String>();
        source.setId(1);
        source.setOk(true);
        Utility.copyProperties(source, target, ignored);
        Assert.assertEquals(source.getId(), target.getId());
        Assert.assertEquals(source.getOk(), target.getOk());
        Assert.assertFalse(source.getHidden().equals(target.getHidden()));
        ignored.add("id");
        target=new ReflectionClassTest(false);
        Utility.copyProperties(source, target, ignored);
        Assert.assertFalse(source.getId().equals(target.getId()));
        Assert.assertEquals(source.getOk(), target.getOk());
        Assert.assertFalse(source.getHidden().equals(target.getHidden()));
        try{
            Utility.copyProperties(source, new Integer(1), ignored);
            Assert.fail();
        } catch (NotSameClassException e){}
    }
    @Test
    public void getTotCapiTest(){
        BeneInterface b1=new Bene();
        b1.setQta(new BigDecimal("1"));
        BeneInterface b2=new Bene();
        b2.setQta(new BigDecimal("3"));
        BeneInterface b3=new Bene();
        b3.setQta(new BigDecimal("15"));
        DdTInterface d1=new DdT();
        d1.setBeni(Arrays.asList(b1));
        DdTInterface d2=new DdT();
        d2.setBeni(Arrays.asList(b2,b3));
        Assert.assertEquals(new BigDecimal("19"),Utility.getTotCapi(Arrays.asList(d1,d2)));
    }
    @Test
    public void mapDdTTest(){
        ArrayList<DdTInterface> lst= generateDdTList(10,10);
        Map<Integer, Map<Long, BeneInterface>> mapDdT = Utility.mapDdT(lst);
        Assert.assertEquals(10, mapDdT.size());
        for (Entry<Integer, Map<Long, BeneInterface>> entry : mapDdT.entrySet()){
            Integer key = entry.getKey();
            Assert.assertTrue(key>=0 && key<10);
            Map<Long, BeneInterface> value = entry.getValue();
            Assert.assertEquals(10, value.size());
            for (Entry<Long,BeneInterface> entry2:value.entrySet()){
                Assert.assertEquals(entry2.getKey(),entry2.getValue().getId());
            }
        }
    }

    @Test
    public void getPagineBeniTest(){
        ArrayList<DdTInterface> lst = generateDdTList(10,10);
        Map<Integer, List<BeneFattura>> pagineBeni = Utility.getPagineBeni(lst);
        Assert.assertEquals(4, pagineBeni.size());
        for (Entry<Integer, List<BeneFattura>> entry:pagineBeni.entrySet()){
            Integer key = entry.getKey();
            int limit=-1;
            if (key<4 && key>0){
                limit=30;
            } else if (key.intValue()==4){
                limit=10;
            } else {
                Assert.fail();
            }
            Assert.assertEquals(limit, entry.getValue().size());
        }
        lst = generateDdTList(9,10);
        pagineBeni = Utility.getPagineBeni(lst);
        Assert.assertEquals(3, pagineBeni.size());
        for (Entry<Integer, List<BeneFattura>> entry:pagineBeni.entrySet()){
            Integer key = entry.getKey();
            if (key<4 && key>0){
                Assert.assertEquals(30, entry.getValue().size());
            } else {
                Assert.fail();
            }
        }
    }

    private ArrayList<DdTInterface> generateDdTList(int ddtNumber, int beniForDdTNumber) {
        ArrayList<DdTInterface> lst = new ArrayList<>();
        for (int i = 0; i < ddtNumber; i++) {
            ArrayList<BeneInterface> lstBene = new ArrayList<>();
            for (int j = 0; j < beniForDdTNumber; j++) {
                Bene bene = new Bene();
                bene.setId(new Long(j));
                lstBene.add(bene);
            }
            DdT d1 = new DdT();
            d1.setBeni(lstBene);
            d1.setId(i);
            lst.add(d1);
        }
        return lst;
    }

}
