package progettotlp.rest.resources;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.fatturapa.FatturaPaConverter;
import progettotlp.fatturapa.jaxb.FatturaElettronicaType;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FatturaResourceTest {

    @Before
    public void setup(){
        ConfigurationManager.init();
    }

    @Test
    public void marshalFatturaPA() throws Exception {
        Fattura fatturaToTest = getFatturaToTest();
        Azienda aziendaPrincipale = getAziendaPrincipale();
        FatturaElettronicaType fatturaElettronicaType = FatturaPaConverter.convertToFatturaPa(aziendaPrincipale, fatturaToTest);
        String s = new FatturaResource().marshalFatturaPA(fatturaElettronicaType);
        File aaa = File.createTempFile("aaa", ".xml");
        System.out.println(aaa.getAbsolutePath());
        IOUtils.write(s, new FileOutputStream(aaa), Charset.defaultCharset());
        assertNotNull(s);

    }

    public Fattura getFatturaToTest(){
        Fattura fattura = getFattura();
        fattura.setCliente(getCliente());
        fattura.setDdt(getDdts(fattura));
        return fattura;
    }

    private List<DdTInterface> getDdts(FatturaInterface fattura) {
        List<DdTInterface> result = new ArrayList<>();
        result.add(getDdt1(fattura));
        result.add(getDdt2(fattura));
        result.add(getDdt3(fattura));
        result.add(getDdt4(fattura));
        result.add(getDdt5(fattura));
        result.add(getDdt6(fattura));
        result.add(getDdt7(fattura));
        return result;
    }

    private DdTInterface getDdt1(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3684L,
                Date.valueOf(LocalDate.of(2021, 1, 4)),
                1,
                "Cessionario",
                "Reso C/Taglio",
                "Elite Confezioni srl\nVia Ventignano,43\n65012 Cepagatti (PE)",
                "",
                "",
                "",
                "",
                2,
                "",
                "04/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8732L,
                "Mod.CAMA0406P1USCR66",
                "01PR-2021-3981",
                "Camicie",
                "109",
                "1.7",
                "185.3",
                false,
                false,
                false,
                false,
                false)
        );
        return ddT;
    }

    private DdTInterface getDdt2(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3685L,
                Date.valueOf(LocalDate.of(2021, 1, 4)),
                2,
                "Cessionario",
                "Reso C/Taglio",
                "Beppe Confezioni\nVia Cavalieri,6\n36050 Zermeghedo (VI)",
                "",
                "",
                "",
                "",
                6,
                "",
                "04/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8733L,
                "Mod.GOMA0366Q1UTC003",
                "01PR-2021-4051",
                "Gonne",
                "149",
                "3.6",
                "536.4",
                false,
                false,
                false,
                false,
                false));
        ddT.getBeni().add(createBene(
                8734L,
                "Mod.GOMA0366Q1UTC003",
                "Reso",
                "Capo Campione",
                "1",
                "0",
                "0",
                false,
                false,
                false,
                false,
                false));
        ddT.getBeni().add(createBene(
                8735L,
                "Mod.GOMA0369Q1UTV834",
                "01PR-2021-4143",
                "Gonne",
                "199",
                "3.7",
                "736.3",
                false,
                false,
                false,
                true,
                false));
        return ddT;
    }

    private DdTInterface getDdt3(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3686L,
                Date.valueOf(LocalDate.of(2021, 1, 4)),
                3,
                "Cessionario",
                "Reso C/Taglio",
                "Cattaneo\nVia Statuto 131\n24033 Calusco D'Adda (BG)",
                "",
                "",
                "",
                "",
                1,
                "",
                "04/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8736L,
                "Mod.GOMA0384QVUTCZ95",
                "01PR-2021-4211",
                "Gonne",
                "70",
                "3.6",
                "252",
                false,
                false,
                false,
                false,
                false));
        return ddT;
    }

    private DdTInterface getDdt4(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3687L,
                Date.valueOf(LocalDate.of(2021, 1, 4)),
                4,
                "Cessionario",
                "Reso C/Taglio",
                "Elite Confezioni srl\nVia Ventignano,43\n65012 Cepagatti (PE)",
                "",
                "",
                "",
                "",
                1,
                "",
                "04/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8737L,
                "Mod.ABMA0686Q1UTCZ95",
                "01PR-2021-4183",
                "Abiti",
                "101",
                "6.1",
                "616.1",
                false,
                false,
                false,
                false,
                false));
        return ddT;
    }

    private DdTInterface getDdt5(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3688L,
                Date.valueOf(LocalDate.of(2021, 1, 11)),
                5,
                "Cessionario",
                "Reso C/Taglio",
                "Confezioni Lucia\nVia Ignazio Silone,1\n64023 Mosciano Sant'Angelo (TE)",
                "",
                "",
                "",
                "",
                2,
                "",
                "11/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8738L,
                "Mod.PAMA0212A1UTCZ98",
                "01PR-2021-4239",
                "Pantaloni",
                "230",
                "1.3",
                "299",
                false,
                false,
                false,
                false,
                false));
        return ddT;
    }

    private DdTInterface getDdt6(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3690L,
                Date.valueOf(LocalDate.of(2021, 1, 13)),
                7,
                "Cessionario",
                "Reso C/Taglio",
                "Confezioni Lucia\nVia Ignazio Silone,1\n64023 Mosciano Sant'Angelo (TE)",
                "",
                "",
                "",
                "",
                7,
                "",
                "14/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8740L,
                "Mod.PAMAT09A01TCR23",
                "01PR-2021-4261",
                "Pantaloni",
                "280",
                "1.9",
                "532",
                false,
                false,
                false,
                false,
                false));
        return ddT;
    }

    private DdTInterface getDdt7(FatturaInterface fattura) {
        DdT ddT = createDdt(
                fattura,
                3691L,
                Date.valueOf(LocalDate.of(2021, 1, 17)),
                8,
                "Cessionario",
                "Reso C/Taglio",
                "Cattaneo\nVia Statuto 131\n24033 Calusco D'Adda (BG)",
                "",
                "",
                "",
                "",
                1,
                "",
                "18/01/2021",
                "",
                true);
        ddT.getBeni().add(createBene(
                8741L,
                "Mod.GOMA0384QVUTCZ95",
                "01PR-2021-4270",
                "Gonne",
                "60",
                "3.6",
                "216",
                false,
                false,
                false,
                false,
                false));
        return ddT;
    }

    private DdT createDdt(FatturaInterface fattura, long realId, Date data, int id, String cessionario, String causale, String destinazione, String vostroOrdine, String vostroOrdineDel, String tipo, String aspettoEsteriore, int colli, String porto, String ritiro, String annotazioni, boolean fatturabile) {
        DdT ddT = new DdT();
        ddT.setRealId(realId);
        ddT.setData(data);
        ddT.setId(id);
        ddT.setMezzo(cessionario);
        ddT.setCausale(causale);
        ddT.setDestinazione(destinazione);
        ddT.setVostroOrdine(vostroOrdine);
        ddT.setVostroOrdineDel(vostroOrdineDel);
        ddT.setTipo(tipo);
        ddT.setAspettoEsteriore(aspettoEsteriore);
        ddT.setColli(colli);
        ddT.setPorto(porto);
        ddT.setRitiro(ritiro);
        ddT.setAnnotazioni(annotazioni);
        ddT.setFatturabile(fatturabile);
        ddT.setFattura(fattura);
        ddT.setCliente(fattura.getCliente());
        ddT.setBeni(new ArrayList<>());
        return ddT;
    }

    private Bene createBene(long id, String codice, String commessa, String camicie, String qta, String prezzo, String tot, boolean prototipo, boolean campionario, boolean primoCapo, boolean piazzato, boolean interamenteAdesivato) {
        Bene bene = new Bene();
        bene.setId(id);
        bene.setCodice(codice);
        bene.setCommessa(commessa);
        bene.setDescrizione(camicie);
        bene.setQta(new BigDecimal(qta));
        bene.setPrezzo(new BigDecimal(prezzo));
        bene.setTot(new BigDecimal(tot));
        bene.setPrototipo(prototipo);
        bene.setCampionario(campionario);
        bene.setPrimoCapo(primoCapo);
        bene.setPiazzato(piazzato);
        bene.setInteramenteAdesivato(interamenteAdesivato);
        return bene;
    }

    private Azienda getCliente() {
        Azienda azienda = new Azienda();
        azienda.setId(38L);
        azienda.setNome("Staff International S.P.A. ");
        azienda.setCodFis("00482290244");
        azienda.setVia("Via del Progresso");
        azienda.setCivico("10");
        azienda.setCap("36025");
        azienda.setCitta("Noventa Vicentina");
        azienda.setProvincia("VI");
        azienda.setNazione("IT");
        azienda.setMail("");
        azienda.setTelefono("");
        azienda.setFax("");
        azienda.setTassabile(false);
        azienda.setPrincipale(false);
        azienda.setNumeroProtocollo("2012171645813239-000019");
        azienda.setPEC("");
        azienda.setCodiceFatturaPa("A4707H7");
        azienda.setPIva("00482290244");
        return azienda;
    }
    private Azienda getAziendaPrincipale() {
        Azienda azienda = new Azienda();
        azienda.setId(1L);
        azienda.setNome("C.R. di Caposano Raffaella");
        azienda.setCodFis("CPSRFL66P63E058J");
        azienda.setVia("Salara Vecchia");
        azienda.setCivico("29");
        azienda.setCap("65129");
        azienda.setCitta("Pescara");
        azienda.setProvincia("PE");
        azienda.setNazione("Italia");
        azienda.setMail("info@crtaglio.com");
        azienda.setTelefono("328/9784864");
        azienda.setFax("328/9784864");
        azienda.setTassabile(true);
        azienda.setPrincipale(true);
        azienda.setPEC("pec@pec.crtaglio.com");
        azienda.setCodiceFatturaPa("M5UXCR1");
        azienda.setPIva("01815220684");
        return azienda;
    }

    private Fattura getFattura() {
        Fattura fattura = new Fattura();
        fattura.setRealId(600L);
        fattura.setEmissione(Date.valueOf(LocalDate.of(2021, 1, 29)));
        fattura.setScadenza(Date.valueOf(LocalDate.of(2021, 3, 30)));
        fattura.setId(1);
        fattura.setNetto(new BigDecimal("3373.1"));
        fattura.setIvaPerc(new BigDecimal(0));
        fattura.setIva(new BigDecimal(0));
        fattura.setTotale(new BigDecimal("3373.1"));
        return fattura;
    }
}