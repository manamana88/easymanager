
package progettotlp.classes;

import java.io.Serializable;
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

import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.DateUtils;
import progettotlp.rest.utils.DateDeserializer;
import progettotlp.rest.utils.DateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * La classe Fattura rappresenta un documento di fatturazione emesso da un'Azienda.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
@Entity
@Table(name="fattura")
public class Fattura implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="real_id")
    private Long realId;
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    @JsonDeserialize(using=DateDeserializer.class)
    private Date emissione;
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    @JsonDeserialize(using=DateDeserializer.class)
    private Date scadenza;
    private Integer id;
    private Float netto=0F;
    @Column(name="iva_perc")
    private Float ivaPerc=0F;
    private Float iva=0F;
    private Float totale=0F;
    private String bollo;

    @ManyToOne(fetch=FetchType.EAGER,optional=false)
    @JoinColumn(name="cliente")
    private Azienda cliente;
    @OneToMany(fetch=FetchType.LAZY)
    @IndexColumn(name="idx",nullable=false)
    @JoinColumn(name="fattura")
    @Cascade(value={CascadeType.SAVE_UPDATE})
    private List<DdT> ddt;

    public Fattura(){}
    public Fattura(List<DdT> ddt, Date emissione, Date scadenza, int id, Azienda cliente, float netto, float ivaPerc, float iva, float totale, String bollo) {
        this.ddt = ddt; //OK
        this.emissione = emissione;
        this.scadenza = scadenza;
        this.id = id;
        this.cliente = cliente;
        this.netto = netto;
        this.ivaPerc = ivaPerc;
        this.iva = iva;
        this.totale = totale;
        this.bollo = bollo;
    }

    public Long getRealId() {
        return realId;
    }

    public void setRealId(Long realId) {
        this.realId = realId;
    }

    public Azienda getCliente() {
        return cliente;
    }

    public void setCliente(Azienda cliente) {
        this.cliente = cliente;
    }

    public List<DdT> getDdt() {
        return ddt;
    }

    public void setDdt(List<DdT> ddt) {
        this.ddt = ddt;
    }

    public Date getEmissione() {
        return emissione;
    }

    public void setEmissione(Date emissione) {
        this.emissione = emissione;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getIvaPerc() {
        return ivaPerc;
    }

    public void setIvaPerc(Float ivaPerc) {
        this.ivaPerc = ivaPerc;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getNetto() {
        return netto;
    }

    public void setNetto(Float netto) {
        this.netto = netto;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public Float getTotale() {
        return totale;
    }

    public void setTotale(Float totale) {
        this.totale = totale;
    }
   
    public String getBollo() {
		return bollo;
	}

    public void setBollo(String bollo) {
		this.bollo = bollo;
	}
	
    /**
     * Imposta la data di emissione di una fattura.
     * @param giorno
     * @param mese
     * @param anno 
     */
    public void setEmissione(int giorno, int mese, int anno, int scadenza) throws ValidationException {
        this.emissione=DateUtils.getDate(giorno, mese, anno);
        this.scadenza=DateUtils.calcolaScadenza(emissione, scadenza);
    }

    @Override
    public String toString() {
        return "Fattura{" + "realId=" + realId + ", ddt=" + ddt + ", emissione=" + emissione + ", scadenza=" + scadenza + ", id=" + id + ", cliente=" + cliente + ", netto=" + netto + ", iva=" + iva + ", totale=" + totale + ", bollo=" + bollo + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fattura other = (Fattura) obj;
        if (this.realId != other.realId && (this.realId == null || !this.realId.equals(other.realId))) {
            return false;
        }
        if (this.ddt != other.ddt && (this.ddt == null || !this.ddt.equals(other.ddt))) {
            return false;
        }
        if (this.emissione != other.emissione && (this.emissione == null || !this.emissione.equals(other.emissione))) {
            return false;
        }
        if (this.scadenza != other.scadenza && (this.scadenza == null || !this.scadenza.equals(other.scadenza))) {
            return false;
        }
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.cliente != other.cliente && (this.cliente == null || !this.cliente.equals(other.cliente))) {
            return false;
        }
        if (this.netto != other.netto && (this.netto == null || !this.netto.equals(other.netto))) {
            return false;
        }
        if (this.ivaPerc != other.ivaPerc && (this.ivaPerc == null || !this.ivaPerc.equals(other.ivaPerc))) {
            return false;
        }
        if (this.iva != other.iva && (this.iva == null || !this.iva.equals(other.iva))) {
            return false;
        }
        if (this.totale != other.totale && (this.totale == null || !this.totale.equals(other.totale))) {
            return false;
        }
        if (this.bollo != other.bollo && (this.bollo == null || !this.bollo.equals(other.bollo))) {
        	return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.realId != null ? this.realId.hashCode() : 0);
        hash = 67 * hash + (this.ddt != null ? this.ddt.hashCode() : 0);
        hash = 67 * hash + (this.emissione != null ? this.emissione.hashCode() : 0);
        hash = 67 * hash + (this.scadenza != null ? this.scadenza.hashCode() : 0);
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 67 * hash + (this.cliente != null ? this.cliente.hashCode() : 0);
        hash = 67 * hash + (this.netto != null ? this.netto.hashCode() : 0);
        hash = 67 * hash + (this.ivaPerc != null ? this.ivaPerc.hashCode() : 0);
        hash = 67 * hash + (this.iva != null ? this.iva.hashCode() : 0);
        hash = 67 * hash + (this.totale != null ? this.totale.hashCode() : 0);
        hash = 67 * hash + (this.bollo != null ? this.bollo.hashCode() : 0);
        return hash;
    }

    
}
