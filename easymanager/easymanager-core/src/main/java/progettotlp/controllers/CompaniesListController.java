package progettotlp.controllers;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import progettotlp.classes.Azienda;
import progettotlp.persistenza.AziendaManager;

@ManagedBean
@SessionScoped
public class CompaniesListController {

	@ManagedProperty(value="#{aziendaManager}")
    private AziendaManager aziendaManager;

	public List<Azienda> getCompaniesList(){
		return aziendaManager.getAziendeNonPrincipali();
	}
	
	public void setAziendaManager(AziendaManager aziendaManager) {
		this.aziendaManager = aziendaManager;
	}
	
}
