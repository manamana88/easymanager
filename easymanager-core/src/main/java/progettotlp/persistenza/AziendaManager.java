/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.List;

import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.interfaces.AziendaInterface;

/**
 *
 * @author vincenzo
 */
public interface AziendaManager extends BaseManager{

    public void cancellaAzienda(AziendaInterface a) throws PersistenzaException;

    public Azienda getAziendaPerNome(String nome);

    public List<Azienda> getAziendePerNome(List<String> nomi);

    public AziendaInterface getAziendaPrincipale();

    public List<Azienda> getAziende();

    public List<Azienda> getAziendeNonPrincipali();

    public int getNumAziende();

    public void modificaAzienda(AziendaInterface a) throws PersistenzaException;

    public void registraAzienda(AziendaInterface a) throws PersistenzaException;

    public Boolean isAziendaTassabileByName(String text);
    
}
