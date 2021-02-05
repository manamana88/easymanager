
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TipoRitenutaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoRitenutaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="4"/>
 *     &lt;enumeration value="RT01"/>
 *     &lt;enumeration value="RT02"/>
 *     &lt;enumeration value="RT03"/>
 *     &lt;enumeration value="RT04"/>
 *     &lt;enumeration value="RT05"/>
 *     &lt;enumeration value="RT06"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TipoRitenutaType")
@XmlEnum
public enum TipoRitenutaType {


    /**
     * Ritenuta di acconto persone fisiche
     * 
     */
    @XmlEnumValue("RT01")
    RT_01("RT01"),

    /**
     * Ritenuta di acconto persone giuridiche
     * 
     */
    @XmlEnumValue("RT02")
    RT_02("RT02"),

    /**
     * Contributo INPS
     * 
     */
    @XmlEnumValue("RT03")
    RT_03("RT03"),

    /**
     * Contributo ENASARCO
     * 
     */
    @XmlEnumValue("RT04")
    RT_04("RT04"),

    /**
     * Contributo ENPAM
     * 
     */
    @XmlEnumValue("RT05")
    RT_05("RT05"),

    /**
     * Altro contributo previdenziale
     * 
     */
    @XmlEnumValue("RT06")
    RT_06("RT06");
    private final String value;

    TipoRitenutaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoRitenutaType fromValue(String v) {
        for (TipoRitenutaType c: TipoRitenutaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
