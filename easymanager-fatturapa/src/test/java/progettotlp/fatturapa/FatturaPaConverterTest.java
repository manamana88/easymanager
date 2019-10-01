package progettotlp.fatturapa;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import progettotlp.facilities.ConfigurationManager;
import progettotlp.interfaces.AziendaInterface;
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
	public void riferimentoNormativo() throws Exception {
		AziendaInterface mock = Mockito.mock(AziendaInterface.class);
		when(mock.getNazione()).thenReturn("it");
		when(mock.getNumeroAutorizzazione()).thenReturn("111");
		when(mock.getNumeroRegistrazione()).thenReturn("111");
		when(mock.getDataAutorizzazione()).thenReturn(new Date());
		when(mock.getDataRegistrazione()).thenReturn(new Date());
		String createRiferimentoNormativo = FatturaPaConverter.createRiferimentoNormativo(mock);
		System.out.println(createRiferimentoNormativo);
		System.out.println(createRiferimentoNormativo.length());
		assertTrue(createRiferimentoNormativo.length()<=100);
		
		mock = Mockito.mock(AziendaInterface.class);
		when(mock.getNazione()).thenReturn("uk");
		when(mock.getNumeroAutorizzazione()).thenReturn("111");
		when(mock.getNumeroRegistrazione()).thenReturn("111");
		when(mock.getDataAutorizzazione()).thenReturn(new Date());
		when(mock.getDataRegistrazione()).thenReturn(new Date());
		String createRiferimentoNormativo2 = FatturaPaConverter.createRiferimentoNormativo(mock);
		System.out.println(createRiferimentoNormativo2);
		System.out.println(createRiferimentoNormativo2.length());
		assertTrue(createRiferimentoNormativo2.length()<=100);
	}

}
