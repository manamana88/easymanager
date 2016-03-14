/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.List;
import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.PersistenzaException;

/**
 *
 * @author vincenzo
 */
public interface AccountManager {

    public void registraAccount(AccountEmail accountEmail) throws PersistenzaException;
    public void modificaAccount(AccountEmail accountEmail) throws PersistenzaException;
    public void cancellaAccount(AccountEmail accountEmail) throws PersistenzaException;
    public AccountEmail getAccount(Long id);
    public AccountEmail getAccountByUsername(String username);
    public List<AccountEmail> getAccounts();
}
