package progettotlp.facilities;

import java.util.Arrays;

import progettotlp.rest.beans.ResponseBean;

public class BeanUtils {

    public static ResponseBean createResponseBean(String input) {
        ResponseBean bean = new ResponseBean();
        bean.addToItems(input);
        return bean;
    }

    public static ResponseBean createResponseBean(Object ... objects) {
    	ResponseBean bean = new ResponseBean();
    	bean.setItems(Arrays.asList(objects));
    	return bean;
    }

}
