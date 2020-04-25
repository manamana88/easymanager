
package progettotlp.classes;

import java.io.Serializable;
import java.math.BigDecimal;
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
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;
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
public class Fattura implements Serializable, FatturaInterface {
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
    private BigDecimal netto=new BigDecimal("0");
    @Column(name="iva_perc")
    private BigDecimal ivaPerc=new BigDecimal("0");
    private BigDecimal iva=new BigDecimal("0");
    private BigDecimal totale=new BigDecimal("0");
    private String bollo;

    @ManyToOne(fetch=FetchType.EAGER,optional=false, targetEntity=Azienda.class)
    @JoinColumn(name="cliente")
    private AziendaInterface cliente;
    @OneToMany(fetch=FetchType.LAZY, targetEntity=DdT.class)
    @IndexColumn(name="idx",nullable=false)
    @JoinColumn(name="fattura")
    @Cascade(value={CascadeType.SAVE_UPDATE})
    private List<DdTInterface> ddt;

    public Fattura(){}
    public Fattura(List<DdTInterface> ddt, Date emissione, Date scadenza, int id, AziendaInterface cliente, BigDecimal netto, BigDecimal ivaPerc, BigDecimal iva, BigDecimal totale, String bollo) {
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

    @Override
	public Long getRealId() {
        return realId;
    }

    @Override
	public void setRealId(Long realId) {
        this.realId = realId;
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
	public List<DdTInterface> getDdt() {
        return ddt;
    }

    @Override
	public void setDdt(List<DdTInterface> ddt) {
        this.ddt = ddt;
    }

    @Override
	public Date getEmissione() {
        return emissione;
    }

    @Override
	public void setEmissione(Date emissione) {
        this.emissione = emissione;
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
	public BigDecimal getIvaPerc() {
        return ivaPerc;
    }

    @Override
	public void setIvaPerc(BigDecimal ivaPerc) {
        this.ivaPerc = ivaPerc;
    }

    @Override
	public BigDecimal getIva() {
        return iva;
    }

    @Override
	public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    @Override
	public BigDecimal getNetto() {
        return netto;
    }

    @Override
	public void setNetto(BigDecimal netto) {
        this.netto = netto;
    }

    @Override
	public Date getScadenza() {
        return scadenza;
    }

    @Override
	public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    @Override
	public BigDecimal getTotale() {
        return totale;
    }

    @Override
	public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }
   
    @Override
	public String getBollo() {
		return bollo;
	}

    @Override
	public void setBollo(String bollo) {
		this.bollo = bollo;
	}
	
    /**
     * Imposta la data di emissione di una fattura.
     * @param giorno
     * @param mese
     * @param anno 
     */
    @Override
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
