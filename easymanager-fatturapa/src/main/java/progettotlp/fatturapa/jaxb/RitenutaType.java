
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RitenutaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RitenutaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="SI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RitenutaType")
@XmlEnum
public enum RitenutaType {


    /**
     * SI = Cessione / Prestazione soggetta a ritenuta
     * 
     */
    SI;

    public String value() {
        return name();
    }

    public static RitenutaType fromValue(String v) {
        return valueOf(v);
    }

}
