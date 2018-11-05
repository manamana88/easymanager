package progettotlp.fatturapa.jaxb.adapter;

import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import progettotlp.facilities.DateUtils;

public class SimpleDateAdapter extends XmlAdapter<String,XMLGregorianCalendar> {

	
	@Override
	public XMLGregorianCalendar unmarshal(String v) throws Exception {
		return DateUtils.toXmlGregorianCalendar(DateUtils.parseDate(v));
	}

	@Override
	public String marshal(XMLGregorianCalendar v) throws Exception {
		return new SimpleDateFormat("yyyy-MM-dd").format(v.toGregorianCalendar().getTime());
	}

}
