/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package progettotlp.facilities;

import java.io.File;
import java.util.List;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.print.BeneFattura;

public class FatturaUtilities
{
    protected static int getTotCapi(List<BeneFattura> list)
    {
        int tot = 0;
        for (BeneFattura bene : list) {
            tot += bene.getBene().getQta().intValue();
        }
        return tot;
    }

    public static int getTotCapi(Fattura f) {
        int tot = 0;
        for (DdT d : f.getDdt()) {
            for (Bene b : d.getBeni()) {
                tot += b.getQta().intValue();
            }
        }
        return tot;
    }

    public static String getDirectoryPath(Fattura fattura, String basePath) {
        return basePath + File.separator
            + DateUtils.getYear(fattura.getEmissione())
            + File.separator
            + DateUtils.getMonthString(fattura.getEmissione());
    }

    public static String getFileName(Fattura fattura) {
        return fattura.getId() + " - " + fattura.getCliente().getNome();
    }
}