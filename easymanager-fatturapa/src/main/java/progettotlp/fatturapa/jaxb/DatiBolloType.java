
package progettotlp.fatturapa.jaxb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import progettotlp.fatturapa.jaxb.adapter.Amount2Digits;


/**
 * <p>Java class for DatiBolloType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatiBolloType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BolloVirtuale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}BolloVirtualeType"/>
 *         &lt;element name="ImportoBollo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiBolloType", propOrder = {
    "bolloVirtuale",
    "importoBollo"
})
public class DatiBolloType {

    @XmlElement(name = "BolloVirtuale", required = true)
    @XmlSchemaType(name = "string")
    protected BolloVirtualeType bolloVirtuale;
    @XmlJavaTypeAdapter(Amount2Digits.class)
    @XmlElement(name = "ImportoBollo")
    protected BigDecimal importoBollo;

    /**
     * Gets the value of the bolloVirtuale property.
     * 
     * @return
     *     possible object is
     *     {@link BolloVirtualeType }
     *     
     */
    public BolloVirtualeType getBolloVirtuale() {
        return bolloVirtuale;
    }

    /**
     * Sets the value of the bolloVirtuale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BolloVirtualeType }
     *     
     */
    public void setBolloVirtuale(BolloVirtualeType value) {
        this.bolloVirtuale = value;
    }

    /**
     * Gets the value of the importoBollo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoBollo() {
        return importoBollo;
    }

    /**
     * Sets the value of the importoBollo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoBollo(BigDecimal value) {
        this.importoBollo = value;
    }

}
