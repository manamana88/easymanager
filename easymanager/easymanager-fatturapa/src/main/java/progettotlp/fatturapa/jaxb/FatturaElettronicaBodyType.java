//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.04 at 05:43:05 PM GMT 
//


package progettotlp.fatturapa.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FatturaElettronicaBodyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FatturaElettronicaBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiGenerali" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}DatiGeneraliType"/>
 *         &lt;element name="DatiBeniServizi" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}DatiBeniServiziType"/>
 *         &lt;element name="DatiVeicoli" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}DatiVeicoliType" minOccurs="0"/>
 *         &lt;element name="DatiPagamento" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}DatiPagamentoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Allegati" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}AllegatiType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FatturaElettronicaBodyType", propOrder = {
    "datiGenerali",
    "datiBeniServizi",
    "datiVeicoli",
    "datiPagamento",
    "allegati"
})
public class FatturaElettronicaBodyType {

    @XmlElement(name = "DatiGenerali", required = true)
    protected DatiGeneraliType datiGenerali;
    @XmlElement(name = "DatiBeniServizi", required = true)
    protected DatiBeniServiziType datiBeniServizi;
    @XmlElement(name = "DatiVeicoli")
    protected DatiVeicoliType datiVeicoli;
    @XmlElement(name = "DatiPagamento")
    protected List<DatiPagamentoType> datiPagamento;
    @XmlElement(name = "Allegati")
    protected List<AllegatiType> allegati;

    /**
     * Gets the value of the datiGenerali property.
     * 
     * @return
     *     possible object is
     *     {@link DatiGeneraliType }
     *     
     */
    public DatiGeneraliType getDatiGenerali() {
        return datiGenerali;
    }

    /**
     * Sets the value of the datiGenerali property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiGeneraliType }
     *     
     */
    public void setDatiGenerali(DatiGeneraliType value) {
        this.datiGenerali = value;
    }

    /**
     * Gets the value of the datiBeniServizi property.
     * 
     * @return
     *     possible object is
     *     {@link DatiBeniServiziType }
     *     
     */
    public DatiBeniServiziType getDatiBeniServizi() {
        return datiBeniServizi;
    }

    /**
     * Sets the value of the datiBeniServizi property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiBeniServiziType }
     *     
     */
    public void setDatiBeniServizi(DatiBeniServiziType value) {
        this.datiBeniServizi = value;
    }

    /**
     * Gets the value of the datiVeicoli property.
     * 
     * @return
     *     possible object is
     *     {@link DatiVeicoliType }
     *     
     */
    public DatiVeicoliType getDatiVeicoli() {
        return datiVeicoli;
    }

    /**
     * Sets the value of the datiVeicoli property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiVeicoliType }
     *     
     */
    public void setDatiVeicoli(DatiVeicoliType value) {
        this.datiVeicoli = value;
    }

    /**
     * Gets the value of the datiPagamento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datiPagamento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatiPagamento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatiPagamentoType }
     * 
     * 
     */
    public List<DatiPagamentoType> getDatiPagamento() {
        if (datiPagamento == null) {
            datiPagamento = new ArrayList<DatiPagamentoType>();
        }
        return this.datiPagamento;
    }

    /**
     * Gets the value of the allegati property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the allegati property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAllegati().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AllegatiType }
     * 
     * 
     */
    public List<AllegatiType> getAllegati() {
        if (allegati == null) {
            allegati = new ArrayList<AllegatiType>();
        }
        return this.allegati;
    }

	public void setDatiPagamento(List<DatiPagamentoType> datiPagamento) {
		this.datiPagamento = datiPagamento;
	}

	public void setAllegati(List<AllegatiType> allegati) {
		this.allegati = allegati;
	}

}
