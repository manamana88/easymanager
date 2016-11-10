package progettotlp.rest.beans;

import progettotlp.classes.Fattura;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.FatturaUtilities;

public class FatturaBean extends Fattura {

	private Integer scadenzaGiorni;
	private Integer capiTot;
	
	public FatturaBean(Fattura f){
		setCliente(f.getCliente());
		setDdt(f.getDdt());
		setEmissione(f.getEmissione());
		setId(f.getId());
		setIva(f.getIva());
		setIvaPerc(f.getIvaPerc());
		setNetto(f.getNetto());
		setRealId(f.getRealId());
		setScadenza(f.getScadenza());
		setTotale(f.getTotale());
		setBollo(f.getBollo());
		scadenzaGiorni=DateUtils.getTimeFrame(getEmissione(), getScadenza());
		capiTot=FatturaUtilities.getTotCapi(f);
	}

	public Integer getScadenzaGiorni() {
		return scadenzaGiorni;
	}

	public void setScadenzaGiorni(Integer scadenzaGiorni) {
		this.scadenzaGiorni = scadenzaGiorni;
	}

	public Integer getCapiTot() {
		return capiTot;
	}

	public void setCapiTot(Integer capiTot) {
		this.capiTot = capiTot;
	}
	
}
