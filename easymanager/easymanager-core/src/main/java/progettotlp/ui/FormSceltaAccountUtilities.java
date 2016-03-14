/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.persistenza.AccountManager;

/**
 *
 * @author vincenzo
 */
public class FormSceltaAccountUtilities extends AbstractFormUtilities{
    
    public static enum FormSceltaAccountType{CANCELLA,MODIFICA};
    
    protected JList accountList;
    protected JButton accountScegliModifica;
    protected JButton accountScegliCancella;

    protected AccountManager accountManager;

    public FormSceltaAccountUtilities(JInternalFrame formSceltaAccount, AccountManager accountManager) {
        this.accountManager = accountManager;
        init(formSceltaAccount);
    }
    
    
    public String getSelectedAccount() throws NoSelectedRow{
        int selectedIndex = getSelectedIndex();
        return (String)accountList.getModel().getElementAt(selectedIndex);
    }
    
    public void cancellaAccount() throws NoSelectedRow, GenericExceptionToPrint{
        String selectedAccount = getSelectedAccount();
        AccountEmail scelta=accountManager.getAccountByUsername(selectedAccount);
        try{
            accountManager.cancellaAccount(scelta);
            removeSelectedAccount();
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore cancellazione", "Errore, account non eliminato dal sistema.",e);
        }
    }

    protected void caricaLista(){
        DefaultListModel x= new DefaultListModel();
        List<AccountEmail> l=accountManager.getAccounts();
        for (AccountEmail accountEmail : l){
            x.addElement(accountEmail.getUsername());
        }
        accountList.setModel(x);
    }

    private int getSelectedIndex() throws NoSelectedRow {
        int selectedIndex = accountList.getSelectedIndex();
        if (selectedIndex<0){
            throw new NoSelectedRow("Errore", "Nessun account selezionato");
        }
        return selectedIndex;
    }
    
    private void removeSelectedAccount() throws NoSelectedRow{
        int selectedIndex = getSelectedIndex();
        ((DefaultListModel)accountList.getModel()).remove(selectedIndex);
    }
    
    
    public void showForm(FormSceltaAccountType formType){
        caricaLista();
        switch (formType) {
            case CANCELLA:
                accountScegliCancella.setVisible(true);
                accountScegliModifica.setVisible(false);
                break;
            case MODIFICA:
                accountScegliCancella.setVisible(false);
                accountScegliModifica.setVisible(true);
                break;
        }
        accountList.clearSelection();
        form.setVisible(true);
    }
    
    
}
