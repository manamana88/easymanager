/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.ProgettoTLPAboutBox;
import progettotlp.ProgettoTLPApp;
import progettotlp.exceptions.InitializationException;

/**
 *
 * @author vincenzo
 */
public class GeneralUtilities {
    public static final Logger logger = LoggerFactory.getLogger(GeneralUtilities.class);

    public static void showAboutBox(JDialog aboutBox) {
        if (aboutBox == null) {
            JFrame mainFrame = ProgettoTLPApp.getApplication().getMainFrame();
            aboutBox = new ProgettoTLPAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ProgettoTLPApp.getApplication().show(aboutBox);
    }
    
    public static Component getElementInDialog(JDialog container, String name){
        Component[] components = container.getContentPane().getComponents();
        for (Component component : components){
            if (name.equals(component.getName()))
                return component;
        }
        return null;
    }
    
    public static void areFieldsNull(Object toInitialize) throws InitializationException{
        try{
            Class<? extends Object> clazz = toInitialize.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields){
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (!isPrivate){
                    if (field.get(toInitialize) == null){
                        throw new InitializationException(field.getName()+" is null");
                    }
                }
            }
        } catch (InitializationException e){
            throw e;
        } catch (Throwable e){
            throw new InitializationException("unable to initialize object "+toInitialize.getClass().getSimpleName(),e);
        }
    }
    
    public static void initializeClassFromJDialog(Object toInitialize, Container container) throws InitializationException{
        Class<? extends Object> clazz = toInitialize.getClass();
        Component[] components = container.getComponents();
        for (Component component : components){
            Field field=null;
            try {
                String name = component.getName();
                if (name!=null){
                    field = clazz.getDeclaredField(name);
                }
            } catch (NoSuchFieldException ex) {
            } catch (SecurityException ex) {
            }
            if (field!=null){
                try {
                    field.set(toInitialize, component);
                } catch (Exception ex) {
                    logger.debug(ex.getMessage());
                    throw new InitializationException("Unable to initializeClass",ex);
                }
            }
            if (component instanceof Container){
                initializeClassFromJDialog(toInitialize, (Container)component);
            }
        }
    }
}
