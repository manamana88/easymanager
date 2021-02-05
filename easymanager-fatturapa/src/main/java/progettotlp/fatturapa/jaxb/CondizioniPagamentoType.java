
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CondizioniPagamentoType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CondizioniPagamentoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;minLength value="4"/>
 *     &lt;maxLength value="4"/>
 *     &lt;enumeration value="TP01"/>
 *     &lt;enumeration value="TP02"/>
 *     &lt;enumeration value="TP03"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CondizioniPagamentoType")
@XmlEnum
public enum CondizioniPagamentoType {


    /**
     * pagamento a rate
     * 
     */
    @XmlEnumValue("TP01")
    TP_01("TP01"),

    /**
     * pagamento completo
     * 
     */
    @XmlEnumValue("TP02")
    TP_02("TP02"),

    /**
     * anticipo
     * 
     */
    @XmlEnumValue("TP03")
    TP_03("TP03");
    private final String value;

    CondizioniPagamentoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CondizioniPagamentoType fromValue(String v) {
        for (CondizioniPagamentoType c: CondizioniPagamentoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
