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
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;
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
public class DdT implements Serializable, DdTInterface {

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

    @ManyToOne(fetch=FetchType.EAGER,optional=false,targetEntity=Azienda.class)
    @JoinColumn(name="cliente")
    private AziendaInterface cliente;

    @OneToMany(fetch=FetchType.LAZY, targetEntity=Bene.class, orphanRemoval = true)
    @IndexColumn(name="idx",nullable=false)
    @JoinColumn(name="ddt",nullable=false)
    @Cascade(value={CascadeType.SAVE_UPDATE})
    private List<BeneInterface> beni;

    @ManyToOne(fetch=FetchType.LAZY, targetEntity=Fattura.class)
    @JoinColumn(name="fattura",insertable=false,updatable=false)
    @JsonSerialize(using=FatturaSerializer.class)
    private FatturaInterface fattura;

    public DdT(){}
    /**
     * Costruttore per la classe DdT che utilizza i dati di un'Azienda per 
     * inizializzare gli attributi del documento di trasporto.
     * @param cliente 
     */
    public DdT(Azienda cliente) {
        this.beni = new ArrayList<>();
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
    public DdT(List<BeneInterface> beni, Date data, int id, Azienda cliente) {
        this.beni = beni;
        this.data = data;
        this.id = id;
        this.cliente = cliente;
    }

    public DdT(List<BeneInterface> beni, Date data, int id, Azienda cliente, String mezzo, String causale, String destinazione, 
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
    
    @Override
	public void addBene(BeneInterface b){
        this.beni.add(b);
    }
    @Override
	public void removeBene(int i){
        this.beni.remove(i);
    }

    @Override
    public String toString() {
        return "DdT{" + "realId=" + realId + ", beni=" + beni + ", data=" + data + ", id=" + id + ", cliente=" + cliente + ", mezzo=" + mezzo + ", causale=" + causale + ", destinazione=" + destinazione + ", vostroOrdine=" + vostroOrdine + ", vostroOrdineDel=" + vostroOrdineDel + ", tipo=" + tipo + ", aspettoEsteriore=" + aspettoEsteriore + ", colli=" + colli + ", peso=" + peso + ", porto=" + porto + ", ritiro=" + ritiro + ", annotazioni=" + annotazioni + ", progressivo=" + progressivo + ", fatturabile=" + fatturabile + '}';
    }

    @Override
	public String getAnnotazioni() {
        return annotazioni;
    }

    @Override
	public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }

    @Override
	public String getAspettoEsteriore() {
        return aspettoEsteriore;
    }

    @Override
	public void setAspettoEsteriore(String aspettoEsteriore) {
        this.aspettoEsteriore = aspettoEsteriore;
    }

    @Override
	public List<BeneInterface> getBeni() {
        return beni;
    }

    @Override
	public void setBeni(List<BeneInterface> beni) {
        this.beni = beni;
    }

    @Override
	public String getCausale() {
        return causale;
    }

    @Override
	public void setCausale(String causale) {
        this.causale = causale;
    }

    @Override
	public AziendaInterface getCliente() {
        return cliente;
    }

    @Override
	public void setCliente(AziendaInterface cliente) {
        this.cliente = cliente;
    }

    @Override
	public Integer getColli() {
        return colli;
    }

    @Override
	public void setColli(Integer colli) {
        this.colli = colli;
    }

    @Override
	public Date getData() {
        return data;
    }

    @Override
	public void setData(Date data) {
        this.data = data;
    }
    
    @Override
	public void setData(int giorno,int mese,int anno) throws ValidationException {
        this.data=DateUtils.getDate(giorno, mese, anno);
    }

    @Override
	public String getDestinazione() {
        return destinazione;
    }

    @Override
	public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    @Override
	public FatturaInterface getFattura() {
        return fattura;
    }

    @Override
	public void setFattura(FatturaInterface fattura) {
        this.fattura = fattura;
    }

    @Override
	public Integer getId() {
        return id;
    }

    @Override
	public void setId(Integer id) {
        this.id = id;
    }

    @Override
	public String getMezzo() {
        return mezzo;
    }

    @Override
	public void setMezzo(String mezzo) {
        this.mezzo = mezzo;
    }

    @Override
	public Double getPeso() {
        return peso;
    }

    @Override
	public void setPeso(Double peso) {
        this.peso = peso;
    }

    @Override
	public String getPorto() {
        return porto;
    }

    @Override
	public void setPorto(String porto) {
        this.porto = porto;
    }

    @Override
	public Integer getProgressivo() {
        return progressivo;
    }

    @Override
	public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    @Override
	public Long getRealId() {
        return realId;
    }

    @Override
	public void setRealId(Long realId) {
        this.realId = realId;
    }

    @Override
	public String getRitiro() {
        return ritiro;
    }

    @Override
	public void setRitiro(String ritiro) {
        this.ritiro = ritiro;
    }

    @Override
	public String getTipo() {
        return tipo;
    }

    @Override
	public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
	public String getVostroOrdine() {
        return vostroOrdine;
    }

    @Override
	public void setVostroOrdine(String vostroOrdine) {
        this.vostroOrdine = vostroOrdine;
    }

    @Override
	public String getVostroOrdineDel() {
        return vostroOrdineDel;
    }

    @Override
	public void setVostroOrdineDel(String vostroOrdineDel) {
        this.vostroOrdineDel = vostroOrdineDel;
    }
    
    @Override
	public Boolean isFatturabile() {
    	return fatturabile;
    }

    @Override
	public Boolean getFatturabile() {
		return fatturabile;
	}
    
	@Override
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
