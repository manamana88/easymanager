//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.04 at 05:43:05 PM GMT 
//


package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoggettoEmittenteType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SoggettoEmittenteType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="2"/>
 *     &lt;enumeration value="CC"/>
 *     &lt;enumeration value="TZ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SoggettoEmittenteType")
@XmlEnum
public enum SoggettoEmittenteType {


    /**
     * Cessionario / Committente
     * 
     */
    CC,

    /**
     * Terzo
     * 
     */
    TZ;

    public String value() {
        return name();
    }

    public static SoggettoEmittenteType fromValue(String v) {
        return valueOf(v);
    }

}