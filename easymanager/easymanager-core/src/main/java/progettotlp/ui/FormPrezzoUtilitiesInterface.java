/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.awt.Container;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.persistenza.LastSameBeneFatturatoInfos;
import progettotlp.ui.FormPrezzoUtilities.CostoType;
import progettotlp.ui.FormPrezzoUtilities.FormPrezzoType;

/**
 *
 * @author vincenzo
 */
public interface FormPrezzoUtilitiesInterface {
    public static final String MAI_FATTURATO = "Mai fatturato";
    public static final String ND = "N/D";

    void compilaForm(String ddt, String codice, String commessa, String descrizione, boolean proto, boolean piazz, boolean pc, boolean camp, boolean intAde, int capi, CostoType costoType, Float prezzoValue,LastSameBeneFatturatoInfos infos);

    void compilaForm(DdT d, Bene b,LastSameBeneFatturatoInfos infos);

    CostoType getCostoType() throws ValidationException;

    Float getPrezzo() throws ValidationException;

    void okAction() throws ValidationException;

    Float okModificaPrezzoAction() throws ValidationException;

    void showForm(FormPrezzoType formPrezzoType);

    void tempoAction();

    void unitarioAction();

    Container getForm();

}
