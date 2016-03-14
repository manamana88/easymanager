/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package progettotlp.ui;

import java.awt.Container;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import progettotlp.classes.AccountEmail;
import progettotlp.classes.Azienda;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.MailException;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.Controlli;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.FatturaUtilities;
import progettotlp.persistenza.AccountManager;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.FatturaManager;
import progettotlp.print.FatturaPrinter;

public class FormEmailUtilities extends AbstractFormUtilities
{
    private static String EMPTY_CHOICE = "-";
    private static String NO_MAIL = "NO MAIL";
    protected JComboBox from;
    protected JComboBox destinatario;
    protected JTextField altriDestinatari;
    protected JComboBox allegato;
    protected JTextField oggetto;
    protected JTextArea testo;
    protected FatturaManager fatturaManager;
    protected AziendaManager aziendaManager;
    protected AccountManager accountManager;
    boolean areListenersActive = false;
    protected FormInvioEmailInCorsoUtilities formInvioEmailInCorsoUtilities;
    Future<Boolean> invioMail;

    public FormEmailUtilities(JDialog formEmail,
                              FormInvioEmailInCorsoUtilities form,
                              FatturaManager fatturaManager,
                              AziendaManager aziendaManager,
                              AccountManager accountManager)
    {
        this.formInvioEmailInCorsoUtilities = form;
        this.fatturaManager = fatturaManager;
        this.aziendaManager = aziendaManager;
        this.accountManager = accountManager;
        init(formEmail);
    }

    private void activateListeners() {
        this.areListenersActive = true;
    }

    private void deactivateListeners() {
        this.areListenersActive = false;
    }

    protected void chargeComboBox(JComboBox field, List<String> values, boolean addEmptyField) {
        field.removeAllItems();
        if (addEmptyField) {
            field.addItem(EMPTY_CHOICE);
        }
        if (values != null)
            for (String value : values)
                field.addItem(value);
    }

    protected String getSelectedValue(JComboBox combo)
    {
        String selectedItem = (String) combo.getSelectedItem();
        if ((selectedItem != null) && (!(selectedItem.trim().isEmpty())) && (!(EMPTY_CHOICE.equals(selectedItem)))) {
            return selectedItem;
        }
        return null;
    }

    protected String extractAziendaName(String input) {
        Pattern aziendaExtractor = Pattern.compile("^(.+)( \\[[^\\]]+\\])$");
        Matcher matcher = aziendaExtractor.matcher(input);
        if ((matcher.matches()) && (matcher.groupCount() > 0)) {
            return matcher.group(1);
        }
        return null;
    }

    protected String extractAziendaMail(String input)
    {
        Pattern aziendaExtractor = Pattern.compile("^(.+)( \\[([^\\]]+)\\])$");
        Matcher matcher = aziendaExtractor.matcher(input);
        if ((matcher.matches()) && (matcher.groupCount() > 0)) {
            return matcher.group(3);
        }
        return null;
    }

    protected String extractFatturaId(String input)
    {
        Pattern aziendaExtractor = Pattern.compile("^(Fattura )(\\d+)( del.*)$");
        Matcher matcher = aziendaExtractor.matcher(input);
        if ((matcher.matches()) && (matcher.groupCount() > 0)) {
            return matcher.group(2);
        }
        return null;
    }

    protected String getSelectedAziendaName() {
        String selectedAzienda = getSelectedValue(this.destinatario);
        return ((selectedAzienda == null) ? null : extractAziendaName(selectedAzienda));
    }

    protected String getSelectedAziendaMail() {
        String selectedAzienda = getSelectedValue(this.destinatario);
        return ((selectedAzienda == null) ? null : extractAziendaMail(selectedAzienda));
    }

    protected void resetForm() throws GenericExceptionToPrint {
        List<AccountEmail> accounts = this.accountManager.getAccounts();
        if (accounts.isEmpty()) {
            throw new GenericExceptionToPrint("Errore", "Non ci sono account email salvati.");
        }
        List<String> accountsToCharge = new ArrayList<String>();
        for (AccountEmail accountEmail : accounts) {
            accountsToCharge.add(accountEmail.getUsername());
        }
        chargeComboBox(this.from, accountsToCharge, false);

        List<Azienda> aziendeNonPrincipali = this.aziendaManager.getAziendeNonPrincipali();
        List<String> aziendeToCharge = new ArrayList<String>();
        for (Azienda azienda : aziendeNonPrincipali) {
            String mail = azienda.getMail();
            if ((mail == null) || (mail.trim().isEmpty()))
                aziendeToCharge.add(azienda.getNome() + " [" + NO_MAIL + "]");
            else {
                aziendeToCharge.add(azienda.getNome() + " [" + mail + "]");
            }
        }
        chargeComboBox(this.destinatario, aziendeToCharge, true);

        this.altriDestinatari.setText(null);
        this.allegato.removeAllItems();
        this.oggetto.setText(null);
        this.testo.setText(null);
    }

    public void changeDestinatarioListener() {
        if (this.areListenersActive) {
            String selectedAziendaName = getSelectedAziendaName();
            if (selectedAziendaName != null) {
                List<Fattura> fattureByAziendaName = this.fatturaManager.getFattureByAziendaName(selectedAziendaName);
                List<String> fatturaSintesi = new ArrayList<String>();
                for (Fattura f : fattureByAziendaName) {
                    fatturaSintesi.add("Fattura " + f.getId()
                        + " del "
                        + DateUtils.formatDate(f.getEmissione())
                        + " a "
                        + f.getCliente().getNome());
                }
                chargeComboBox(this.allegato, fatturaSintesi, true);
            } else {
                this.allegato.removeAllItems();
                this.allegato.addItem(EMPTY_CHOICE);
            }
        }
    }

    public MimeMessage inviaEmail() throws ValidationException, MailException {
        AccountEmail accountByUsername = this.accountManager.getAccountByUsername(getSelectedValue(this.from));
        List allDestinatari = new ArrayList();
        String selectedAziendaMail = getSelectedAziendaMail();
        if ((!(NO_MAIL.equals(selectedAziendaMail))) && (selectedAziendaMail != null)) {
            allDestinatari.add(selectedAziendaMail);
        }
        String[] emails = this.altriDestinatari.getText().split(";", -1);
        for (String email : emails) {
            if (Controlli.checkMail(email, false)) {
                if ((email != null) && (!(email.trim().isEmpty())))
                    allDestinatari.add(email);
            }
            else {
                throw new ValidationException("Indirizzo email [" + email + "] non valido.");
            }
        }
        if (allDestinatari.isEmpty()) {
            throw new ValidationException("Non è stato inserito nessun destinatario");
        }
        String testoValue = this.testo.getText();
        String oggettoValue = this.oggetto.getText();
        String selectedValue = getSelectedValue(this.allegato);
        Fattura f = null;
        if (selectedValue != null) {
            f = this.fatturaManager.getFattura(Integer.parseInt(extractFatturaId(selectedValue)), true, true);
        }

        return compileMail(accountByUsername, f, allDestinatari, oggettoValue, testoValue);
    }

    protected MimeMessage compileMail(final AccountEmail account,
                                      Fattura f,
                                      List<String> allDestinatari,
                                      String oggettoValue,
                                      String testoValue) throws MailException
    {
        try {
            boolean isSetAttachment = f != null;
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", account.getSmtp());

            Session session = Session.getInstance(props, new Authenticator()
            {
                public PasswordAuthentication getPasswordAuthentication() {
                    String username = account.getUsername();
                    String password = account.getPassword();
                    return new PasswordAuthentication(username, password);
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(account.getUsername()));
            for (String emailDestinatario : allDestinatari) {
                message.addRecipient(RecipientType.TO, new InternetAddress(emailDestinatario));
            }

            message.setSubject(oggettoValue);
            if (!(isSetAttachment)) {
                message.setText(testoValue);
            } else {
                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(testoValue);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textBodyPart);
                List printPage = FatturaPrinter.printPage(f, this.aziendaManager.getAziendaPrincipale(), true);
                for (int i = 0; i < printPage.size(); ++i) {
                    File file = (File) printPage.get(i);
                    MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    attachmentBodyPart.setDataHandler(new DataHandler(source));
                    attachmentBodyPart.setFileName(FatturaUtilities.getFileName(f) + " Pagina " + (i + 1) + ".pdf");
                    multipart.addBodyPart(attachmentBodyPart);
                }
                message.setContent(multipart);
            }
            return message;
        } catch (Exception e) {
            throw new MailException("Errore", "Siamo spiacenti si è verificato un errore.", e);
        }
    }

    public void showForm() throws GenericExceptionToPrint {
        resetForm();
        activateListeners();
        this.form.setSize(550, 550);
        this.form.setVisible(true);
    }

    public void hideForm()
    {
        deactivateListeners();
        ((JDialog) this.form).dispose();
    }
}