package progettotlp.fatturapa.jaxb.adapter;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import progettotlp.facilities.StringUtils;

public class Amount2Digits extends XmlAdapter<String,BigDecimal> {

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
		return new BigDecimal(v);
	}

	@Override
	public String marshal(BigDecimal v) throws Exception {
		return StringUtils.formatNumber(v);
	}

}
