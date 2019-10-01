/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.Date;

import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.BeneInterface;

/**
 *
 * @author vincenzo
 */
public class LastSameBeneFatturatoInfos {
    private Integer fatturaId;
    private Date fatturaEmissione;
    private BeneInterface bene;

    public LastSameBeneFatturatoInfos(Integer fatturaId, Date fatturaEmissione, BeneInterface b) {
        this.fatturaId = fatturaId;
        this.fatturaEmissione = fatturaEmissione;
        this.bene = b;
    }

    public String getFatturaRef(){
        return fatturaId+" del "+DateUtils.formatDate(fatturaEmissione);
    }

    public BeneInterface getBene() {
        return bene;
    }

    public void setBene(BeneInterface b) {
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
