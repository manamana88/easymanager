/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.statistiche;

import java.util.Date;
import progettotlp.classes.Azienda;
import progettotlp.classes.Fattura;

/**
 *
 * @author vincenzo
 */
public class StatisticheFattura {
    private Integer id;
    private Azienda cliente;
    private Date emissione;
    private Float netto;
    private Float iva;
    private Float totale;
    private Float capiTagliati;

    public StatisticheFattura(Fattura fattura, Float capiTagliati) {
        this.id = fattura.getId();
        this.cliente = fattura.getCliente();
        this.netto = fattura.getNetto();
        this.iva = fattura.getIva();
        this.totale = fattura.getTotale();
        this.capiTagliati = capiTagliati;
    }

    public StatisticheFattura() {
    }

    public Float getCapiTagliati() {
        return capiTagliati;
    }

    public void setCapiTagliati(Float capiTagliati) {
        this.capiTagliati = capiTagliati;
    }

    public Azienda getCliente() {
        return cliente;
    }

    public void setCliente(Azienda cliente) {
        this.cliente = cliente;
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

    public Float getTotale() {
        return totale;
    }

    public void setTotale(Float totale) {
        this.totale = totale;
    }
    
}
