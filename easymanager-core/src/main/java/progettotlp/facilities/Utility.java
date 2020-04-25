/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.facilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import progettotlp.Constants;
import progettotlp.exceptions.NotSameClassException;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.print.BeneFattura;
import progettotlp.rest.resources.FatturaResource;

/**
 *
 * @author Vincenzo
 */
public class Utility {


    public static int getSelectedAnno(){
        String property = System.getProperty(Constants.CURRENT_YEAR_PROPERTY);
        return property==null || property.trim().isEmpty()?GregorianCalendar.getInstance().get(Calendar.YEAR):Integer.parseInt(property);
    }
    /**
     * Metodo per convertire un valore di tipo <nulltype> in un valore di tipo <boolean>
     * @param x
     * @return Il valore passato convertito in boolean.
     */
    public static boolean convertNullToBoolean(Object x){
        if (x==null) return false;
        else if(x.getClass().getName().indexOf("Boolean")>-1){
                    if (((Boolean) x).compareTo(true)==0){
                        return true;
                    }
                }
        return false;
    }
    
    /**
     * Metodo per convertire un valore di tipo <nulltype> in un valore di tipo <string>
     * @param x
     * @return Il valore passato convertito in String.
     */
    public static String convertNullToString(Object x){
        return x==null?"":x.toString();
    }

    public static Float convertStringToFloat(Object x){
        if (x==null)
            return 0F;
        if ("String".equals(x.getClass().getSimpleName())){
            String xString=(String)x;
            if (xString.isEmpty())
                return 0F;
            else
                return Float.parseFloat(xString);
        }
        return (Float)x;
    }
    
    /**
     * Metodo per controllare se la riga di una tabella sia vuota o meno.
     * @param x
     * @param riga
     * @return true se la riga in questione e vuota, false altrimenti.
     */
    public static boolean isRigaVuota(JTable x, int riga){
        for(int i=0;i<x.getColumnCount();i++){
            if (x.getValueAt(riga, i)!=null){
                if (x.getValueAt(riga, i).getClass().equals("".getClass())){
                    if (!x.getValueAt(riga, i).equals("")){
                        return false;
                    }
                }
                if(x.getValueAt(riga, i).getClass().getName().indexOf("Boolean")>-1){
                    if (((Boolean)x.getValueAt(riga, i)).compareTo(Boolean.TRUE) == 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public static Map<Integer,Map<Long,BeneInterface>> mapDdT(List<DdTInterface> list){
        Map<Integer,Map<Long,BeneInterface>> res=new HashMap<>();
        for (DdTInterface d : list){
            Map<Long,BeneInterface> mappingBene=new HashMap<>();
            for (BeneInterface bene:d.getBeni()){
                mappingBene.put(bene.getId(), bene);
            }
            res.put(d.getId(), mappingBene);
        }
        return res;
    }
    
    public static Map<Integer,List<BeneFattura>> getPagineBeni(List<DdTInterface> lista){
        Map<Integer,List<BeneFattura>> res=new HashMap<Integer,List<BeneFattura>>();
        Integer page=1;
        List<BeneFattura> realList=new ArrayList<BeneFattura>();
        Iterator<DdTInterface> itDdT=lista.iterator();
        while(itDdT.hasNext()){
            List<BeneFattura> tempList=new ArrayList<BeneFattura>();
            DdTInterface curr=itDdT.next();
            Iterator<BeneInterface> itBeni=curr.getBeni().iterator();
            while (itBeni.hasNext()){
                tempList.add(new BeneFattura(itBeni.next(), curr.getData(),curr.getId()));
            }
            if (realList.size()+tempList.size()<FatturaResource.ROWS_PER_PAGE){
                realList.addAll(tempList);
            } else {
                res.put(page++, realList);
                realList=new ArrayList<BeneFattura>(tempList);
            }
            tempList=new ArrayList<BeneFattura>();
        } 
        res.put(page++, realList);
        return res;
    }
    
    public static BigDecimal getTotCapi(List<DdTInterface> list){
    	BigDecimal tot=new BigDecimal("0");
        Iterator<DdTInterface> itDdT=list.iterator();
        while(itDdT.hasNext()){
            DdTInterface curr=itDdT.next();
            Iterator<BeneInterface> itBeni=curr.getBeni().iterator();
            while (itBeni.hasNext()){
                tot = tot.add(itBeni.next().getQta());
            }
        }
        return tot;
    }
    public static void copyProperties(Object reference, Object toModify, List<String> ignored) throws NotSameClassException{
        Class<? extends Object> referenceClass = reference.getClass();
        Class<? extends Object> toModifyClass = toModify.getClass();
        if (!referenceClass.equals(toModifyClass)){
            throw new NotSameClassException("Expected class "+referenceClass.getCanonicalName()+" instead "+toModifyClass.getCanonicalName());
        }
        Field[] declaredFields = referenceClass.getDeclaredFields();
        copyProperties(declaredFields, ignored, referenceClass, toModifyClass, toModify, reference);
        Field[] fields = referenceClass.getFields();
        copyProperties(fields, ignored, referenceClass, toModifyClass, toModify, reference);
    }
    protected static String capitalizeString(String string){
        return string.substring(0, 1).toUpperCase()+string.substring(1);
    }
    protected static String getSetterMethodName(String propertyName){
        return "set"+capitalizeString(propertyName);
    }
    protected static String getGetterMethodName(String propertyName, boolean isBoolean){
        String prefix = "get";
        return prefix+capitalizeString(propertyName);
    }
    protected static Method getSetterMethod(Class<?> objClass, String propertyName, Class<?>... parameterTypes) throws NoSuchMethodException{
        try{
            return objClass.getDeclaredMethod(getSetterMethodName(propertyName), parameterTypes);
        } catch (NoSuchMethodException e){
            return objClass.getMethod(getSetterMethodName(propertyName), parameterTypes);
        }
    }
    protected static Method getGetterMethod(Class<?> objClass, String propertyName, Class<?>... parameterTypes) throws NoSuchMethodException{
        try{
            try{    
                return objClass.getDeclaredMethod(getGetterMethodName(propertyName, false), parameterTypes);
            } catch (NoSuchMethodException e){
                return objClass.getDeclaredMethod(getGetterMethodName(propertyName, true), parameterTypes);
            }
        } catch (NoSuchMethodException e){
            try{
                return objClass.getMethod(getGetterMethodName(propertyName, false), parameterTypes);
            } catch (NoSuchMethodException e2){
                return objClass.getMethod(getGetterMethodName(propertyName, true), parameterTypes);
            }
        }
    }
    protected static void copyProperties(Field[] declaredFields, List<String> ignored, Class<? extends Object> toModifyClass, Class<? extends Object> referenceClass, Object toModify, Object reference){
        for(Field field: declaredFields){
            try{
                if (ignored.contains(field.getName())){
                    continue;
                }
                boolean isBoolean = field.getType().equals(Boolean.class) || field.getType().equals(boolean.class);
                Method setter = toModifyClass.getMethod(getSetterMethodName(field.getName()), field.getType());
                Method getter = referenceClass.getMethod(getGetterMethodName(field.getName(),isBoolean));
                setter.invoke(toModify, getter.invoke(reference));
            } catch (Exception e){}
        }
    }
}
