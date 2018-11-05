package progettotlp.fatturapa;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.*;
import org.junit.Test;

import progettotlp.interfaces.FatturaInterface;

public class FatturaPaConverterTest {

	@Test
	public void testCreateProgressivoInvio() throws Exception {
		FatturaInterface mockFattura = mock(FatturaInterface.class);
		Date dateToReturn = new SimpleDateFormat("yyyyMMdd").parse("20180101");
		when(mockFattura.getEmissione()).thenReturn(dateToReturn);
		when(mockFattura.getId()).thenReturn(23);
		assertEquals("1823", FatturaPaConverter.createProgressivoInvio(mockFattura));
	}

}
