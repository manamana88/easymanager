
package progettotlp.classes;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import progettotlp.rest.utils.DateDeserializer;
import progettotlp.rest.utils.DateSerializer;

/**
 * Azienda rappresenta sia l'ente che emette le fatture che i clienti di quest'ente, 
 * registrati nel database interno dell'applicazione.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
@Entity
@Table(name="azienda")
public class Azienda implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(name="p_iva")
    private String pIva;
    @Column(name="cod_fis")
    private String codFis;
    private String via;
    private String civico;
    private String cap;
    private String citta;
    private String provincia;
    private String nazione;
    @Column(name="email")
    private String mail;
    private String telefono;
    private String fax;
    @Type(type="yes_no")
    private Boolean tassabile;
    @Type(type="yes_no")
    private Boolean principale = false;
    @Column(name="num_aut")
    private String numeroAutorizzazione;
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    @JsonDeserialize(using=DateDeserializer.class)
    @Column(name="data_aut")
    private Date dataAutorizzazione;
    @Column(name="num_reg")
    private String numeroRegistrazione;
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using=DateSerializer.class)
    @JsonDeserialize(using=DateDeserializer.class)
    @Column(name="data_reg")
    private Date dataRegistrazione;

    public Azienda(){}
    
    public Azienda(String nome, String pIva, String codFis, String via, String civico, 
            String cap, String citta, String provincia, String nazione, String mail, 
            String telefono, String fax, Boolean principale, String numeroAutorizzazione, 
            Date dataAutorizzazione, String numeroRegistrazione, Date dataRegistrazione) {
        this.nome = nome;
        this.pIva = pIva;
        this.codFis = codFis;
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.citta = citta;
        this.provincia=provincia;
        this.nazione = nazione;
        this.mail=mail;
        this.telefono=telefono;
        this.fax=fax;
        this.principale=principale;
        this.numeroAutorizzazione=numeroAutorizzazione;
        this.dataAutorizzazione=dataAutorizzazione;
        this.numeroRegistrazione=numeroRegistrazione;
        this.dataRegistrazione=dataRegistrazione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il numero di fax di un'{@link Azienda}.
     * @return String fax : numero di fax dell'Azienda.
     */
    public String getFax() {
        return fax;
    }

    /**
     * Imposta il numero di fax di un'{@link Azienda}.
     * @param fax 
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * Restituisce la provincia in cui si trova la sede di un'Azienda.
     * @return String provincia : la provincia in cui si trova l'Azienda.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Imposta la provincia in cui si trova la sede di un'Azienda.
     * @param provincia 
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Restituisce il numero di telefono dell'Azienda.
     * @return String telefono : il numero di telefono dell'Azienda.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il numero di telefono dell'Azienda.
     * @param telefono 
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    /**
     * Restituisce il CAP per un'Azienda. 
     * @return String CAP : il numero di CAP dell'Azienda.
     */
    public String getCap() {
        return cap;
    }

    /**
     * Imposta il CAP dell'Azienda.
     * @param cap 
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * Restituisce la Città in cui si trova la sede di un'Azienda
     * @return String citta : la città in cui si trova l'azienda.
     */
    public String getCitta() {
        return citta;
    }

    /**
     * Imposta la Città in cui si trova la sede dell'Azienda.
     * @param citta 
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * Restituisce il numero civico della sede dell'Azienda
     * @return String civico : il numero civico dell'Azienda.
     */
    public String getCivico() {
        return civico;
    }

    /**
     * Imposta il numero civico di un'Azienda.
     * @param civico 
     */
    public void setCivico(String civico) {
        this.civico = civico;
    }

    /**
     * Restituisce il codice fiscale di un'Azienda.
     * @return String cod_fis : il codice fiscale dell'Azienda.
     */
    public String getCodFis() {
        return codFis;
    }

    /**
     * Imposta il codice fiscale di un'Azienda.
     * @param cod_fis 
     */
    public void setCodFis(String cod_fis) {
        this.codFis = cod_fis;
    }

    /**
     * Restituisce l'indirizzo email di un'Azienda.
     * @return String mail : indirizzo email dell'Azienda.
     */
    public String getMail() {
        return mail;
    }
   
    /**
     * Imposta l'indirizzo email di un'Azienda.
     * @param mail 
     */
    public void setMail(String mail) {
        this.mail=mail;
    }

    /**
     * Restituisce la Nazione in cui si trova l'Azienda.
     * @return String nazione : la Nazione dell'Azienda. 
     */
    public String getNazione() {
        return nazione;
    }

    /**
     * Imposta la Nazione in cui si trova l'Azienda.
     * @param nazione 
     */
    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    /**
     * Restituisce il nome dell'Azienda.
     * @return String nome : il nome dell'Azienda.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'Azienda
     * @param nome 
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** 
     * Restituisce la partita iva di un'Azienda.
     * @return String p_iva : la partita iva dell'Azienda.
     */
    public String getPIva() {
        return pIva;
    }

    /**
     * Imposta la partita iva di un'Azienda.
     * @param p_iva 
     */
    public void setPIva(String p_iva) {
        this.pIva = p_iva;
    }

    /**
     * Restituisce la via in cui si trova un'Azienda.
     * @return String via : la via in cui si trova l'Azienda.
     */
    public String getVia() {
        return via;
    }

    /**
     * Imposta la via in cui si trova l'Azienda
     * @param via 
     */
    public void setVia(String via) {
        this.via = via;
    }

    public String getpIva() {
        return pIva;
    }

    public void setpIva(String pIva) {
        this.pIva = pIva;
    }

    public Boolean isPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }

    public Boolean isTassabile() {
        return tassabile;
    }

    public Boolean getTassabile() {
        return tassabile;
    }

    public void setTassabile(Boolean tassabile) {
        this.tassabile = tassabile;
    }

    public String getNumeroAutorizzazione() {
		return numeroAutorizzazione;
	}

	public void setNumeroAutorizzazione(String numeroAutorizzazione) {
		this.numeroAutorizzazione = numeroAutorizzazione;
	}

	public Date getDataAutorizzazione() {
		return dataAutorizzazione;
	}

	public void setDataAutorizzazione(Date dataAutorizzazione) {
		this.dataAutorizzazione = dataAutorizzazione;
	}

	public String getNumeroRegistrazione() {
		return numeroRegistrazione;
	}

	public void setNumeroRegistrazione(String numeroRegistrazione) {
		this.numeroRegistrazione = numeroRegistrazione;
	}

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	@Override
	public String toString() {
		return "Azienda [id=" + id + ", nome=" + nome + ", pIva=" + pIva + ", codFis=" + codFis + ", via=" + via
				+ ", civico=" + civico + ", cap=" + cap + ", citta=" + citta + ", provincia=" + provincia + ", nazione="
				+ nazione + ", mail=" + mail + ", telefono=" + telefono + ", fax=" + fax + ", tassabile=" + tassabile
				+ ", principale=" + principale + ", numeroAutorizzazione=" + numeroAutorizzazione
				+ ", dataAutorizzazione=" + dataAutorizzazione + ", numeroRegistrazione=" + numeroRegistrazione
				+ ", dataRegistrazione=" + dataRegistrazione + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cap == null) ? 0 : cap.hashCode());
		result = prime * result + ((citta == null) ? 0 : citta.hashCode());
		result = prime * result + ((civico == null) ? 0 : civico.hashCode());
		result = prime * result + ((codFis == null) ? 0 : codFis.hashCode());
		result = prime * result + ((dataAutorizzazione == null) ? 0 : dataAutorizzazione.hashCode());
		result = prime * result + ((dataRegistrazione == null) ? 0 : dataRegistrazione.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((nazione == null) ? 0 : nazione.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numeroAutorizzazione == null) ? 0 : numeroAutorizzazione.hashCode());
		result = prime * result + ((numeroRegistrazione == null) ? 0 : numeroRegistrazione.hashCode());
		result = prime * result + ((pIva == null) ? 0 : pIva.hashCode());
		result = prime * result + ((principale == null) ? 0 : principale.hashCode());
		result = prime * result + ((provincia == null) ? 0 : provincia.hashCode());
		result = prime * result + ((tassabile == null) ? 0 : tassabile.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((via == null) ? 0 : via.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Azienda other = (Azienda) obj;
		if (cap == null) {
			if (other.cap != null)
				return false;
		} else if (!cap.equals(other.cap))
			return false;
		if (citta == null) {
			if (other.citta != null)
				return false;
		} else if (!citta.equals(other.citta))
			return false;
		if (civico == null) {
			if (other.civico != null)
				return false;
		} else if (!civico.equals(other.civico))
			return false;
		if (codFis == null) {
			if (other.codFis != null)
				return false;
		} else if (!codFis.equals(other.codFis))
			return false;
		if (dataAutorizzazione == null) {
			if (other.dataAutorizzazione != null)
				return false;
		} else if (!dataAutorizzazione.equals(other.dataAutorizzazione))
			return false;
		if (dataRegistrazione == null) {
			if (other.dataRegistrazione != null)
				return false;
		} else if (!dataRegistrazione.equals(other.dataRegistrazione))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (nazione == null) {
			if (other.nazione != null)
				return false;
		} else if (!nazione.equals(other.nazione))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numeroAutorizzazione == null) {
			if (other.numeroAutorizzazione != null)
				return false;
		} else if (!numeroAutorizzazione.equals(other.numeroAutorizzazione))
			return false;
		if (numeroRegistrazione == null) {
			if (other.numeroRegistrazione != null)
				return false;
		} else if (!numeroRegistrazione.equals(other.numeroRegistrazione))
			return false;
		if (pIva == null) {
			if (other.pIva != null)
				return false;
		} else if (!pIva.equals(other.pIva))
			return false;
		if (principale == null) {
			if (other.principale != null)
				return false;
		} else if (!principale.equals(other.principale))
			return false;
		if (provincia == null) {
			if (other.provincia != null)
				return false;
		} else if (!provincia.equals(other.provincia))
			return false;
		if (tassabile == null) {
			if (other.tassabile != null)
				return false;
		} else if (!tassabile.equals(other.tassabile))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (via == null) {
			if (other.via != null)
				return false;
		} else if (!via.equals(other.via))
			return false;
		return true;
	}


    

}
