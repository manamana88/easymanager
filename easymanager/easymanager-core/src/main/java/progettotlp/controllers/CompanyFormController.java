package progettotlp.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import progettotlp.classes.Azienda;
import progettotlp.persistenza.AziendaManager;

@ManagedBean
@RequestScoped
public class CompanyFormController {

	@ManagedProperty(value="#{aziendaManager}")
    private AziendaManager aziendaManager;

	public Azienda getAzienda(Long id){
		Azienda azienda = aziendaManager.get(Azienda.class, id);
		return azienda;
	}
	
	public void saveAzienda(Azienda a){
		System.out.println("s");
	}
	
	public void setAziendaManager(AziendaManager aziendaManager) {
		this.aziendaManager = aziendaManager;
	}
}
