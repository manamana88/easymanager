/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.List;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.PersistenzaException;

/**
 *
 * @author vincenzo
 */
public interface FatturaManager extends BaseManager {

    public void cancellaFattura(int id) throws PersistenzaException;

    public boolean existsFattura(int mese, Azienda azienda);

    public boolean existsFattura(int id);

    public List<Fattura> getAllFatture(boolean initializeDdT, boolean initializeBeni);

    public List<Fattura> getFattureByAzienda(Long aziendaId, boolean initializeDdT, boolean initializeBeni);

    public List<Fattura> getFattureByAziendaName(String aziendaName);

    public Fattura getFattura(int id,boolean initializeDdT, boolean initializeBeni);

    public int getLastFattura();

    public void modificaFattura(Fattura daSalvare) throws PersistenzaException;

    public void registraFattura(Fattura daSalvare) throws PersistenzaException;

    public LastSameBeneFatturatoInfos getLastSameBeneFatturatoInfos(Bene b);
    
}
