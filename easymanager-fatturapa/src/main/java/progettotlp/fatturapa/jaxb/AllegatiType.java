
package progettotlp.fatturapa.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Blocco relativo ai dati di eventuali allegati
 * 
 * <p>Java class for AllegatiType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllegatiType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NomeAttachment" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String60LatinType"/>
 *         &lt;element name="AlgoritmoCompressione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String10Type" minOccurs="0"/>
 *         &lt;element name="FormatoAttachment" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String10Type" minOccurs="0"/>
 *         &lt;element name="DescrizioneAttachment" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String100LatinType" minOccurs="0"/>
 *         &lt;element name="Attachment" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AllegatiType", propOrder = {
    "nomeAttachment",
    "algoritmoCompressione",
    "formatoAttachment",
    "descrizioneAttachment",
    "attachment"
})
public class AllegatiType {

    @XmlElement(name = "NomeAttachment", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String nomeAttachment;
    @XmlElement(name = "AlgoritmoCompressione")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String algoritmoCompressione;
    @XmlElement(name = "FormatoAttachment")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String formatoAttachment;
    @XmlElement(name = "DescrizioneAttachment")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String descrizioneAttachment;
    @XmlElement(name = "Attachment", required = true)
    protected byte[] attachment;

    /**
     * Gets the value of the nomeAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeAttachment() {
        return nomeAttachment;
    }

    /**
     * Sets the value of the nomeAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeAttachment(String value) {
        this.nomeAttachment = value;
    }

    /**
     * Gets the value of the algoritmoCompressione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmoCompressione() {
        return algoritmoCompressione;
    }

    /**
     * Sets the value of the algoritmoCompressione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmoCompressione(String value) {
        this.algoritmoCompressione = value;
    }

    /**
     * Gets the value of the formatoAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatoAttachment() {
        return formatoAttachment;
    }

    /**
     * Sets the value of the formatoAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatoAttachment(String value) {
        this.formatoAttachment = value;
    }

    /**
     * Gets the value of the descrizioneAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneAttachment() {
        return descrizioneAttachment;
    }

    /**
     * Sets the value of the descrizioneAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneAttachment(String value) {
        this.descrizioneAttachment = value;
    }

    /**
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAttachment(byte[] value) {
        this.attachment = value;
    }

}
