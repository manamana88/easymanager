package progettotlp.facilities;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.FatturaInterface;

public class FatturaUtilsTest {
	
	@BeforeClass 
	public static void setup() throws IOException {
		InputStream resourceAsStream = FatturaUtilsTest.class.getClassLoader().getResourceAsStream("easymanager.properties");
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		ConfigurationManager.setProperties(properties);
	}
	
	@Test
	public void testIsBolloMandatory() {
		FatturaInterface mock = mock(FatturaInterface.class);
		when(mock.getBollo()).thenReturn("1234567890");
		assertTrue(FatturaUtils.isBolloMandatory(mock));

		mock = mock(FatturaInterface.class);
		when(mock.getBollo()).thenReturn(null);
		AziendaInterface clienteMock = mock(AziendaInterface.class);
		when(clienteMock.isTassabile()).thenReturn(true);
		when(mock.getCliente()).thenReturn(clienteMock);
		when(mock.getNetto()).thenReturn(new BigDecimal("77.48"));
		assertFalse(FatturaUtils.isBolloMandatory(mock));
		
		mock = mock(FatturaInterface.class);
		when(mock.getBollo()).thenReturn(null);
		clienteMock = mock(AziendaInterface.class);
		when(clienteMock.isTassabile()).thenReturn(false);
		when(mock.getCliente()).thenReturn(clienteMock);
		when(mock.getNetto()).thenReturn(new BigDecimal("77.48"));
		assertTrue(FatturaUtils.isBolloMandatory(mock));
		
		mock = mock(FatturaInterface.class);
		when(mock.getBollo()).thenReturn(null);
		clienteMock = mock(AziendaInterface.class);
		when(clienteMock.isTassabile()).thenReturn(true);
		when(mock.getCliente()).thenReturn(clienteMock);
		when(mock.getNetto()).thenReturn(new BigDecimal("77.47"));
		assertFalse(FatturaUtils.isBolloMandatory(mock));
		
		mock = mock(FatturaInterface.class);
		when(mock.getBollo()).thenReturn(null);
		clienteMock = mock(AziendaInterface.class);
		when(clienteMock.isTassabile()).thenReturn(false);
		when(mock.getCliente()).thenReturn(clienteMock);
		when(mock.getNetto()).thenReturn(new BigDecimal("77.47"));
		assertFalse(FatturaUtils.isBolloMandatory(mock));
		
	}

}
