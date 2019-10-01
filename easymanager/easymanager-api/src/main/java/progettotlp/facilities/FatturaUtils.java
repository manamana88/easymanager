package progettotlp.facilities;

import progettotlp.interfaces.FatturaInterface;

public class FatturaUtils {

	public static boolean isBolloMandatory(FatturaInterface f) {
		String bollo = f.getBollo();
		if (bollo != null && !bollo.trim().isEmpty()) {
			return true;
		} else {
			Boolean tassabile = f.getCliente().isTassabile();
			Float netto = f.getNetto();
			Float bolloLimit = ConfigurationManager.getBolloLimit();
			return !tassabile && netto>bolloLimit;
		}
	}
}
