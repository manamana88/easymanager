/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package progettotlp.ui;

import java.awt.Container;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.exceptions.toprint.MailException;
import progettotlp.facilities.Utility;

public class FormInvioEmailInCorsoUtilities extends AbstractFormUtilities {

    protected JButton annullaInvioEmail;
    private Future<Boolean> invioInCorso;

    public FormInvioEmailInCorsoUtilities(JDialog formEmailInCorso) {
        init(formEmailInCorso);
    }

    public void setInvioInCorso(Future<Boolean> invioInCorso) {
        this.invioInCorso = invioInCorso;
    }

    public void killMail() {
        if (this.invioInCorso != null) {
            this.invioInCorso.cancel(true);
        }
    }

    public void showDialog() {
        System.out.println("showDialog");
        this.annullaInvioEmail.setVisible(false);
        this.form.setSize(300, 200);
        this.form.setVisible(true);
    }

    public void disposeDialog() {
        System.out.println("disposeDialog");
        this.form.setVisible(false);
    }

    public void enableButton() {
        System.out.println("enableButton");
        this.annullaInvioEmail.setVisible(true);
    }

    public void sendMail(final MimeMessage message) throws MailException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        this.invioInCorso = executor.submit(new Callable() {

            Logger logger = LoggerFactory.getLogger(FormInvioEmailInCorsoUtilities.class);

            public Boolean call() throws Exception {
                try {
                    Transport.send(message);
                    return Boolean.valueOf(true);
                } catch (MessagingException ex) {
                    this.logger.error("Unable to send email: ", ex);
                }
                return Boolean.valueOf(false);
            }
        });
        executor.execute(new Runnable() {

            public void run() {
                boolean result = false;
                for (int i = 0; (!(result)) && (i < 10); ++i) {
                    System.out.println("Time: " + i);
                    try {
                        if (FormInvioEmailInCorsoUtilities.this.invioInCorso.isCancelled()) {
                            System.out.println("CANCELLED");
                            result = false;
                        }
                        result = ((Boolean) FormInvioEmailInCorsoUtilities.this.invioInCorso.get(1L, TimeUnit.SECONDS)).booleanValue();

                        if ((FormInvioEmailInCorsoUtilities.this.invioInCorso.isDone()) && (!(result))) {
                            FormInvioEmailInCorsoUtilities.this.disposeDialog();
                            System.out.println("FAULT");
                        }
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getClass().getSimpleName());
                    } catch (ExecutionException ex) {
                        System.out.println(ex.getClass().getSimpleName());
                    } catch (TimeoutException ex) {
                        System.out.println(ex.getClass().getSimpleName());
                        FormInvioEmailInCorsoUtilities.this.enableButton();
                    } catch (CancellationException ex) {
                        System.out.println(ex.getClass().getSimpleName());
                    }
                }
                FormInvioEmailInCorsoUtilities.this.disposeDialog();
            }
        });
        showDialog();
        try {
            if ((this.invioInCorso.isCancelled()) || (!(((Boolean) this.invioInCorso.get()).booleanValue()))) {
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new MailException("Errore", "Errore nell'invio della mail");
        }

        executor.shutdown();
    }
}
