
package progettotlp.fatturapa.jaxb;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DatiTrasportoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatiTrasportoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiAnagraficiVettore" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}DatiAnagraficiVettoreType" minOccurs="0"/>
 *         &lt;element name="MezzoTrasporto" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String80LatinType" minOccurs="0"/>
 *         &lt;element name="CausaleTrasporto" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String100LatinType" minOccurs="0"/>
 *         &lt;element name="NumeroColli" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}NumeroColliType" minOccurs="0"/>
 *         &lt;element name="Descrizione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String100LatinType" minOccurs="0"/>
 *         &lt;element name="UnitaMisuraPeso" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String10Type" minOccurs="0"/>
 *         &lt;element name="PesoLordo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}PesoType" minOccurs="0"/>
 *         &lt;element name="PesoNetto" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}PesoType" minOccurs="0"/>
 *         &lt;element name="DataOraRitiro" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="DataInizioTrasporto" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="TipoResa" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}TipoResaType" minOccurs="0"/>
 *         &lt;element name="IndirizzoResa" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}IndirizzoType" minOccurs="0"/>
 *         &lt;element name="DataOraConsegna" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiTrasportoType", propOrder = {
    "datiAnagraficiVettore",
    "mezzoTrasporto",
    "causaleTrasporto",
    "numeroColli",
    "descrizione",
    "unitaMisuraPeso",
    "pesoLordo",
    "pesoNetto",
    "dataOraRitiro",
    "dataInizioTrasporto",
    "tipoResa",
    "indirizzoResa",
    "dataOraConsegna"
})
public class DatiTrasportoType {

    @XmlElement(name = "DatiAnagraficiVettore")
    protected DatiAnagraficiVettoreType datiAnagraficiVettore;
    @XmlElement(name = "MezzoTrasporto")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String mezzoTrasporto;
    @XmlElement(name = "CausaleTrasporto")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String causaleTrasporto;
    @XmlElement(name = "NumeroColli")
    @XmlSchemaType(name = "integer")
    protected Integer numeroColli;
    @XmlElement(name = "Descrizione")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String descrizione;
    @XmlElement(name = "UnitaMisuraPeso")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String unitaMisuraPeso;
    @XmlElement(name = "PesoLordo")
    protected BigDecimal pesoLordo;
    @XmlElement(name = "PesoNetto")
    protected BigDecimal pesoNetto;
    @XmlElement(name = "DataOraRitiro")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataOraRitiro;
    @XmlElement(name = "DataInizioTrasporto")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataInizioTrasporto;
    @XmlElement(name = "TipoResa")
    protected String tipoResa;
    @XmlElement(name = "IndirizzoResa")
    protected IndirizzoType indirizzoResa;
    @XmlElement(name = "DataOraConsegna")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataOraConsegna;

    /**
     * Gets the value of the datiAnagraficiVettore property.
     * 
     * @return
     *     possible object is
     *     {@link DatiAnagraficiVettoreType }
     *     
     */
    public DatiAnagraficiVettoreType getDatiAnagraficiVettore() {
        return datiAnagraficiVettore;
    }

    /**
     * Sets the value of the datiAnagraficiVettore property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiAnagraficiVettoreType }
     *     
     */
    public void setDatiAnagraficiVettore(DatiAnagraficiVettoreType value) {
        this.datiAnagraficiVettore = value;
    }

    /**
     * Gets the value of the mezzoTrasporto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMezzoTrasporto() {
        return mezzoTrasporto;
    }

    /**
     * Sets the value of the mezzoTrasporto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMezzoTrasporto(String value) {
        this.mezzoTrasporto = value;
    }

    /**
     * Gets the value of the causaleTrasporto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCausaleTrasporto() {
        return causaleTrasporto;
    }

    /**
     * Sets the value of the causaleTrasporto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCausaleTrasporto(String value) {
        this.causaleTrasporto = value;
    }

    /**
     * Gets the value of the numeroColli property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroColli() {
        return numeroColli;
    }

    /**
     * Sets the value of the numeroColli property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroColli(Integer value) {
        this.numeroColli = value;
    }

    /**
     * Gets the value of the descrizione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Sets the value of the descrizione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

    /**
     * Gets the value of the unitaMisuraPeso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitaMisuraPeso() {
        return unitaMisuraPeso;
    }

    /**
     * Sets the value of the unitaMisuraPeso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitaMisuraPeso(String value) {
        this.unitaMisuraPeso = value;
    }

    /**
     * Gets the value of the pesoLordo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPesoLordo() {
        return pesoLordo;
    }

    /**
     * Sets the value of the pesoLordo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPesoLordo(BigDecimal value) {
        this.pesoLordo = value;
    }

    /**
     * Gets the value of the pesoNetto property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPesoNetto() {
        return pesoNetto;
    }

    /**
     * Sets the value of the pesoNetto property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPesoNetto(BigDecimal value) {
        this.pesoNetto = value;
    }

    /**
     * Gets the value of the dataOraRitiro property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataOraRitiro() {
        return dataOraRitiro;
    }

    /**
     * Sets the value of the dataOraRitiro property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataOraRitiro(XMLGregorianCalendar value) {
        this.dataOraRitiro = value;
    }

    /**
     * Gets the value of the dataInizioTrasporto property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizioTrasporto() {
        return dataInizioTrasporto;
    }

    /**
     * Sets the value of the dataInizioTrasporto property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizioTrasporto(XMLGregorianCalendar value) {
        this.dataInizioTrasporto = value;
    }

    /**
     * Gets the value of the tipoResa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoResa() {
        return tipoResa;
    }

    /**
     * Sets the value of the tipoResa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoResa(String value) {
        this.tipoResa = value;
    }

    /**
     * Gets the value of the indirizzoResa property.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoType }
     *     
     */
    public IndirizzoType getIndirizzoResa() {
        return indirizzoResa;
    }

    /**
     * Sets the value of the indirizzoResa property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoType }
     *     
     */
    public void setIndirizzoResa(IndirizzoType value) {
        this.indirizzoResa = value;
    }

    /**
     * Gets the value of the dataOraConsegna property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataOraConsegna() {
        return dataOraConsegna;
    }

    /**
     * Sets the value of the dataOraConsegna property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataOraConsegna(XMLGregorianCalendar value) {
        this.dataOraConsegna = value;
    }

}
