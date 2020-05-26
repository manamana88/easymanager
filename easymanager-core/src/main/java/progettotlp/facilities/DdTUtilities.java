package progettotlp.facilities;

import progettotlp.interfaces.DdTInterface;

public class DdTUtilities {

    public static String getFileName(DdTInterface ddt) {
        return String.format("%d - %s.pdf", ddt.getId(), ddt.getCliente().getNome());
    }
}