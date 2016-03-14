package progettotlp.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TestController {

	public String helloWorld(){
		return "CIAO";
	}
}
