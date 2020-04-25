package progettotlp.interfaces;

import java.math.BigDecimal;

public interface BeneInterface {

	Boolean getCampionario();

	void setCampionario(Boolean campionario);

	String getCodice();

	void setCodice(String codice);

	String getCommessa();

	void setCommessa(String commessa);

	String getDescrizione();

	void setDescrizione(String descrizione);

	Long getId();

	void setId(Long id);

	Boolean getInteramenteAdesivato();

	void setInteramenteAdesivato(Boolean interamenteAdesivato);

	Boolean getPiazzato();

	void setPiazzato(Boolean piazzato);

	BigDecimal getPrezzo();

	void setPrezzo(BigDecimal prezzo);

	Boolean getPrimoCapo();

	void setPrimoCapo(Boolean primoCapo);

	Boolean getPrototipo();

	void setPrototipo(Boolean prototipo);

	BigDecimal getQta();

	void setQta(BigDecimal qta);

	BigDecimal getTot();

	void setTot(BigDecimal tot);

}