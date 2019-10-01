package progettotlp.interfaces;

import java.util.Date;
import java.util.List;

import progettotlp.exceptions.toprint.ValidationException;

public interface FatturaInterface {

	Long getRealId();

	void setRealId(Long realId);

	AziendaInterface getCliente();

	void setCliente(AziendaInterface cliente);

	List<DdTInterface> getDdt();

	void setDdt(List<DdTInterface> ddt);

	Date getEmissione();

	void setEmissione(Date emissione);

	Integer getId();

	void setId(Integer id);

	Float getIvaPerc();

	void setIvaPerc(Float ivaPerc);

	Float getIva();

	void setIva(Float iva);

	Float getNetto();

	void setNetto(Float netto);

	Date getScadenza();

	void setScadenza(Date scadenza);

	Float getTotale();

	void setTotale(Float totale);

	String getBollo();

	void setBollo(String bollo);

	/**
	 * Imposta la data di emissione di una fattura.
	 * @param giorno
	 * @param mese
	 * @param anno 
	 */
	void setEmissione(int giorno, int mese, int anno, int scadenza) throws ValidationException;

}