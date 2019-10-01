/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.List;

import progettotlp.classes.Fattura;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.FatturaInterface;

/**
 *
 * @author vincenzo
 */
public interface FatturaManager extends BaseManager {

    public void cancellaFattura(int id) throws PersistenzaException;

    public boolean existsFattura(int mese, AziendaInterface azienda);

    public boolean existsFattura(int id);

    public List<Fattura> getAllFatture(boolean initializeDdT, boolean initializeBeni);

    public List<Fattura> getFattureByAzienda(Long aziendaId, boolean initializeDdT, boolean initializeBeni);

    public List<Fattura> getFattureByAziendaName(String aziendaName);

    public Fattura getFattura(int id,boolean initializeDdT, boolean initializeBeni);

    public int getLastFattura();

    public void modificaFattura(FatturaInterface daSalvare) throws PersistenzaException;

    public void registraFattura(FatturaInterface daSalvare) throws PersistenzaException;

    public LastSameBeneFatturatoInfos getLastSameBeneFatturatoInfos(BeneInterface b);
    
}
