/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.Date;
import progettotlp.classes.Bene;
import progettotlp.facilities.DateUtils;

/**
 *
 * @author vincenzo
 */
public class LastSameBeneFatturatoInfos {
    private Integer fatturaId;
    private Date fatturaEmissione;
    private Bene bene;

    public LastSameBeneFatturatoInfos(Integer fatturaId, Date fatturaEmissione, Bene b) {
        this.fatturaId = fatturaId;
        this.fatturaEmissione = fatturaEmissione;
        this.bene = b;
    }

    public String getFatturaRef(){
        return fatturaId+" del "+DateUtils.formatDate(fatturaEmissione);
    }

    public Bene getBene() {
        return bene;
    }

    public void setBene(Bene b) {
        this.bene = b;
    }

    public Date getFatturaEmissione() {
        return fatturaEmissione;
    }

    public void setFatturaEmissione(Date fatturaEmissione) {
        this.fatturaEmissione = fatturaEmissione;
    }

    public Integer getFatturaId() {
        return fatturaId;
    }

    public void setFatturaId(Integer fatturaId) {
        this.fatturaId = fatturaId;
    }

    @Override
    public String toString() {
        return "LastSameBeneFatturatoInfos{" + "fatturaId=" + fatturaId + "fatturaEmissione=" + DateUtils.formatDate(fatturaEmissione) + "bene=" + bene + '}';
    }

    

}
