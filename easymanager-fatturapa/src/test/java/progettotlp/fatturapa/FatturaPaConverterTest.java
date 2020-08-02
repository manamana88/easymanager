package progettotlp.fatturapa;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import progettotlp.facilities.ConfigurationManager;
import progettotlp.fatturapa.jaxb.DettaglioLineeType;
import progettotlp.fatturapa.jaxb.ScontoMaggiorazioneType;
import progettotlp.fatturapa.jaxb.TipoScontoMaggiorazioneType;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

public class FatturaPaConverterTest {

	@BeforeClass 
	public static void setup() throws IOException {
		InputStream resourceAsStream = FatturaPaConverterTest.class.getClassLoader().getResourceAsStream("easymanager.properties");
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		ConfigurationManager.setProperties(properties);
	}
	
	@Test
	public void testCreateProgressivoInvio() throws Exception {
		FatturaInterface mockFattura = mock(FatturaInterface.class);
		Date dateToReturn = new SimpleDateFormat("yyyyMMdd").parse("20180101");
		when(mockFattura.getEmissione()).thenReturn(dateToReturn);
		when(mockFattura.getId()).thenReturn(23);
		assertEquals("1823", FatturaPaConverter.createProgressivoInvio(mockFattura));
	}
	
	@Test
	public void riferimentoNormativo() {
		AziendaInterface mock = Mockito.mock(AziendaInterface.class);
		when(mock.getNazione()).thenReturn("it");
		when(mock.getNumeroProtocollo()).thenReturn("20060511484914079 - 000001");
		String createRiferimentoNormativo = FatturaPaConverter.createRiferimentoNormativo(mock);
		System.out.println(createRiferimentoNormativo);
		System.out.println(createRiferimentoNormativo.length());
		assertTrue(createRiferimentoNormativo.length()<=100);
		String expected = "Art.8 co.1 lett.C DPR 633/72 come da vs. dichiarazione protocollo n. 20060511484914079 - 000001";
		assertEquals(expected, createRiferimentoNormativo);

		mock = Mockito.mock(AziendaInterface.class);
		when(mock.getNazione()).thenReturn("uk");
		when(mock.getNumeroProtocollo()).thenReturn("20060511484914079 - 000001");
		String createRiferimentoNormativo2 = FatturaPaConverter.createRiferimentoNormativo(mock);
		System.out.println(createRiferimentoNormativo2);
		System.out.println(createRiferimentoNormativo2.length());
		assertTrue(createRiferimentoNormativo2.length()<=100);
		String expected2 = "Art.7/ter co.1 DPR 633/1972 come da vs. dichiarazione protocollo n. 20060511484914079 - 000001";
		assertEquals(expected2, createRiferimentoNormativo2);
	}

	@Test
	public void testCreateDettaglioLinea() {
		BeneInterface bene = mock(BeneInterface.class);
		when(bene.getDescrizione()).thenReturn("Descrizione");
		when(bene.getQta()).thenReturn(new BigDecimal("75"));
		when(bene.getPrezzo()).thenReturn(null);
		when(bene.getTot()).thenReturn(new BigDecimal("50"));
		DdTInterface ddt = mock(DdTInterface.class);
		when(ddt.getCliente()).thenReturn(mock(AziendaInterface.class));
		DettaglioLineeType dettaglioLinea = FatturaPaConverter.createDettaglioLinea(ddt, bene);
		assertNotNull(dettaglioLinea);
		assertEquals("Descrizione", dettaglioLinea.getDescrizione());
		assertEquals(new BigDecimal("0.67"), dettaglioLinea.getPrezzoUnitario());
		assertEquals(new BigDecimal("75"), dettaglioLinea.getQuantita());
		assertEquals(new BigDecimal("50"), dettaglioLinea.getPrezzoTotale());
		List<ScontoMaggiorazioneType> sconti = dettaglioLinea.getScontoMaggiorazione();
		assertNotNull(sconti);
		assertEquals(1, sconti.size());
		ScontoMaggiorazioneType scontoMaggiorazioneType = sconti.get(0);
		assertEquals(new BigDecimal("0.25"), scontoMaggiorazioneType.getImporto());
		assertEquals(TipoScontoMaggiorazioneType.SC, scontoMaggiorazioneType.getTipo());
	}

	@Test
	public void testScontoMaggiorazione() {
		BigDecimal prezzo = new BigDecimal("1.19");

		BeneInterface bene = mock(BeneInterface.class);
		when(bene.getTot()).thenReturn(new BigDecimal("608.09"));
		when(bene.getQta()).thenReturn(new BigDecimal("511"));
		List<ScontoMaggiorazioneType> scontoMaggiorazioneList = FatturaPaConverter.createScontoMaggiorazione(bene, prezzo);
		assertTrue(scontoMaggiorazioneList.isEmpty());

		bene = mock(BeneInterface.class);
		when(bene.getTot()).thenReturn(new BigDecimal("609"));
		when(bene.getQta()).thenReturn(new BigDecimal("511"));
		scontoMaggiorazioneList = FatturaPaConverter.createScontoMaggiorazione(bene, prezzo);
		assertEquals(1, scontoMaggiorazioneList.size());
		ScontoMaggiorazioneType scontoMaggiorazione = scontoMaggiorazioneList.get(0);
		assertEquals(TipoScontoMaggiorazioneType.MG, scontoMaggiorazione.getTipo());
		assertEquals(new BigDecimal("0.91"), scontoMaggiorazione.getImporto());
		assertNull(scontoMaggiorazione.getPercentuale());
		
		bene = mock(BeneInterface.class);
		when(bene.getTot()).thenReturn(new BigDecimal("607"));
		when(bene.getQta()).thenReturn(new BigDecimal("511"));
		scontoMaggiorazioneList = FatturaPaConverter.createScontoMaggiorazione(bene, prezzo);
		assertEquals(1, scontoMaggiorazioneList.size());
		scontoMaggiorazione = scontoMaggiorazioneList.get(0);
		assertEquals(TipoScontoMaggiorazioneType.SC, scontoMaggiorazione.getTipo());
		assertEquals(new BigDecimal("1.09"), scontoMaggiorazione.getImporto());
		assertNull(scontoMaggiorazione.getPercentuale());
	}

}
