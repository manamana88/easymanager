
package progettotlp.fatturapa.jaxb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DatiRitenutaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatiRitenutaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoRitenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}TipoRitenutaType"/>
 *         &lt;element name="ImportoRitenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType"/>
 *         &lt;element name="AliquotaRitenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RateType"/>
 *         &lt;element name="CausalePagamento" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CausalePagamentoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiRitenutaType", propOrder = {
    "tipoRitenuta",
    "importoRitenuta",
    "aliquotaRitenuta",
    "causalePagamento"
})
public class DatiRitenutaType {

    @XmlElement(name = "TipoRitenuta", required = true)
    @XmlSchemaType(name = "string")
    protected TipoRitenutaType tipoRitenuta;
    @XmlElement(name = "ImportoRitenuta", required = true)
    protected BigDecimal importoRitenuta;
    @XmlElement(name = "AliquotaRitenuta", required = true)
    protected BigDecimal aliquotaRitenuta;
    @XmlElement(name = "CausalePagamento", required = true)
    @XmlSchemaType(name = "string")
    protected CausalePagamentoType causalePagamento;

    /**
     * Gets the value of the tipoRitenuta property.
     * 
     * @return
     *     possible object is
     *     {@link TipoRitenutaType }
     *     
     */
    public TipoRitenutaType getTipoRitenuta() {
        return tipoRitenuta;
    }

    /**
     * Sets the value of the tipoRitenuta property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoRitenutaType }
     *     
     */
    public void setTipoRitenuta(TipoRitenutaType value) {
        this.tipoRitenuta = value;
    }

    /**
     * Gets the value of the importoRitenuta property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoRitenuta() {
        return importoRitenuta;
    }

    /**
     * Sets the value of the importoRitenuta property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoRitenuta(BigDecimal value) {
        this.importoRitenuta = value;
    }

    /**
     * Gets the value of the aliquotaRitenuta property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAliquotaRitenuta() {
        return aliquotaRitenuta;
    }

    /**
     * Sets the value of the aliquotaRitenuta property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAliquotaRitenuta(BigDecimal value) {
        this.aliquotaRitenuta = value;
    }

    /**
     * Gets the value of the causalePagamento property.
     * 
     * @return
     *     possible object is
     *     {@link CausalePagamentoType }
     *     
     */
    public CausalePagamentoType getCausalePagamento() {
        return causalePagamento;
    }

    /**
     * Sets the value of the causalePagamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link CausalePagamentoType }
     *     
     */
    public void setCausalePagamento(CausalePagamentoType value) {
        this.causalePagamento = value;
    }

}
