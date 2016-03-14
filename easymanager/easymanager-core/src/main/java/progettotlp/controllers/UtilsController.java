package progettotlp.controllers;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class UtilsController {

	private static final String PREVENT_CLASS = "preventCheckbox";

	public String addPreventClass(boolean add){
		return add ? PREVENT_CLASS : "";
	}
}
