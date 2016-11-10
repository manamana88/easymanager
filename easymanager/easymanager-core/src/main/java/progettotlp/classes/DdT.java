/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.DateUtils;
import progettotlp.rest.utils.DateDeserializer;
import progettotlp.rest.utils.DateSerializer;
import progettotlp.rest.utils.FatturaSerializer;

/**
 * La classe DdT rappresenta un documento di trasporto, ovvero un documento che
 * giustifica il trasferimento di un materiale da cedente a cessionario, attraverso
 * il trasporto dello stesso.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
@Entity
@Table(name="ddt")
public class DdT implements Serializable {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="real_id")
    private Long realId;
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    @JsonDeserialize(using=DateDeserializer.class)
    private Date data;
    private Integer id;
    private String mezzo;       
    private String causale;     
    private String destinazione;
    @Column(name="vostro_ordine")
    private String vostroOrdine;
    @Column(name="vostro_ordine_del")
    private String vostroOrdineDel;     
    private String tipo;
    @Column(name="aspetto_esteriore")
    private String aspettoEsteriore;     
    private Integer colli;     
    private Double peso;     
    private String porto;   
    private String ritiro; 
    private String annotazioni;
    private Integer progressivo;
    @Type(type="yes_no")
    private Boolean fatturabile;

    @ManyToOne(fetch=FetchType.EAGER,optional=false)
    @JoinColumn(name="cliente")
    private Azienda cliente;

    @OneToMany(fetch=FetchType.LAZY)
    @IndexColumn(name="idx",nullable=false)
    @JoinColumn(name="ddt",nullable=false)
    @Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN})
    private List<Bene> beni;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fattura",insertable=false,updatable=false)
    @JsonSerialize(using=FatturaSerializer.class)
    private Fattura fattura;

    public DdT(){}
    /**
     * Costruttore per la classe DdT che utilizza i dati di un'Azienda per 
     * inizializzare gli attributi del documento di trasporto.
     * @param cliente 
     */
    public DdT(Azienda cliente) {
        this.beni = new ArrayList();
        this.data = new Date();
        this.cliente = cliente;
    }

    /**
     * Costruttore alternativo per la classe DdT, tiene conto di tutti i parametri
     * da considerare, inclusa la lista dei beni presenti nel documento.
     * @param beni
     * @param data
     * @param id
     * @param cliente 
     */
    public DdT(List<Bene> beni, Date data, int id, Azienda cliente) {
        this.beni = beni;
        this.data = data;
        this.id = id;
        this.cliente = cliente;
    }

    public DdT(List<Bene> beni, Date data, int id, Azienda cliente, String mezzo, String causale, String destinazione, 
            String vostroOrdine, String vostroOrdineDel, String tipo, String aspettoEsteriore, int colli, double peso, 
            String porto, String ritiro, String annotazioni, int progressivo, boolean fatturabile) {
        this.beni = beni;
        this.data = data;
        this.id = id;
        this.cliente = cliente;
        this.mezzo = mezzo;
        this.causale = causale;
        this.destinazione = destinazione;
        this.vostroOrdine = vostroOrdine;
        this.vostroOrdineDel = vostroOrdineDel;
        this.tipo = tipo;
        this.aspettoEsteriore = aspettoEsteriore;
        this.colli = colli;
        this.peso = peso;
        this.porto = porto;
        this.ritiro = ritiro;
        this.annotazioni = annotazioni;
        this.progressivo = progressivo;
    }
    
    public void addBene(Bene b){
        this.beni.add(b);
    }
    public void removeBene(int i){
        this.beni.remove(i);
    }

    @Override
    public String toString() {
        return "DdT{" + "realId=" + realId + ", beni=" + beni + ", data=" + data + ", id=" + id + ", cliente=" + cliente + ", mezzo=" + mezzo + ", causale=" + causale + ", destinazione=" + destinazione + ", vostroOrdine=" + vostroOrdine + ", vostroOrdineDel=" + vostroOrdineDel + ", tipo=" + tipo + ", aspettoEsteriore=" + aspettoEsteriore + ", colli=" + colli + ", peso=" + peso + ", porto=" + porto + ", ritiro=" + ritiro + ", annotazioni=" + annotazioni + ", progressivo=" + progressivo + ", fatturabile=" + fatturabile + '}';
    }

    public String getAnnotazioni() {
        return annotazioni;
    }

    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }

    public String getAspettoEsteriore() {
        return aspettoEsteriore;
    }

    public void setAspettoEsteriore(String aspettoEsteriore) {
        this.aspettoEsteriore = aspettoEsteriore;
    }

    public List<Bene> getBeni() {
        return beni;
    }

    public void setBeni(List<Bene> beni) {
        this.beni = beni;
    }

    public String getCausale() {
        return causale;
    }

    public void setCausale(String causale) {
        this.causale = causale;
    }

    public Azienda getCliente() {
        return cliente;
    }

    public void setCliente(Azienda cliente) {
        this.cliente = cliente;
    }

    public Integer getColli() {
        return colli;
    }

    public void setColli(Integer colli) {
        this.colli = colli;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    public void setData(int giorno,int mese,int anno) throws ValidationException {
        this.data=DateUtils.getDate(giorno, mese, anno);
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public void setFattura(Fattura fattura) {
        this.fattura = fattura;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMezzo() {
        return mezzo;
    }

    public void setMezzo(String mezzo) {
        this.mezzo = mezzo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getPorto() {
        return porto;
    }

    public void setPorto(String porto) {
        this.porto = porto;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    public Long getRealId() {
        return realId;
    }

    public void setRealId(Long realId) {
        this.realId = realId;
    }

    public String getRitiro() {
        return ritiro;
    }

    public void setRitiro(String ritiro) {
        this.ritiro = ritiro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getVostroOrdine() {
        return vostroOrdine;
    }

    public void setVostroOrdine(String vostroOrdine) {
        this.vostroOrdine = vostroOrdine;
    }

    public String getVostroOrdineDel() {
        return vostroOrdineDel;
    }

    public void setVostroOrdineDel(String vostroOrdineDel) {
        this.vostroOrdineDel = vostroOrdineDel;
    }
    
    public Boolean isFatturabile() {
    	return fatturabile;
    }

    public Boolean getFatturabile() {
		return fatturabile;
	}
    
	public void setFatturabile(Boolean fatturabile) {
		this.fatturabile = fatturabile;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DdT other = (DdT) obj;
        if (this.realId != other.realId && (this.realId == null || !this.realId.equals(other.realId))) {
            return false;
        }
        if (this.beni != other.beni && (this.beni == null || !this.beni.equals(other.beni))) {
            return false;
        }
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.cliente != other.cliente && (this.cliente == null || !this.cliente.equals(other.cliente))) {
            return false;
        }
        if ((this.mezzo == null) ? (other.mezzo != null) : !this.mezzo.equals(other.mezzo)) {
            return false;
        }
        if ((this.causale == null) ? (other.causale != null) : !this.causale.equals(other.causale)) {
            return false;
        }
        if ((this.destinazione == null) ? (other.destinazione != null) : !this.destinazione.equals(other.destinazione)) {
            return false;
        }
        if ((this.vostroOrdine == null) ? (other.vostroOrdine != null) : !this.vostroOrdine.equals(other.vostroOrdine)) {
            return false;
        }
        if ((this.vostroOrdineDel == null) ? (other.vostroOrdineDel != null) : !this.vostroOrdineDel.equals(other.vostroOrdineDel)) {
            return false;
        }
        if ((this.tipo == null) ? (other.tipo != null) : !this.tipo.equals(other.tipo)) {
            return false;
        }
        if ((this.aspettoEsteriore == null) ? (other.aspettoEsteriore != null) : !this.aspettoEsteriore.equals(other.aspettoEsteriore)) {
            return false;
        }
        if (this.colli != other.colli && (this.colli == null || !this.colli.equals(other.colli))) {
            return false;
        }
        if (this.peso != other.peso && (this.peso == null || !this.peso.equals(other.peso))) {
            return false;
        }
        if ((this.porto == null) ? (other.porto != null) : !this.porto.equals(other.porto)) {
            return false;
        }
        if ((this.ritiro == null) ? (other.ritiro != null) : !this.ritiro.equals(other.ritiro)) {
            return false;
        }
        if ((this.annotazioni == null) ? (other.annotazioni != null) : !this.annotazioni.equals(other.annotazioni)) {
            return false;
        }
        if (this.progressivo != other.progressivo && (this.progressivo == null || !this.progressivo.equals(other.progressivo))) {
            return false;
        }
        if (this.fatturabile != other.fatturabile && (this.fatturabile == null || !this.fatturabile.equals(other.fatturabile))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.realId != null ? this.realId.hashCode() : 0);
        hash = 97 * hash + (this.beni != null ? this.beni.hashCode() : 0);
        hash = 97 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.cliente != null ? this.cliente.hashCode() : 0);
        hash = 97 * hash + (this.mezzo != null ? this.mezzo.hashCode() : 0);
        hash = 97 * hash + (this.causale != null ? this.causale.hashCode() : 0);
        hash = 97 * hash + (this.destinazione != null ? this.destinazione.hashCode() : 0);
        hash = 97 * hash + (this.vostroOrdine != null ? this.vostroOrdine.hashCode() : 0);
        hash = 97 * hash + (this.vostroOrdineDel != null ? this.vostroOrdineDel.hashCode() : 0);
        hash = 97 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        hash = 97 * hash + (this.aspettoEsteriore != null ? this.aspettoEsteriore.hashCode() : 0);
        hash = 97 * hash + (this.colli != null ? this.colli.hashCode() : 0);
        hash = 97 * hash + (this.peso != null ? this.peso.hashCode() : 0);
        hash = 97 * hash + (this.porto != null ? this.porto.hashCode() : 0);
        hash = 97 * hash + (this.ritiro != null ? this.ritiro.hashCode() : 0);
        hash = 97 * hash + (this.annotazioni != null ? this.annotazioni.hashCode() : 0);
        hash = 97 * hash + (this.progressivo != null ? this.progressivo.hashCode() : 0);
        hash = 97 * hash + (this.fatturabile != null ? this.fatturabile.hashCode() : 0);
        return hash;
    }

    
}
