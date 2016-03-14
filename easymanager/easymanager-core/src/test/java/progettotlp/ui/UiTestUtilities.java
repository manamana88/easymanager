/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vincenzo
 */
public class UiTestUtilities {
    public static void initializeClass(Object toInitialize, List<Class<? extends Object>> ignore){
        Class<? extends Object> clazz = toInitialize.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field f : declaredFields){
            Class<?> fieldType = f.getType();
            if (ignore.contains(fieldType)){
                continue;
            }
            try{
                Constructor<?> fieldConstructor = fieldType.getConstructor();
                f.set(toInitialize, fieldConstructor.newInstance());
            } catch (Exception e){
            }
        }
    }

    public static void initializeClass(Object toInitialize){
        initializeClass(toInitialize,new ArrayList<Class<? extends Object>>());
    }
}
