package progettotlp.print;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.exceptions.PrintException;

/**
 *
 * @author Giuseppe Della Penna
 */
public class TemplateResult {

    protected Configuration cfg;
    
    private Logger logger = LoggerFactory.getLogger(TemplateResult.class);

    public TemplateResult() throws PrintException {
        init();
    }

    private void init() throws PrintException {
        try{
            cfg = new Configuration();
            File f=new File("C:"+File.separator+"TPL");
            System.out.println(f.exists());
            System.out.println(f.isDirectory());
            cfg.setDirectoryForTemplateLoading(f);
            //impostiamo il gestore degli oggetti - trasformer� in data model i Java beans
            cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
            //impostiamo un handler per gli errori nei template - utile per il debug
            cfg.setTemplateExceptionHandler(new ConsoleTemplateExceptionHandler());
        } catch (Throwable e){
            throw new PrintException("Unable to init: ", e);
        }
    }

    //questo metodo principale si occupa di chiamare Freemarker e compilare il template
    //se � stato specificato un template di outline, quello richiesto viene inserito
    //all'interno dell'outline
    private void process(String tplname, Map datamodel, Writer out) throws IOException, TemplateException{
        Template t;
        //assicuriamoci di avere sempre un data model da passare al template, che contenga anche tutti i default
        Map<String, Object> localdatamodel = new HashMap();
        //nota: in questo modo il data model utente pu� eventualmente sovrascrivere i dati precaricati da getDefaultDataModel
        //ad esempio per disattivare l'outline template basta porre a null la rispettiva chiave

        if (datamodel != null) {
            localdatamodel.putAll(datamodel);
        }
        String outline_name = (String) localdatamodel.get("outline_tpl");
        if (outline_name == null || outline_name.isEmpty()) {
            //se non c'� un outline, carichiamo semplicemente il template specificato
            t = cfg.getTemplate(tplname);
        } else {
            //un template di outline � stato specificato: il template da caricare � quindi sempre l'outline...
            t = cfg.getTemplate(outline_name);
            //...e il template specifico per questa pagina viene indicato all'outline tramite una variabile content_tpl
            localdatamodel.put("content_tpl", tplname);
            //si suppone che l'outline includa questo secondo template
        }
        //Freemarker: associamo i dati al template e lo mandiamo in output
        t.process(localdatamodel, out);
    }

    //questa versione di activate pu� essere usata per generare output non diretto verso il browser, ad esempio
    //su un file
    public void activate(String tplname, Map datamodel, OutputStream out) throws PrintException{
        //impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            //encoding = cfg.getDefaultEncoding();
            encoding="ISO-8859-1";
        }
        try {
            //notare la gestione dell'encoding, che viene invece eseguita implicitamente tramite il setContentType nel contesto servlet
            process(tplname, datamodel, new OutputStreamWriter(out, encoding));
        } catch (Throwable ex) {
            throw new PrintException("Unable to activate", ex);
        }
    }
    
}
