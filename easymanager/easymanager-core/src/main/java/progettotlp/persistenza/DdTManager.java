/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.persistenza;

import java.util.Date;
import java.util.List;

import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.DdTInterface;

/**
 *
 * @author vincenzo
 */
public interface DdTManager extends BaseManager {

    public void cancellaDdT(Long id) throws PersistenzaException;

    public boolean existsDdT(Long realId);

    public boolean existsDdTById(int id);

    public List<DdT> getAllDdT(boolean initializeBeni, boolean initializeFattura);

    public List<DdT> getAllDdT(Long aziendaId);

    public List<DdTInterface> getAllDdT(AziendaInterface a, int mese,boolean initializeBeni, boolean initializeFattura);

    public List<DdT> getAllDdT(AziendaInterface a, Date startDate, Date endDate);

    public List<DdTInterface> getAllDdTWithoutFattura(AziendaInterface a, Date startDate, Date endDate);

    public List<DdT> getAllDdTWithoutFattura(boolean initializeBeni, boolean initializeFattura);

    public List<Bene> getBeniDdT(int id) throws PersistenzaException;

    public DdTInterface getDdTById(int id,boolean initializeBeni, boolean initializeFattura);

    public DdT getDdT(Long id,boolean initializeBeni, boolean initializeFattura);

    public int getLastDdT();

    public boolean isEmptyDdTListMese(int selectedMese, AziendaInterface azienda);

    public void modificaDdT(DdTInterface toModify) throws PersistenzaException;

    public void registraDdT(DdTInterface d) throws PersistenzaException;
    
}
