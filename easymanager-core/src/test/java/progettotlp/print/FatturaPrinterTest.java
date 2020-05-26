package progettotlp.print;

import org.junit.Before;
import org.junit.Test;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FatturaPrinterTest {

    @Before
    public void setup() throws URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource("easymanager.properties");
        assertNotNull(resource);
        File file = new File(resource.toURI());
        assertTrue(file.exists());
        System.setProperty(ConfigurationManager.PROPERTIES_PATH, file.getAbsolutePath());
        ConfigurationManager.init();
    }

    @Test
    public void printPage() throws Exception {
        DdTInterface ddt = new DdT();

        BeneInterface b1 = new Bene();
        b1.setCodice("cod");
        b1.setCommessa("com");
        b1.setDescrizione("descr");
        b1.setPiazzato(true);
        b1.setQta(new BigDecimal("1"));
        BeneInterface b2 = new Bene();
        b2.setCodice("cod2");
        b2.setCommessa("com2");
        b2.setDescrizione("descr2");
        b2.setInteramenteAdesivato(true);
        b2.setQta(new BigDecimal("2"));
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
        a.setDataAutorizzazione(new Date());
        a.setDataRegistrazione(new Date());
        a.setNumeroAutorizzazione("1");
        a.setNumeroRegistrazione("2");
        ddt.setCliente(a);

        FatturaInterface f = new Fattura();
        f.setBollo("this_is_bollo");
        f.setCliente(a);
        f.setId(1);
        f.setDdt(Collections.singletonList(ddt));
        Date emissione = new Date();
        f.setEmissione(emissione);
        f.setScadenza(Date.from(LocalDate.now().plus(30, ChronoUnit.DAYS).atStartOfDay(ZoneOffset.systemDefault()).toInstant()));
        f.setIva(new BigDecimal("40"));
        f.setIvaPerc(new BigDecimal("20"));
        f.setNetto(new BigDecimal(200));
        f.setTotale(new BigDecimal(240));

        File tempFile = File.createTempFile("emem", "emem");
        System.out.println(tempFile.getAbsolutePath());
        FatturaPrinter.printPage(f, a, tempFile);
    }
}