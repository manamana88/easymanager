package progettotlp.interfaces;

import java.util.Date;
import java.util.List;

import progettotlp.exceptions.toprint.ValidationException;

public interface DdTInterface {

	void addBene(BeneInterface b);

	void removeBene(int i);

	String getAnnotazioni();

	void setAnnotazioni(String annotazioni);

	String getAspettoEsteriore();

	void setAspettoEsteriore(String aspettoEsteriore);

	List<BeneInterface> getBeni();

	void setBeni(List<BeneInterface> beni);

	String getCausale();

	void setCausale(String causale);

	AziendaInterface getCliente();

	void setCliente(AziendaInterface cliente);

	Integer getColli();

	void setColli(Integer colli);

	Date getData();

	void setData(Date data);

	void setData(int giorno, int mese, int anno) throws ValidationException;

	String getDestinazione();

	void setDestinazione(String destinazione);

	FatturaInterface getFattura();

	void setFattura(FatturaInterface fattura);

	Integer getId();

	void setId(Integer id);

	String getMezzo();

	void setMezzo(String mezzo);

	Double getPeso();

	void setPeso(Double peso);

	String getPorto();

	void setPorto(String porto);

	Integer getProgressivo();

	void setProgressivo(Integer progressivo);

	Long getRealId();

	void setRealId(Long realId);

	String getRitiro();

	void setRitiro(String ritiro);

	String getTipo();

	void setTipo(String tipo);

	String getVostroOrdine();

	void setVostroOrdine(String vostroOrdine);

	String getVostroOrdineDel();

	void setVostroOrdineDel(String vostroOrdineDel);

	Boolean isFatturabile();

	Boolean getFatturabile();

	void setFatturabile(Boolean fatturabile);

}