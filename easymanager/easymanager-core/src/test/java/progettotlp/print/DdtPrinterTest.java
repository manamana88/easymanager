package progettotlp.print;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.DateUtils;

public class DdtPrinterTest {

	@Before
	public void setup() throws URISyntaxException{
		URL resource = this.getClass().getClassLoader().getResource("easymanager.properties");
		File file = new File(resource.toURI());
		assertTrue(file.exists());
		System.setProperty(ConfigurationManager.PROPERTIES_PATH, file.getAbsolutePath());
		ConfigurationManager.init();
	}
	
	@Test
	public void test() throws Exception{
		DdT ddt = new DdT();
		
		Bene b1 = new Bene();
        b1.setCodice("cod");
        b1.setCommessa("com");
        b1.setDescrizione("descr");
        b1.setPiazzato(true);
        b1.setQta(1);
        Bene b2 = new Bene();
        b2.setCodice("cod2");
        b2.setCommessa("com2");
        b2.setDescrizione("descr2");
        b2.setInteramenteAdesivato(true);
        b2.setQta(2);
        ddt.setBeni(Arrays.asList(b1, b2));
        Date data = DateUtils.parseDate("01/01/2012");
        ddt.setData(data);
        ddt.setId(1);
        ddt.setMezzo("Cessionario");
        ddt.setCausale("Reso c/adesivazione");
        ddt.setPorto("Destinazione");

        Azienda a = new Azienda();
        a.setCap("65129");
        a.setCitta("Pescara");
        a.setCivico("29");
        a.setCodFis("CPSRFL66P63E058J");
        a.setFax("085/4322029");
        a.setMail("info@crtaglio.com");
        a.setNazione("Italia");
        a.setNome("C.R.Taglio di Caposano Raffaella");
        a.setPIva("01815220684");
        a.setPrincipale(false);
        a.setProvincia("PE");
        a.setTelefono("328/9784864");
        a.setVia("Salara Vecchia");
        a.setTassabile(false);
        ddt.setCliente(a);
		
		File printPage = DdtPrinter.printPage(a, ddt, true, false);
		System.out.println(printPage.getAbsolutePath());
	}
}
