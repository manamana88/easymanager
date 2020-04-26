package progettotlp.interfaces;

import java.math.BigDecimal;
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

	BigDecimal getIvaPerc();

	void setIvaPerc(BigDecimal ivaPerc);

	BigDecimal getIva();

	void setIva(BigDecimal iva);

	BigDecimal getNetto();

	void setNetto(BigDecimal netto);

	Date getScadenza();

	void setScadenza(Date scadenza);

	BigDecimal getTotale();

	void setTotale(BigDecimal totale);

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