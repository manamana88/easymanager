package progettotlp.persistenza;

public class ManagerProvider {

	private static AccountManager accountManager = new AccountManagerImpl();
	private static AziendaManager aziendaManager = new AziendaManagerImpl(); 
	private static DdTManager ddtManager = new DdTManagerImpl();
	private static FatturaManager fatturaManager = new FatturaManagerImpl();
	private static StatisticheManager statisticheManager = new StatisticheManagerImpl();
	
	public static AccountManager getAccountManager() {
		return accountManager;
	}
	
	public static AziendaManager getAziendaManager() {
		return aziendaManager;
	}
	
	public static DdTManager getDdtManager() {
		return ddtManager;
	}
	
	public static FatturaManager getFatturaManager() {
		return fatturaManager;
	}
	
	public static StatisticheManager getStatisticheManager() {
		return statisticheManager;
	}
	
	
}
