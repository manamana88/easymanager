package progettotlp.persistenza;

public class ManagerProvider {

	public static AccountManager getAccountManager() {
		return new AccountManagerImpl();
	}
	
	public static AziendaManager getAziendaManager() {
		return new AziendaManagerImpl();
	}
	
	public static DdTManager getDdtManager() {
		return new DdTManagerImpl();
	}
	
	public static FatturaManager getFatturaManager() {
		return new FatturaManagerImpl();
	}
	
	public static StatisticheManager getStatisticheManager() {
		return new StatisticheManagerImpl();
	}
	
	
}
