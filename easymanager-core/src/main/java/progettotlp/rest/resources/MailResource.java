package progettotlp.rest.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.classes.AccountEmail;
import progettotlp.classes.Azienda;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.toprint.MailException;
import progettotlp.facilities.Controlli;
import progettotlp.facilities.FatturaUtilities;
import progettotlp.interfaces.AccountEmailInterface;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;
import progettotlp.persistenza.AccountManager;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.FatturaManager;
import progettotlp.persistenza.ManagerProvider;
import progettotlp.print.DdtPrinter;
import progettotlp.print.FatturaPrinter;

@Path("mail")
public class MailResource {
	
	private final Pattern ATTACHMENT_MATCHER = Pattern.compile("([FD])(\\d*)");
	private AccountManager accountManager = ManagerProvider.getAccountManager();
	private AziendaManager aziendaManager = ManagerProvider.getAziendaManager();
	private DdTManager ddtManager = ManagerProvider.getDdtManager();
	private FatturaManager fatturaManager = ManagerProvider.getFatturaManager();
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response invia(
			@FormParam("account") Long accountId,
			@FormParam("company") Long companyId,
			@FormParam("recipients") String recipients,
			@FormParam("attachment") String attachmentIdString,
			@FormParam("object") String object,
			@FormParam("text") String text
			) throws Exception{
		AccountEmailInterface accountEmail = accountManager.get(AccountEmail.class, accountId);
		MimeBodyPart attachment = null;
		if (attachmentIdString!=null && !attachmentIdString.trim().isEmpty()){
			Matcher matcher = ATTACHMENT_MATCHER.matcher(attachmentIdString);
			if (matcher.matches()){
				long attachmentId = Long.parseLong(matcher.group(2));
				AziendaInterface aziendaPrincipale = aziendaManager.getAziendaPrincipale();
				if ("F".equalsIgnoreCase(matcher.group(1))){
					FatturaInterface fattura = fatturaManager.get(Fattura.class, attachmentId);
					fattura = fatturaManager.getFattura(fattura.getId(), true, true);
					File printPage = FatturaPrinter.printPage(fattura, aziendaPrincipale, true);
					attachment = createAttachment(printPage, FatturaUtilities.getFileName(fattura) + ".pdf");
				} else {
					DdTInterface ddt = ddtManager.getDdT(attachmentId, true, true);
					File printPage = DdtPrinter.printPage(aziendaPrincipale, ddt, true);
					attachment = createAttachment(printPage, ddt.getId()+" - "+ddt.getCliente().getNome()+".pdf");
				}
			}
		}
		List<String> recipientList = new ArrayList<String>();
		if (companyId != null){
			AziendaInterface azienda = aziendaManager.get(Azienda.class, companyId);
			if (azienda != null){
				String mail = azienda.getMail();
				if (mail!=null && !mail.trim().isEmpty()){
					recipientList.add(mail);
				}
			}
		}
		for (String recipient : recipients.split(",")) {
			if (recipient!=null && !recipient.trim().isEmpty()){
				recipientList.add(recipient);
			}
		}
		checkDestinatari(recipientList);
		MimeMessage compileMail = compileMail(accountEmail, recipientList, object, attachment, text);
        Transport.send(compileMail);
		return Response.ok().build();
	}

	private void checkDestinatari(List<String> recipientList) throws MailException {
		if (recipientList.isEmpty()){
			throw new MailException("Errore", "Nessun destinatario specificato");
		}
		for (String string : recipientList) {
			if (!Controlli.checkMail(string, true)){
				throw new MailException("Errore", "Mail ["+string+"] non valida");
			}
		}
	}

	public MimeBodyPart createAttachment(File file, String filename) throws MessagingException{
		MimeBodyPart attachmentBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(file);
		attachmentBodyPart.setDataHandler(new DataHandler(source));
		attachmentBodyPart.setFileName(filename);
		return attachmentBodyPart;
	}

	public MimeMessage compileMail(final AccountEmailInterface account, List<String> allDestinatari, String oggetto, MimeBodyPart attachment, String text) throws MailException{
		try {
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

            message.setSubject(oggetto);
            if (attachment==null) {
                message.setText(text);
            } else {
                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(text);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textBodyPart);
                multipart.addBodyPart(attachment);
                message.setContent(multipart);
            }
            return message;
        } catch (Exception e) {
            throw new MailException("Errore", "Siamo spiacenti si \u00E8 verificato un errore.", e);
        }
	}
}
