/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.statistiche;

import java.math.BigDecimal;
import java.util.Date;

import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.FatturaInterface;

/**
 *
 * @author vincenzo
 */
public class StatisticheFattura {
    private Integer id;
    private AziendaInterface cliente;
    private Date emissione;
    private BigDecimal netto;
    private BigDecimal iva;
    private BigDecimal totale;
    private BigDecimal capiTagliati;

    public StatisticheFattura(FatturaInterface fattura, BigDecimal capiTagliati) {
        this.id = fattura.getId();
        this.cliente = fattura.getCliente();
        this.netto = fattura.getNetto();
        this.iva = fattura.getIva();
        this.totale = fattura.getTotale();
        this.capiTagliati = capiTagliati;
    }

    public StatisticheFattura() {
    }

    public BigDecimal getCapiTagliati() {
        return capiTagliati;
    }

    public void setCapiTagliati(BigDecimal capiTagliati) {
        this.capiTagliati = capiTagliati;
    }

    public AziendaInterface getCliente() {
        return cliente;
    }

    public void setCliente(AziendaInterface cliente) {
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

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getNetto() {
        return netto;
    }

    public void setNetto(BigDecimal netto) {
        this.netto = netto;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }
    
}
