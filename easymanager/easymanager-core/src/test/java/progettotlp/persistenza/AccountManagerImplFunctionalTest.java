/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.ArrayList;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.junit.Test;
import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.PersistenzaException;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class AccountManagerImplFunctionalTest extends AbstractTest{
    protected AccountManagerImpl accountManagerImpl;

    @Test
    public void testRegistraAzienda() throws PersistenzaException{
        AccountEmail daSalvare= new AccountEmail();
        String username="username";
        String password="password";
        String smtp="smtp";

        daSalvare.setPassword(password);
        daSalvare.setSmtp(smtp);
        daSalvare.setUsername(username);

        accountManagerImpl.registraAccount(daSalvare);
        Long id = daSalvare.getId();
        assertNotNull(id);
        AccountEmail retrieved = retrieveObject(AccountEmail.class, id,accountManagerImpl);
        assertNotNull(retrieved);
        assertEquals(username,retrieved.getUsername());
        assertEquals(password,retrieved.getPassword());
        assertEquals(smtp,retrieved.getSmtp());
    }

    @Test
    public void testGetAccounts() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAccountTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        List<AccountEmail> accounts = accountManagerImpl.getAccounts();
        assertNotNull(accounts);
        assertEquals(3, accounts.size());
        for(AccountEmail accountEmail : accounts){
            if (accountEmail.getId().equals(1L)){
                assertEquals("username1",accountEmail.getUsername());
                assertEquals("password1",accountEmail.getPassword());
                assertEquals("smtp1",accountEmail.getSmtp());
            } else if (accountEmail.getId().equals(2L)){
                assertEquals("username2",accountEmail.getUsername());
                assertEquals("password2",accountEmail.getPassword());
                assertEquals("smtp2",accountEmail.getSmtp());
            } else if (accountEmail.getId().equals(3L)){
                assertEquals("username3",accountEmail.getUsername());
                assertEquals("password3",accountEmail.getPassword());
                assertEquals("smtp3",accountEmail.getSmtp());
            } else {
                fail("Unexpected account: ["+accountEmail.getUsername()+"]");
            }
        }
    }

    @Test
    public void testGetAccountByUsername() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAccountTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        AccountEmail accountEmail = accountManagerImpl.getAccountByUsername("username1");
        assertNotNull(accountEmail);
        assertEquals("username1",accountEmail.getUsername());
        assertEquals("password1",accountEmail.getPassword());
        assertEquals("smtp1",accountEmail.getSmtp());

        accountEmail=accountManagerImpl.getAccount(5L);
        assertNull(accountEmail);
    }

    @Test
    public void testGetAccount() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAccountTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        AccountEmail accountEmail = accountManagerImpl.getAccount(1L);
        assertNotNull(accountEmail);
        assertEquals("username1",accountEmail.getUsername());
        assertEquals("password1",accountEmail.getPassword());
        assertEquals("smtp1",accountEmail.getSmtp());

        accountEmail=accountManagerImpl.getAccount(5L);
        assertNull(accountEmail);
    }

    @Test
    public void testModificaAccount() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAccountTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);

        Long id=1L;
        String username="username";
        String password="password";
        String smtp="smtp";

        AccountEmail daSalvare = retrieveObject(AccountEmail.class, id,accountManagerImpl);
        daSalvare.setPassword(password);
        daSalvare.setSmtp(smtp);
        daSalvare.setUsername(username);

        accountManagerImpl.modificaAccount(daSalvare);

        assertNotNull(daSalvare);
        assertEquals(username,daSalvare.getUsername());
        assertEquals(password,daSalvare.getPassword());
        assertEquals(smtp,daSalvare.getSmtp());
    }

    @Test
    public void tastCancellaAccount() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAccountTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);

        Long id=1L;
        AccountEmail daCancellare = retrieveObject(AccountEmail.class, id,accountManagerImpl);
        assertNotNull(daCancellare);

        accountManagerImpl.cancellaAccount(daCancellare);

        daCancellare = retrieveObject(AccountEmail.class, id,accountManagerImpl);
        assertNull(daCancellare);

    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AccountManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return this;
    }


}
