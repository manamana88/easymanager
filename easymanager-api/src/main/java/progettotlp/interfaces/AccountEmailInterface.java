package progettotlp.interfaces;

public interface AccountEmailInterface {

	Long getId();

	void setId(Long id);

	String getPassword();

	void setPassword(String password);

	String getSmtp();

	void setSmtp(String smtp);

	String getUsername();

	void setUsername(String username);

}