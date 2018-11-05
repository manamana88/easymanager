/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.List;
import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.interfaces.AccountEmailInterface;

/**
 *
 * @author vincenzo
 */
public interface AccountManager extends BaseManager {

    public void registraAccount(AccountEmailInterface accountEmail) throws PersistenzaException;
    public void modificaAccount(AccountEmailInterface accountEmail) throws PersistenzaException;
    public void cancellaAccount(AccountEmailInterface accountEmail) throws PersistenzaException;
    public AccountEmailInterface getAccount(Long id);
    public AccountEmailInterface getAccountByUsername(String username);
    public List<AccountEmail> getAccounts();
}
