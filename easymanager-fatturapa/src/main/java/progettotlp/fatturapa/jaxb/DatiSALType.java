
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DatiSALType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatiSALType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RiferimentoFase" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RiferimentoFaseType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiSALType", propOrder = {
    "riferimentoFase"
})
public class DatiSALType {

    @XmlElement(name = "RiferimentoFase")
    @XmlSchemaType(name = "integer")
    protected int riferimentoFase;

    /**
     * Gets the value of the riferimentoFase property.
     * 
     */
    public int getRiferimentoFase() {
        return riferimentoFase;
    }

    /**
     * Sets the value of the riferimentoFase property.
     * 
     */
    public void setRiferimentoFase(int value) {
        this.riferimentoFase = value;
    }

}
