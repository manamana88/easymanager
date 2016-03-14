/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import progettotlp.persistenza.AziendaManager;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.Controlli;
import progettotlp.facilities.Conversioni;
import progettotlp.facilities.Utility;

/**
 *
 * @author vincenzo
 */
public class FormAziendaUtilities extends AbstractFormUtilities{
    
    public static enum FormAziendaType{REGISTRATION,NEW_AZIENDA,MODIFICA, VISUALIZZA};
    
    protected JCheckBox giuridico;
    protected JCheckBox tassabile;
    protected JTextField aziendaId;
    protected JTextField iva; 
    protected JTextField cod_fis;
    protected JTextField nomeAzienda;
    protected JTextField email;
    protected JTextField via;
    protected JTextField civico;
    protected JTextField cap;
    protected JTextField citta;
    protected JTextField provincia;
    protected JTextField nazione;
    protected JTextField telefono;
    protected JTextField fax;
    protected JTextField aziendaPrincipale;
    protected JButton annulla;
    protected JButton esci;
    protected JButton registra;
    protected JButton bottoneOk;
    protected JButton modificaAzienda;
    
    protected AziendaManager aziendaManager;
    
    public FormAziendaUtilities(JDialog formAzienda, AziendaManager aziendaManager) {
        this.aziendaManager=aziendaManager;
        init(formAzienda);
    }
    
    public void ivaCopiaPIva() { 
        if (giuridico.isSelected())
            cod_fis.setText(iva.getText());
    }     
    
    public void giuridicoAbilita() {
        if (giuridico.isSelected()){
            String ivaText = iva.getText();
            if (ivaText!=null && !ivaText.trim().isEmpty())
                cod_fis.setText(ivaText);
            cod_fis.setEnabled(false);
        } else cod_fis.setEnabled(true);
    }
    
    public void esciSenzaSalvareAzienda(){
        // Handler per l'uscita dall'applicazione senza aver registrato l'Azienda principale
        Object[] options = {"Annulla","Si"};
        int n = JOptionPane.showOptionDialog(form,
            "Per utilizzare questo programma occorre inserire i dati della vostra azienda. Siete sicuri di voler uscire?",
            "Uscita dal programma",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[1]);
        if (n==1){
            System.exit(0);
        }
    }
    
    /**
     * Metodo che esegue dei controlli sulla correttezza dei dati inseriti al 
     * momento della registrazione di un'Azienda nel sistema.
     * @return 
     */
    public Azienda caricaAziendaDaForm() throws ValidationException{
        Azienda res=new Azienda();
        String aziendaIdText = aziendaId.getText();
        Long id=null;
        if (aziendaIdText == null || aziendaIdText.trim().isEmpty()){
            res.setId(null);
        } else {
            id=Long.parseLong(aziendaIdText);
        }
        res.setId(id);
        String aziendaPrincipaleText=aziendaPrincipale.getText();
        Boolean isPrincipale = null;
        if (aziendaPrincipaleText==null || aziendaPrincipaleText.trim().isEmpty()){
            isPrincipale=false;
        } else {
            isPrincipale=Boolean.parseBoolean(aziendaPrincipaleText);
        }
        res.setPrincipale(isPrincipale);
        res.setNome(nomeAzienda.getText());
        res.setPIva(iva.getText());
        res.setCodFis(cod_fis.getText());
        res.setMail(email.getText());
        res.setVia(via.getText());
        res.setCivico(civico.getText());
        res.setCap(cap.getText());
        res.setCitta(citta.getText());
        res.setProvincia(provincia.getText());
        res.setNazione(nazione.getText());
        res.setTelefono(telefono.getText());
        res.setFax(fax.getText());
        res.setTassabile(tassabile.isSelected());
        checkAzienda(res);
        return res;
    }
    
    /**
     * Metodo che compila il form relativo alle Aziende con i dati dell'Azienda
     * passata come parametro.
     * @param a 
     */
    public void compilaFormAzienda(Azienda a){
        aziendaId.setText(a.getId().toString());
        nomeAzienda.setText(a.getNome());
        iva.setText(a.getPIva());
        cod_fis.setText(a.getCodFis());
        via.setText(a.getVia());
        civico.setText(a.getCivico());
        cap.setText(a.getCap());
        citta.setText(a.getCitta());
        provincia.setText(a.getProvincia());
        nazione.setText(a.getNazione());
        email.setText(a.getMail());
        telefono.setText(a.getTelefono());
        fax.setText(a.getFax());
        if (cod_fis.getText().equals(iva.getText()))
            giuridico.setSelected(true);
        else giuridico.setSelected(false);
        tassabile.setSelected(Utility.convertNullToBoolean(a.isTassabile()));
        String aziendaPrincipaleText = a.isPrincipale().toString();
        aziendaPrincipale.setText(aziendaPrincipaleText);
    }
    
    public void registraAzienda() throws GenericExceptionToPrint, ValidationException{
        Azienda a=caricaAziendaDaForm();
        try{
            aziendaManager.registraAzienda(a);
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore salvataggio", "Errore salvataggio azienda, riprovare",e);
        }
    }
    
    public void modificaAzienda() throws GenericExceptionToPrint, ValidationException{
        Azienda a=caricaAziendaDaForm();
        if (a.getId()==null){
            throw new GenericExceptionToPrint("Dati errati", "Siamo spiacenti, questa azienda non è registrata.");
        }
        try{
            aziendaManager.modificaAzienda(a);
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore salvataggio", "Errore salvataggio azienda, riprovare",e);
        }
    }
    
    /**
     * Metodo che resetta il form relativo alle aziende.
     */
    public void resetFormAzienda(){
        aziendaId.setText(null);
        nomeAzienda.setText("");
        iva.setText("");
        cod_fis.setText("");
        via.setText("");
        civico.setText("");
        cap.setText("");
        citta.setText("");
        provincia.setText("");
        nazione.setText("");
        email.setText("");
        telefono.setText("");
        fax.setText("");
        aziendaPrincipale.setText("false");
        giuridico.setSelected(true);
        tassabile.setSelected(true);
    }
    
    /**
     * Metodo che abilita la modifica del form relativo alle Aziende.
     */
    public void abilitaFormAzienda(){
        nomeAzienda.setEditable(true);
        iva.setEditable(true);
        cod_fis.setEditable(true);
        via.setEditable(true);
        civico.setEditable(true);
        cap.setEditable(true);
        citta.setEditable(true);
        provincia.setEditable(true);
        nazione.setEditable(true);
        email.setEditable(true);
        telefono.setEditable(true);
        fax.setEditable(true);
        giuridico.setEnabled(true);
        tassabile.setEnabled(true);
    }
    
    
    /**
     * Metodo che disabilita la modifica del form relativo alle Aziende.
     */
    public void disabilitaFormAzienda(){
        nomeAzienda.setEditable(false);
        iva.setEditable(false);
        cod_fis.setEditable(false);
        via.setEditable(false);
        civico.setEditable(false);
        cap.setEditable(false);
        citta.setEditable(false);
        provincia.setEditable(false);
        nazione.setEditable(false);
        email.setEditable(false);
        telefono.setEditable(false);
        fax.setEditable(false);
        giuridico.setEnabled(false);
        tassabile.setEnabled(false);
    }
    
    public void showForm(FormAziendaType formType, String title){
        form.setSize(650, 400);
        switch (formType) {
            case REGISTRATION:
                annulla.setVisible(false);
                bottoneOk.setVisible(false);
                esci.setVisible(true);
                modificaAzienda.setVisible(false);
                registra.setVisible(true);
                aziendaPrincipale.setText("true");
                break;
            case NEW_AZIENDA:
                annulla.setVisible(true);
                bottoneOk.setVisible(false);
                esci.setVisible(false);
                modificaAzienda.setVisible(false);
                registra.setVisible(true);
                abilitaFormAzienda();
                resetFormAzienda();
                aziendaPrincipale.setText("false");
                break;
            case MODIFICA:
                annulla.setVisible(true);
                bottoneOk.setVisible(false);
                esci.setVisible(false);
                modificaAzienda.setVisible(true);
                registra.setVisible(false);
                abilitaFormAzienda();
                giuridico.setEnabled(false);
                break;
            case VISUALIZZA:
                annulla.setVisible(false);
                bottoneOk.setVisible(true);
                esci.setVisible(false);
                modificaAzienda.setVisible(false);
                registra.setVisible(false);
                disabilitaFormAzienda();
                break;
        }
        ((JDialog) form).setTitle(title);
        form.setVisible(true);
    }
    
    protected void checkAzienda(Azienda a) throws ValidationException{
        String nome = a.getNome();
        if (nome==null || nome.trim().isEmpty()) {
            throw new ValidationException("Campo vuoto", "Il nome dell'azienda è necessario");
        }
        if (!Controlli.checkIva(a.getpIva(), true)) {
            throw new ValidationException("Dati errati", "Partita IVA errata");
        }
        if (!Controlli.checkMail(a.getMail(), false)){
            throw new ValidationException("E-mail errata", "Inserire una mail valida");
        }
        if (!Controlli.checkCodFIS(a.getCodFis(), true)){
            throw new ValidationException("Dati errati", "Il campo Codice Fiscale contiene dei dati errati");
        }
    }
    
}
