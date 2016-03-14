/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.io.Serializable;
import java.util.List;

import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;

/**
 *
 * @author vincenzo
 */
public interface AziendaManager extends BaseManager{

    public void cancellaAzienda(Azienda a) throws PersistenzaException;

    public Azienda getAziendaPerNome(String nome);

    public List<Azienda> getAziendePerNome(List<String> nomi);

    public Azienda getAziendaPrincipale();

    public List<Azienda> getAziende();

    public List<Azienda> getAziendeNonPrincipali();

    public int getNumAziende();

    public void modificaAzienda(Azienda a) throws PersistenzaException;

    public void registraAzienda(Azienda a) throws PersistenzaException;

    public Boolean isAziendaTassabileByName(String text);
    
}
