
package progettotlp.exceptions.toprint;

public class MailException extends AbstractExceptionToPrint
{
    public MailException(String header, String body)
    {
        super(header, body);
    }

    public MailException(String header, String body, Throwable cause) {
        super(header, body, cause);
    }
}