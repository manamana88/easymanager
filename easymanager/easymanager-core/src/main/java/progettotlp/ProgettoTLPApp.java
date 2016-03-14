/*
 * ProgettoTLPApp.java
 */

package progettotlp;

import java.util.Properties;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import progettotlp.facilities.ConfigurationManager;

/**
 * The main class of the application.
 */
public class ProgettoTLPApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {

        ConfigurationManager.init();
        // Inizializza l'interfaccia grafica dell'applicazione
        ProgettoTLPView main=new ProgettoTLPView(this,null);
        show(main);
    }
    
   protected void startup(Properties p){

        ConfigurationManager.init();
       ProgettoTLPView main=new ProgettoTLPView(this,p);
       show(main);
   }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our applica
        ProgettoTLPApp p = new ProgettoTLPApp();tion come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ProgettoTLPApp
     */
    public static ProgettoTLPApp getApplication() {
        return Application.getInstance(ProgettoTLPApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
            launch(ProgettoTLPApp.class, args);
    }
}