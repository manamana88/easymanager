/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.List;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.PersistenzaException;

/**
 *
 * @author vincenzo
 */
public interface DdTManager {

    public void cancellaDdT(int id) throws PersistenzaException;

    public boolean existsDdT(Long realId);

    public boolean existsDdTById(int id);

    public List<DdT> getAllDdT(boolean initializeBeni, boolean initializeFattura);

    public List<DdT> getAllDdT(Azienda a, int mese,boolean initializeBeni, boolean initializeFattura);

    public List<DdT> getAllDdTWithoutFattura(boolean initializeBeni, boolean initializeFattura);

    public List<Bene> getBeniDdT(int id) throws PersistenzaException;

    public DdT getDdTById(int id,boolean initializeBeni, boolean initializeFattura);

    public DdT getDdT(Long id,boolean initializeBeni, boolean initializeFattura);

    public int getLastDdT();

    public boolean isEmptyDdTListMese(int selectedMese, Azienda azienda);

    public void modificaDdT(DdT toModify) throws PersistenzaException;

    public void registraDdT(DdT d) throws PersistenzaException;
    
}
