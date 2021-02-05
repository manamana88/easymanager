
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Art73Type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Art73Type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="SI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Art73Type")
@XmlEnum
public enum Art73Type {


    /**
     * SI = Documento emesso secondo modalita' e termini stabiliti con DM ai sensi dell'art. 73 DPR 633/72
     * 
     */
    SI;

    public String value() {
        return name();
    }

    public static Art73Type fromValue(String v) {
        return valueOf(v);
    }

}
