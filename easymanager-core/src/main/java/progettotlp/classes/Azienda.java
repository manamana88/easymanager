
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

import progettotlp.interfaces.AziendaInterface;
import progettotlp.rest.utils.DateDeserializer;
import progettotlp.rest.utils.DateSerializer;

/**
 * Azienda rappresenta sia l'ente che emette le fatture che i clienti di quest'ente, 
 * registrati nel database interno dell'applicazione.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
@Entity
@Table(name="azienda")
public class Azienda implements Serializable, AziendaInterface {
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
    @Column(name="num_prot")
    private String numeroProtocollo;
    private String pec;
    @Column(name="codice_fattura_pa")
    private String codiceFatturaPa;

    public Azienda(){}
    
    public Azienda(String nome, String pIva, String codFis, String via, String civico, 
            String cap, String citta, String provincia, String nazione, String mail, 
            String telefono, String fax, Boolean principale, String numeroProtocollo,
            String pec, String codiceFatturaPa) {
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
        this.numeroProtocollo=numeroProtocollo;
		this.pec = pec;
		this.codiceFatturaPa = codiceFatturaPa;
    }

    @Override
	public Long getId() {
        return id;
    }

    @Override
	public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il numero di fax di un'{@link Azienda}.
     * @return String fax : numero di fax dell'Azienda.
     */
    @Override
	public String getFax() {
        return fax;
    }

    /**
     * Imposta il numero di fax di un'{@link Azienda}.
     * @param fax 
     */
    @Override
	public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * Restituisce la provincia in cui si trova la sede di un'Azienda.
     * @return String provincia : la provincia in cui si trova l'Azienda.
     */
    @Override
	public String getProvincia() {
        return provincia;
    }

    /**
     * Imposta la provincia in cui si trova la sede di un'Azienda.
     * @param provincia 
     */
    @Override
	public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Restituisce il numero di telefono dell'Azienda.
     * @return String telefono : il numero di telefono dell'Azienda.
     */
    @Override
	public String getTelefono() {
        return telefono;
    }

    /**
     * Imposta il numero di telefono dell'Azienda.
     * @param telefono 
     */
    @Override
	public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    /**
     * Restituisce il CAP per un'Azienda. 
     * @return String CAP : il numero di CAP dell'Azienda.
     */
    @Override
	public String getCap() {
        return cap;
    }

    /**
     * Imposta il CAP dell'Azienda.
     * @param cap 
     */
    @Override
	public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * Restituisce la Città in cui si trova la sede di un'Azienda
     * @return String citta : la città in cui si trova l'azienda.
     */
    @Override
	public String getCitta() {
        return citta;
    }

    /**
     * Imposta la Città in cui si trova la sede dell'Azienda.
     * @param citta 
     */
    @Override
	public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * Restituisce il numero civico della sede dell'Azienda
     * @return String civico : il numero civico dell'Azienda.
     */
    @Override
	public String getCivico() {
        return civico;
    }

    /**
     * Imposta il numero civico di un'Azienda.
     * @param civico 
     */
    @Override
	public void setCivico(String civico) {
        this.civico = civico;
    }

    /**
     * Restituisce il codice fiscale di un'Azienda.
     * @return String cod_fis : il codice fiscale dell'Azienda.
     */
    @Override
	public String getCodFis() {
        return codFis;
    }

    /**
     * Imposta il codice fiscale di un'Azienda.
     * @param cod_fis 
     */
    @Override
	public void setCodFis(String cod_fis) {
        this.codFis = cod_fis;
    }

    /**
     * Restituisce l'indirizzo email di un'Azienda.
     * @return String mail : indirizzo email dell'Azienda.
     */
    @Override
	public String getMail() {
        return mail;
    }
   
    /**
     * Imposta l'indirizzo email di un'Azienda.
     * @param mail 
     */
    @Override
	public void setMail(String mail) {
        this.mail=mail;
    }

    /**
     * Restituisce la Nazione in cui si trova l'Azienda.
     * @return String nazione : la Nazione dell'Azienda. 
     */
    @Override
	public String getNazione() {
        return nazione;
    }

    /**
     * Imposta la Nazione in cui si trova l'Azienda.
     * @param nazione 
     */
    @Override
	public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    /**
     * Restituisce il nome dell'Azienda.
     * @return String nome : il nome dell'Azienda.
     */
    @Override
	public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'Azienda
     * @param nome 
     */
    @Override
	public void setNome(String nome) {
        this.nome = nome;
    }

    /** 
     * Restituisce la partita iva di un'Azienda.
     * @return String p_iva : la partita iva dell'Azienda.
     */
    @Override
	public String getPIva() {
        return pIva;
    }

    /**
     * Imposta la partita iva di un'Azienda.
     * @param p_iva 
     */
    @Override
	public void setPIva(String p_iva) {
        this.pIva = p_iva;
    }

    /**
     * Restituisce la via in cui si trova un'Azienda.
     * @return String via : la via in cui si trova l'Azienda.
     */
    @Override
	public String getVia() {
        return via;
    }

    /**
     * Imposta la via in cui si trova l'Azienda
     * @param via 
     */
    @Override
	public void setVia(String via) {
        this.via = via;
    }

    @Override
	public Boolean isPrincipale() {
        return principale;
    }

    @Override
	public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }

    @Override
	public Boolean isTassabile() {
        return tassabile;
    }

    @Override
	public Boolean getTassabile() {
        return tassabile;
    }

    @Override
	public void setTassabile(Boolean tassabile) {
        this.tassabile = tassabile;
    }

    @Override
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	@Override
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	@Override
	public String getPEC() {
		return pec;
	}

	@Override
	public void setPEC(String pec) {
		this.pec = pec;
	}

	@Override
	public String getCodiceFatturaPa() {
		return codiceFatturaPa;
	}

	@Override
	public void setCodiceFatturaPa(String codiceFatturaPa) {
		this.codiceFatturaPa = codiceFatturaPa;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Azienda [id=").append(id).append(", nome=").append(nome).append(", pIva=").append(pIva)
				.append(", codFis=").append(codFis).append(", via=").append(via).append(", civico=").append(civico)
				.append(", cap=").append(cap).append(", citta=").append(citta).append(", provincia=").append(provincia)
				.append(", nazione=").append(nazione).append(", mail=").append(mail).append(", telefono=")
				.append(telefono).append(", fax=").append(fax).append(", tassabile=").append(tassabile)
				.append(", principale=").append(principale).append(", numeroProtocollo=")
				.append(numeroProtocollo).append(", pec=").append(pec).append(", codiceFatturaPa=")
				.append(codiceFatturaPa).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cap == null) ? 0 : cap.hashCode());
		result = prime * result + ((citta == null) ? 0 : citta.hashCode());
		result = prime * result + ((civico == null) ? 0 : civico.hashCode());
		result = prime * result + ((codFis == null) ? 0 : codFis.hashCode());
		result = prime * result + ((codiceFatturaPa == null) ? 0 : codiceFatturaPa.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((nazione == null) ? 0 : nazione.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((numeroProtocollo == null) ? 0 : numeroProtocollo.hashCode());
		result = prime * result + ((pIva == null) ? 0 : pIva.hashCode());
		result = prime * result + ((pec == null) ? 0 : pec.hashCode());
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
		if (codiceFatturaPa == null) {
			if (other.codiceFatturaPa != null)
				return false;
		} else if (!codiceFatturaPa.equals(other.codiceFatturaPa))
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
		if (numeroProtocollo == null) {
			if (other.numeroProtocollo != null)
				return false;
		} else if (!numeroProtocollo.equals(other.numeroProtocollo))
			return false;
		if (pIva == null) {
			if (other.pIva != null)
				return false;
		} else if (!pIva.equals(other.pIva))
			return false;
		if (pec == null) {
			if (other.pec != null)
				return false;
		} else if (!pec.equals(other.pec))
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
