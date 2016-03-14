/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.persistenza.AccountManager;

/**
 *
 * @author vincenzo
 */
public class FormAccountUtilities extends AbstractFormUtilities{
    public static enum FormAccountType{NEW_ACCOUNT,MODIFICA};
    private static String MODIFICA_TITLE="Modifica account";
    private static String NEW_ACCOUNT_TITLE="Inserisci un nuovo account";

    protected JTextField accountId;
    protected JTextField username;
    protected JPasswordField password;
    protected JPasswordField passwordConferma;
    protected JTextField smtp;
    protected JButton annullaAccount;
    protected JButton salvaAccount;
    protected JButton modificaAccount;

    private AccountManager accountManager;

    public FormAccountUtilities(JDialog form, AccountManager accountManager) {
        this.accountManager=accountManager;
        init(form);
    }

    private void resetFormAccount(){
        accountId.setText(null);
        username.setText(null);
        password.setText(null);
        passwordConferma.setText(null);
        smtp.setText(null);
    }

    public void salvaAccount() throws ValidationException, GenericExceptionToPrint{
        AccountEmail caricaAccountDaForm = caricaAccountDaForm();
        if (accountManager.getAccountByUsername(caricaAccountDaForm.getUsername())!=null){
            throw new GenericExceptionToPrint("Errore salvataggio", "Esiste già un account con lo stesso username");
        }
        try {
            accountManager.registraAccount(caricaAccountDaForm);
        } catch (Exception e) {
            throw new GenericExceptionToPrint("Errore salvataggio", "Errore salvataggio account, riprovare",e);
        }
    }

    public void modificaAccount() throws ValidationException, GenericExceptionToPrint{
        AccountEmail caricaAccountDaForm = caricaAccountDaForm();
        try {
            accountManager.modificaAccount(caricaAccountDaForm);
        } catch (Exception e) {
            throw new GenericExceptionToPrint("Errore salvataggio", "Errore salvataggio account, riprovare",e);
        }
    }

    public void compilaFormAccount(AccountEmail accountEmail){
        resetFormAccount();
        Long id = accountEmail.getId();
        if (id!=null){
            accountId.setText(id.toString());
        }
        username.setText(accountEmail.getUsername());
        smtp.setText(accountEmail.getSmtp());
    }

    public AccountEmail caricaAccountDaForm() throws ValidationException{
        String idString = accountId.getText();
        Long id=null;
        if (idString!= null && !idString.trim().isEmpty()){
            id=Long.parseLong(idString);
        }
        String usernameValue=username.getText();
        String passwordValue;
        String passwordConfermaValue;
        if (usernameValue==null || usernameValue.trim().isEmpty())
            throw new ValidationException("Il campo username è obbligatorio.");
        try{
            passwordValue=new String(password.getPassword());
            if (passwordValue==null||passwordValue.trim().isEmpty())
                throw new NullPointerException();
        } catch (NullPointerException e){
            throw new ValidationException("Il campo password è obbligatorio.");
        }

        try{
            passwordConfermaValue=new String(passwordConferma.getPassword());
            if (passwordConfermaValue==null||passwordConfermaValue.trim().isEmpty())
                throw new NullPointerException();
        } catch (NullPointerException e){
            throw new ValidationException("Il campo conferma  password è obbligatorio.");
        }
        if (!passwordValue.equals(passwordConfermaValue)){
            throw new ValidationException("Le due password non coincidono");
        }
        String smtpValue = smtp.getText();
        return new AccountEmail(id, usernameValue, passwordValue, smtpValue);
    }

    public void showForm(FormAccountType formType){
        String title=null;
        switch (formType) {
            case NEW_ACCOUNT:
                salvaAccount.setVisible(true);
                modificaAccount.setVisible(false);
                resetFormAccount();
                title=NEW_ACCOUNT_TITLE;
                break;
            case MODIFICA:
                salvaAccount.setVisible(false);
                modificaAccount.setVisible(true);
                title=MODIFICA_TITLE;
                break;
        }
        ((JDialog) form).setTitle(title);
        form.setVisible(true);
    }
}
