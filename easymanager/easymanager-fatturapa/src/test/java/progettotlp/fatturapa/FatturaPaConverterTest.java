package progettotlp.fatturapa;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.mockito.Mockito.*;
import org.junit.Test;

import progettotlp.facilities.ConfigurationManager;
import progettotlp.interfaces.FatturaInterface;

public class FatturaPaConverterTest {

	@Test
	public void testCreateProgressivoInvio() throws Exception {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("easymanager.properties");
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		ConfigurationManager.setProperties(properties);
		FatturaInterface mockFattura = mock(FatturaInterface.class);
		Date dateToReturn = new SimpleDateFormat("yyyyMMdd").parse("20180101");
		when(mockFattura.getEmissione()).thenReturn(dateToReturn);
		when(mockFattura.getId()).thenReturn(23);
		assertEquals("1823", FatturaPaConverter.createProgressivoInvio(mockFattura));
	}

}
