package progettotlp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import progettotlp.facilities.ConfigurationManager;

public class ContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ConfigurationManager.init();
	}
}

