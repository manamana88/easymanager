/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.List;
import java.util.Properties;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.interfaces.AccountEmailInterface;

/**
 *
 * @author vincenzo
 */
public class AccountManagerImpl extends AbstractPersistenza implements AccountManager{

    public AccountManagerImpl(Properties properties) {
        super(properties);
    }

    public AccountManagerImpl() {
        super();
    }

    public void registraAccount(AccountEmailInterface accountEmail) throws PersistenzaException{
        save(accountEmail);
    }

    public void modificaAccount(AccountEmailInterface accountEmail)  throws PersistenzaException{
        update(accountEmail);
    }

    public void cancellaAccount(AccountEmailInterface accountEmail)  throws PersistenzaException{
        delete(accountEmail);
    }

    public AccountEmailInterface getAccount(Long id) {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query= sessione.createCriteria(AccountEmail.class);
            query.add(Restrictions.eq("id", id));
            Object result=query.uniqueResult();
            return result==null?null:(AccountEmail)result;
        } finally {
            sessione.flush();
            sessione.close();
        }
    }

    public AccountEmailInterface getAccountByUsername(String username){
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query= sessione.createCriteria(AccountEmail.class);
            query.add(Restrictions.eq("username", username));
            Object result=query.uniqueResult();
            return result==null?null:(AccountEmail)result;
        } finally {
            sessione.flush();
            sessione.close();
        }

    }

    public List<AccountEmail> getAccounts() {
        Session sessione=null;
        try{
            sessione=sessionFactory.openSession();
            Criteria query = sessione.createCriteria(AccountEmail.class);
            query.addOrder(Order.asc("username"));
            return query.list();
        } finally {
            sessione.flush();
            sessione.close();
        }
    }

}
