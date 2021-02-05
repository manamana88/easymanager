package progettotlp.fatturapa.jaxb.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Amount8Digits extends XmlAdapter<String,BigDecimal> {

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
		return new BigDecimal(v);
	}

	@Override
	public String marshal(BigDecimal v) throws Exception {
		NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(8);
		format.setGroupingUsed(false);
		return format.format(v);
	}

}
