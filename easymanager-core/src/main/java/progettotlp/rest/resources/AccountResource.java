package progettotlp.rest.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.classes.AccountEmail;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.facilities.BeanUtils;
import progettotlp.interfaces.AccountEmailInterface;
import progettotlp.persistenza.AccountManager;
import progettotlp.persistenza.ManagerProvider;

@Path("account")
public class AccountResource {
	
	private AccountManager accountManager = ManagerProvider.getAccountManager();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") Long id){
		AccountEmailInterface accountEmail = accountManager.get(AccountEmail.class, id);
		return Response.ok(BeanUtils.createResponseBean(accountEmail), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		List<AccountEmail> accounts = accountManager.getAccounts();
		return Response.ok(BeanUtils.createResponseBean(accounts.toArray()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@DELETE
	public Response deleteAzienda(
			@QueryParam("id") Long id
			) throws PersistenzaException{
		AccountEmailInterface accountEmail = accountManager.get(AccountEmail.class, id);
		accountManager.cancellaAccount(accountEmail);
		return Response.ok().build();
	}

}
