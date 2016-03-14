package progettotlp.rest.beans;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ErrorBean {

    private String message;
    private String type;
    private Long code;
    private Long errorSubcode;
    private String errorUserTitle;
    private String errorUserMsg;
    private Map<String, Object> otherDetails;

    public ErrorBean() {
        super();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getErrorSubcode() {
        return errorSubcode;
    }

    public void setErrorSubcode(Long errorSubcode) {
        this.errorSubcode = errorSubcode;
    }

    public String getErrorUserTitle() {
        return errorUserTitle;
    }

    public void setErrorUserTitle(String errorUserTitle) {
        this.errorUserTitle = errorUserTitle;
    }

    public String getErrorUserMsg() {
        return errorUserMsg;
    }

    public void setErrorUserMsg(String errorUserMsg) {
        this.errorUserMsg = errorUserMsg;
    }

    public Map<String, Object> getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(Map<String, Object> otherDetails) {
        this.otherDetails = otherDetails;
    }

}
