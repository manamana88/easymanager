/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * Bene è una classe che rappresenta un servizio fornito dall'Azienda, da inserire
 * in un Documento di Trasporto. È caratterizzato da un codice, una commessa, una
 * descrizione, una quantità, un prezzo, un totale e degli attributi supplementari.
 * @author Vincenzo Barrea, Alessio Felicioni
 */
@Entity
@Table(name="bene")
public class Bene implements Serializable{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="cod")
    private String codice;
    private String commessa;
    private String descrizione;
    @Column(name="quantita")
    private Integer qta;
    private Float prezzo;
    private Float tot;
    @Type(type="yes_no")
    private Boolean prototipo=false;
    @Type(type="yes_no")
    private Boolean campionario=false;
    @Column(name="primo_capo")
    @Type(type="yes_no")
    private Boolean primoCapo=false;
    @Type(type="yes_no")
    private Boolean piazzato=false;
    @Column(name="adesivato")
    @Type(type="yes_no")
    private Boolean interamenteAdesivato=false;

    public Bene(){}
    public Bene(String codice, String commessa, String descrizione, int qta, boolean prototipo, boolean campionario, boolean primoCapo, boolean piazzato, boolean interamenteAdesivato) {
        this.codice = codice;
        this.commessa = commessa;
        this.descrizione = descrizione;
        this.qta = qta;
        this.prototipo = prototipo;
        this.campionario = campionario;
        this.primoCapo = primoCapo;
        this.piazzato = piazzato;
        this.interamenteAdesivato = interamenteAdesivato;
    }

    public Bene(String codice, String commessa, String descrizione, int qta, Float prezzo, Float tot, boolean prototipo, boolean campionario, boolean primoCapo, boolean piazzato, boolean interamenteAdesivato) {
        this.codice = codice;
        this.commessa = commessa;
        this.descrizione = descrizione;
        this.qta = qta;
        this.prezzo = prezzo;
        this.tot = tot;
        this.prototipo = prototipo;
        this.campionario = campionario;
        this.primoCapo = primoCapo;
        this.piazzato = piazzato;
        this.interamenteAdesivato = interamenteAdesivato;
    }

    @Override
    public String toString() {
        return "Bene{" + "id=" + id + "codice=" + codice + "commessa=" + commessa + "descrizione=" + descrizione + "qta=" + qta + "prezzo=" + prezzo + "tot=" + tot + "prototipo=" + prototipo + "campionario=" + campionario + "primoCapo=" + primoCapo + "piazzato=" + piazzato + "interamenteAdesivato=" + interamenteAdesivato + '}';
    }

    public Boolean getCampionario() {
        return campionario;
    }

    public void setCampionario(Boolean campionario) {
        this.campionario = campionario;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCommessa() {
        return commessa;
    }

    public void setCommessa(String commessa) {
        this.commessa = commessa;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInteramenteAdesivato() {
        return interamenteAdesivato;
    }

    public void setInteramenteAdesivato(Boolean interamenteAdesivato) {
        this.interamenteAdesivato = interamenteAdesivato;
    }

    public Boolean getPiazzato() {
        return piazzato;
    }

    public void setPiazzato(Boolean piazzato) {
        this.piazzato = piazzato;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public Boolean getPrimoCapo() {
        return primoCapo;
    }

    public void setPrimoCapo(Boolean primoCapo) {
        this.primoCapo = primoCapo;
    }

    public Boolean getPrototipo() {
        return prototipo;
    }

    public void setPrototipo(Boolean prototipo) {
        this.prototipo = prototipo;
    }

    public Integer getQta() {
        return qta;
    }

    public void setQta(Integer qta) {
        this.qta = qta;
    }

    public Float getTot() {
        return tot;
    }

    public void setTot(Float tot) {
        this.tot = tot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bene other = (Bene) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.codice == null) ? (other.codice != null) : !this.codice.equals(other.codice)) {
            return false;
        }
        if ((this.commessa == null) ? (other.commessa != null) : !this.commessa.equals(other.commessa)) {
            return false;
        }
        if ((this.descrizione == null) ? (other.descrizione != null) : !this.descrizione.equals(other.descrizione)) {
            return false;
        }
        if (this.qta != other.qta && (this.qta == null || !this.qta.equals(other.qta))) {
            return false;
        }
        if (this.prezzo != other.prezzo && (this.prezzo == null || !this.prezzo.equals(other.prezzo))) {
            return false;
        }
        if (this.tot != other.tot && (this.tot == null || !this.tot.equals(other.tot))) {
            return false;
        }
        if (this.prototipo != other.prototipo && (this.prototipo == null || !this.prototipo.equals(other.prototipo))) {
            return false;
        }
        if (this.campionario != other.campionario && (this.campionario == null || !this.campionario.equals(other.campionario))) {
            return false;
        }
        if (this.primoCapo != other.primoCapo && (this.primoCapo == null || !this.primoCapo.equals(other.primoCapo))) {
            return false;
        }
        if (this.piazzato != other.piazzato && (this.piazzato == null || !this.piazzato.equals(other.piazzato))) {
            return false;
        }
        if (this.interamenteAdesivato != other.interamenteAdesivato && (this.interamenteAdesivato == null || !this.interamenteAdesivato.equals(other.interamenteAdesivato))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.codice != null ? this.codice.hashCode() : 0);
        hash = 37 * hash + (this.commessa != null ? this.commessa.hashCode() : 0);
        hash = 37 * hash + (this.descrizione != null ? this.descrizione.hashCode() : 0);
        hash = 37 * hash + (this.qta != null ? this.qta.hashCode() : 0);
        hash = 37 * hash + (this.prezzo != null ? this.prezzo.hashCode() : 0);
        hash = 37 * hash + (this.tot != null ? this.tot.hashCode() : 0);
        hash = 37 * hash + (this.prototipo != null ? this.prototipo.hashCode() : 0);
        hash = 37 * hash + (this.campionario != null ? this.campionario.hashCode() : 0);
        hash = 37 * hash + (this.primoCapo != null ? this.primoCapo.hashCode() : 0);
        hash = 37 * hash + (this.piazzato != null ? this.piazzato.hashCode() : 0);
        hash = 37 * hash + (this.interamenteAdesivato != null ? this.interamenteAdesivato.hashCode() : 0);
        return hash;
    }

    
    
}
