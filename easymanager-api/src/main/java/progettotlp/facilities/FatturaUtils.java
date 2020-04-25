package progettotlp.facilities;

import java.math.BigDecimal;

import progettotlp.interfaces.FatturaInterface;

public class FatturaUtils {

	public static boolean isBolloMandatory(FatturaInterface f) {
		String bollo = f.getBollo();
		if (bollo != null && !bollo.trim().isEmpty()) {
			return true;
		} else {
			Boolean tassabile = f.getCliente().isTassabile();
			BigDecimal netto = f.getNetto();
			BigDecimal bolloLimit = ConfigurationManager.getBolloLimit();
			return !tassabile && netto.compareTo(bolloLimit) > 0;
		}
	}
}
