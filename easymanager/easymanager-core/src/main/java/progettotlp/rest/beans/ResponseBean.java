package progettotlp.rest.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ResponseBean {

    private List<ErrorBean> error;
    private List<Object> items;

    public ResponseBean() {
        super();
    }

    public void addToItems(Object object) {
        if (items == null) {
            items = new ArrayList<Object>();
        }
        items.add(object);
    }

    public List<ErrorBean> getError() {
        return error;
    }

    public void setError(List<ErrorBean> error) {
        this.error = error;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
    
}
