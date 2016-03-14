
package progettotlp.classes;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

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
    private Boolean principale;

    public Azienda(){}
    
    public Azienda(String nome, String pIva, String codFis, String via, String civico, 
            String cap, String citta, String provincia, String nazione, String mail, String telefono, String fax, Boolean principale) {
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

    @Override
    public String toString() {
        return "Azienda{" + "id=" + id + "nome=" + nome + "pIva=" + pIva + "codFis=" + codFis + "via=" + via + "civico=" + civico + "cap=" + cap + "citta=" + citta + "provincia=" + provincia + "nazione=" + nazione + "mail=" + mail + "telefono=" + telefono + "fax=" + fax + "tassabile=" + tassabile + "principale=" + principale + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Azienda other = (Azienda) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if ((this.pIva == null) ? (other.pIva != null) : !this.pIva.equals(other.pIva)) {
            return false;
        }
        if ((this.codFis == null) ? (other.codFis != null) : !this.codFis.equals(other.codFis)) {
            return false;
        }
        if ((this.via == null) ? (other.via != null) : !this.via.equals(other.via)) {
            return false;
        }
        if ((this.civico == null) ? (other.civico != null) : !this.civico.equals(other.civico)) {
            return false;
        }
        if ((this.cap == null) ? (other.cap != null) : !this.cap.equals(other.cap)) {
            return false;
        }
        if ((this.citta == null) ? (other.citta != null) : !this.citta.equals(other.citta)) {
            return false;
        }
        if ((this.provincia == null) ? (other.provincia != null) : !this.provincia.equals(other.provincia)) {
            return false;
        }
        if ((this.nazione == null) ? (other.nazione != null) : !this.nazione.equals(other.nazione)) {
            return false;
        }
        if ((this.mail == null) ? (other.mail != null) : !this.mail.equals(other.mail)) {
            return false;
        }
        if ((this.telefono == null) ? (other.telefono != null) : !this.telefono.equals(other.telefono)) {
            return false;
        }
        if ((this.fax == null) ? (other.fax != null) : !this.fax.equals(other.fax)) {
            return false;
        }
        if (this.tassabile != other.tassabile && (this.tassabile == null || !this.tassabile.equals(other.tassabile))) {
            return false;
        }
        if (this.principale != other.principale && (this.principale == null || !this.principale.equals(other.principale))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 23 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 23 * hash + (this.pIva != null ? this.pIva.hashCode() : 0);
        hash = 23 * hash + (this.codFis != null ? this.codFis.hashCode() : 0);
        hash = 23 * hash + (this.via != null ? this.via.hashCode() : 0);
        hash = 23 * hash + (this.civico != null ? this.civico.hashCode() : 0);
        hash = 23 * hash + (this.cap != null ? this.cap.hashCode() : 0);
        hash = 23 * hash + (this.citta != null ? this.citta.hashCode() : 0);
        hash = 23 * hash + (this.provincia != null ? this.provincia.hashCode() : 0);
        hash = 23 * hash + (this.nazione != null ? this.nazione.hashCode() : 0);
        hash = 23 * hash + (this.mail != null ? this.mail.hashCode() : 0);
        hash = 23 * hash + (this.telefono != null ? this.telefono.hashCode() : 0);
        hash = 23 * hash + (this.fax != null ? this.fax.hashCode() : 0);
        hash = 23 * hash + (this.tassabile != null ? this.tassabile.hashCode() : 0);
        hash = 23 * hash + (this.principale != null ? this.principale.hashCode() : 0);
        return hash;
    }

    

}
