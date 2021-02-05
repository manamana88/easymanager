
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CodiceArticoloType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CodiceArticoloType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodiceTipo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String35Type"/>
 *         &lt;element name="CodiceValore" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String35LatinExtType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodiceArticoloType", propOrder = {
    "codiceTipo",
    "codiceValore"
})
public class CodiceArticoloType {

    @XmlElement(name = "CodiceTipo", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String codiceTipo;
    @XmlElement(name = "CodiceValore", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String codiceValore;

    /**
     * Gets the value of the codiceTipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipo() {
        return codiceTipo;
    }

    /**
     * Sets the value of the codiceTipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipo(String value) {
        this.codiceTipo = value;
    }

    /**
     * Gets the value of the codiceValore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceValore() {
        return codiceValore;
    }

    /**
     * Sets the value of the codiceValore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceValore(String value) {
        this.codiceValore = value;
    }

}
