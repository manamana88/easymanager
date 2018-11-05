package progettotlp.interfaces;

import java.util.Date;

public interface AziendaInterface {

	Long getId();

	void setId(Long id);

	/**
	 * Restituisce il numero di fax di un'{@link Azienda}.
	 * @return String fax : numero di fax dell'Azienda.
	 */
	String getFax();

	/**
	 * Imposta il numero di fax di un'{@link Azienda}.
	 * @param fax 
	 */
	void setFax(String fax);

	/**
	 * Restituisce la provincia in cui si trova la sede di un'Azienda.
	 * @return String provincia : la provincia in cui si trova l'Azienda.
	 */
	String getProvincia();

	/**
	 * Imposta la provincia in cui si trova la sede di un'Azienda.
	 * @param provincia 
	 */
	void setProvincia(String provincia);

	/**
	 * Restituisce il numero di telefono dell'Azienda.
	 * @return String telefono : il numero di telefono dell'Azienda.
	 */
	String getTelefono();

	/**
	 * Imposta il numero di telefono dell'Azienda.
	 * @param telefono 
	 */
	void setTelefono(String telefono);

	/**
	 * Restituisce il CAP per un'Azienda. 
	 * @return String CAP : il numero di CAP dell'Azienda.
	 */
	String getCap();

	/**
	 * Imposta il CAP dell'Azienda.
	 * @param cap 
	 */
	void setCap(String cap);

	/**
	 * Restituisce la Città in cui si trova la sede di un'Azienda
	 * @return String citta : la città in cui si trova l'azienda.
	 */
	String getCitta();

	/**
	 * Imposta la Città in cui si trova la sede dell'Azienda.
	 * @param citta 
	 */
	void setCitta(String citta);

	/**
	 * Restituisce il numero civico della sede dell'Azienda
	 * @return String civico : il numero civico dell'Azienda.
	 */
	String getCivico();

	/**
	 * Imposta il numero civico di un'Azienda.
	 * @param civico 
	 */
	void setCivico(String civico);

	/**
	 * Restituisce il codice fiscale di un'Azienda.
	 * @return String cod_fis : il codice fiscale dell'Azienda.
	 */
	String getCodFis();

	/**
	 * Imposta il codice fiscale di un'Azienda.
	 * @param cod_fis 
	 */
	void setCodFis(String cod_fis);

	/**
	 * Restituisce l'indirizzo email di un'Azienda.
	 * @return String mail : indirizzo email dell'Azienda.
	 */
	String getMail();

	/**
	 * Imposta l'indirizzo email di un'Azienda.
	 * @param mail 
	 */
	void setMail(String mail);

	/**
	 * Restituisce la Nazione in cui si trova l'Azienda.
	 * @return String nazione : la Nazione dell'Azienda. 
	 */
	String getNazione();

	/**
	 * Imposta la Nazione in cui si trova l'Azienda.
	 * @param nazione 
	 */
	void setNazione(String nazione);

	/**
	 * Restituisce il nome dell'Azienda.
	 * @return String nome : il nome dell'Azienda.
	 */
	String getNome();

	/**
	 * Imposta il nome dell'Azienda
	 * @param nome 
	 */
	void setNome(String nome);

	/** 
	 * Restituisce la partita iva di un'Azienda.
	 * @return String p_iva : la partita iva dell'Azienda.
	 */
	String getPIva();

	/**
	 * Imposta la partita iva di un'Azienda.
	 * @param p_iva 
	 */
	void setPIva(String p_iva);

	/**
	 * Restituisce la via in cui si trova un'Azienda.
	 * @return String via : la via in cui si trova l'Azienda.
	 */
	String getVia();

	/**
	 * Imposta la via in cui si trova l'Azienda
	 * @param via 
	 */
	void setVia(String via);

	Boolean isPrincipale();

	void setPrincipale(Boolean principale);

	Boolean isTassabile();

	Boolean getTassabile();

	void setTassabile(Boolean tassabile);

	String getNumeroAutorizzazione();

	void setNumeroAutorizzazione(String numeroAutorizzazione);

	Date getDataAutorizzazione();

	void setDataAutorizzazione(Date dataAutorizzazione);

	String getNumeroRegistrazione();

	void setNumeroRegistrazione(String numeroRegistrazione);

	Date getDataRegistrazione();

	void setDataRegistrazione(Date dataRegistrazione);

}