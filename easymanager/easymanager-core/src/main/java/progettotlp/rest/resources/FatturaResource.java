package progettotlp.rest.resources;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.BeanUtils;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.FatturaUtilities;
import progettotlp.facilities.NumberUtils;
import progettotlp.facilities.Utility;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.persistenza.FatturaManager;
import progettotlp.persistenza.ManagerProvider;
import progettotlp.print.FatturaPrinter;
import progettotlp.rest.beans.FatturaBean;

@Path("fattura")
public class FatturaResource {
	
	private AziendaManager aziendaManager = ManagerProvider.getAziendaManager();
	private DdTManager ddtManager = ManagerProvider.getDdtManager();
	private FatturaManager fatturaManager = ManagerProvider.getFatturaManager();
	
    public static final Integer ROWS_PER_PAGE=35;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(
			@QueryParam("id") Long id) {
		Fattura first = fatturaManager.get(Fattura.class, id);
		Fattura initialized = fatturaManager.getFattura(first.getId(), true, true);
		return Response.ok(BeanUtils.createResponseBean(new FatturaBean(initialized)), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("print")
	@Produces(MediaType.APPLICATION_JSON)
	public Response print(
			@QueryParam("id") Long id) throws GenericExceptionToPrint {
        try {
        	System.out.println("ResourceStart"+System.currentTimeMillis());
            Fattura toPrint = fatturaManager.get(Fattura.class, id);
            toPrint = fatturaManager.getFattura(toPrint.getId(), true, true);
            System.out.println("Retrieved"+System.currentTimeMillis());
            File directory = new File(FatturaUtilities.getDirectoryPath(toPrint,
                    ConfigurationManager.getProperty(Property.FATTURE_FOLDER_PATH)));
            directory.mkdirs();
            File printPage = FatturaPrinter.printPage(toPrint,
                    aziendaManager.getAziendaPrincipale(),
                    directory.getAbsolutePath(),
                    FatturaUtilities.getFileName(toPrint), false);
            return Response.ok(BeanUtils.createResponseBean(printPage.getAbsolutePath()), MediaType.APPLICATION_JSON_TYPE).build();
        } catch (Exception ex) {
            throw new GenericExceptionToPrint("Errore", "Siamo spiacenti, si è verificato un errore."+'\n'+"Impossibile stampare la fattura",ex);
        }
	}
	
	@GET
	@Path("candidates")
	@Produces(MediaType.APPLICATION_JSON)
	public Response candidates() throws ParseException {
		List<DdT> allDdT = ddtManager.getAllDdTWithoutFattura(false, false);
		Set<Azienda> aziende = findAziende(allDdT);
		Date referenceDate = findStartDate(allDdT);
		Date endDate = DateUtils.getFatturaDay(referenceDate);
		Date startDate = DateUtils.getFirstDayOfMonth(referenceDate);
		return Response.ok(BeanUtils.createResponseBean(startDate, endDate, aziende), MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Path("next")
	@Produces(MediaType.APPLICATION_JSON)
	public Response next(
			@QueryParam("azienda") Long aziendaId,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) throws ParseException {
		Azienda azienda = aziendaManager.get(Azienda.class, aziendaId);
		Date startDateObject = DateUtils.parseDate(startDate);
		Date endDateObject = DateUtils.parseDate(endDate);
		List<DdT> allDdT = ddtManager.getAllDdTWithoutFattura(azienda, startDateObject, endDateObject);
		int lastFattura = fatturaManager.getLastFattura();
		long totCapi = Utility.getTotCapi(allDdT);
		Float ivaDefault = Float.parseFloat(ConfigurationManager.getProperty(Property.IVA_DEFAULT));
		return Response.ok(BeanUtils.createResponseBean(lastFattura+1, ivaDefault, totCapi, allDdT, azienda), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(
			@QueryParam("company") Long id,
			@QueryParam("init") @DefaultValue("false") boolean init) {
		List<Fattura> lst;
		if (id != null){
			lst = fatturaManager.getFattureByAzienda(id, init, init);
		} else {
			lst=fatturaManager.getAllFatture(init, init);
		}
		if (!init){
			for (Fattura fattura : lst) {
				fattura.setDdt(null);
			}
		}
		return Response.ok(BeanUtils.createResponseBean(lst.toArray()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
			@QueryParam("id") Long id) throws PersistenzaException {
		Fattura fattura = fatturaManager.get(Fattura.class, id);
		fatturaManager.cancellaFattura(fattura.getId());
		return Response.ok().build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(Fattura f) throws Exception {
		Fattura fattura = caricaFattura(f);
        if (!fatturaManager.existsFattura(fattura.getId())){
            try{
                fatturaManager.registraFattura(fattura);
                return Response.ok(fattura, MediaType.APPLICATION_JSON_TYPE).build();
            } catch (Exception e){
                throw new GenericExceptionToPrint("Errore", "Errore nella registrazione della fattura n° "+f.getId()+".",e);
            }
        } else {
            throw new YetExistException("Errore","Esiste già una fattura con lo stesso numero.");
        }
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifica(Fattura f) throws Exception {
		Fattura fattura = caricaFattura(f);
		try{
            fatturaManager.modificaFattura(fattura);
            return Response.ok(fattura, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore", "Errore nella registrazione della fattura n° "+fattura.getId()+".",e);
        }
	}

	private Fattura caricaFattura(Fattura f) throws Exception {
		Azienda clienteAzienda = aziendaManager.get(Azienda.class, f.getCliente().getId());
		int month = DateUtils.getMonth(f.getEmissione());
		List<DdT> listaDdT=ddtManager.getAllDdT(clienteAzienda, month,true,false);
        Map<Integer,Map<Long,Bene>> mapping=Utility.mapDdT(listaDdT);
        List<DdT> ddtToAdd = new ArrayList<DdT>();
        for (DdT d : f.getDdt()){
        	Integer idDdT = d.getId();
        	for (Bene bene : d.getBeni()) {
	            Long idBene = bene.getId();
	            Bene riga = mapping.get(idDdT).get(idBene);
	            if (riga == null){
	            	throw new Exception("Impossibile trovare questo bene: ["+bene.toString()+"]");
	            }
	            Float prezzo = bene.getPrezzo();
				riga.setPrezzo(NumberUtils.roundNumber(prezzo));
	            riga.setTot(NumberUtils.roundNumber(bene.getTot()));
	            checkConsistency(riga);
        	}
        	for (DdT ddt : listaDdT) {
        		if (ddt.getId().equals(idDdT)){
        			ddtToAdd.add(ddt);
        		}
			}
        }
        Boolean tassabile = clienteAzienda.isTassabile();
        Float netto = NumberUtils.roundNumber(f.getNetto());
        Float ivaPerc;
        Float ivaTotale;
        Float totale;
		if (tassabile){
			ivaPerc = f.getIvaPerc();
			ivaTotale = NumberUtils.roundNumber(f.getIva());
			totale = NumberUtils.roundNumber(f.getTotale());
        } else {
        	ivaPerc = 0F;
        	ivaTotale = 0F;
        	totale = netto;
        }
		Fattura fattura = new Fattura(ddtToAdd, f.getEmissione(), f.getScadenza(), f.getId(), clienteAzienda,
        		netto, ivaPerc, ivaTotale, totale);
        checkConsistency(fattura, tassabile);
        Long realId = f.getRealId();
		if (realId!=null){
        	fattura.setRealId(realId);
        }
		return fattura;
	}
	
	private void checkConsistency(Fattura fattura, Boolean tassabile) throws Exception {
		Float netto=0F;
		for (DdT ddt : fattura.getDdt()) {
			for (Bene bene : ddt.getBeni()) {
				netto+=bene.getTot();
			}
		}
		Float roundedNetto = NumberUtils.roundNumber(netto);
		if (!fattura.getNetto().equals(roundedNetto)){
			throw new Exception("Netto inconsistente: "+fattura.getNetto()+" contro "+roundedNetto);
		}
		
		if (tassabile){
			Float ivaTot = NumberUtils.roundNumber(fattura.getNetto()*fattura.getIvaPerc()/100F);
			if (!fattura.getIva().equals(ivaTot)){
				throw new Exception("Iva inconsistente: "+fattura.getIva()+" contro "+ivaTot);
			}
			Float totale = roundedNetto+ivaTot;
			if (!fattura.getTotale().equals(totale)){
				throw new Exception("Totale inconsistente: "+fattura.getTotale()+" contro "+totale);
			}
		} else {
			if (!fattura.getIva().equals(0F)){
				throw new Exception("Iva inconsistente: "+fattura.getIva()+" contro "+0);
			}
			if (!fattura.getTotale().equals(netto)){
				throw new Exception("Totale inconsistente: "+fattura.getTotale()+" contro "+netto);
			}
		}
	}

	private void checkConsistency(Bene riga) throws Exception {
		Float prezzo = riga.getPrezzo();
		Float tot = riga.getTot();
		if (prezzo==null && tot==null){
			throw new Exception("Prezzo non trovato: "+riga.toString());
		} else if (prezzo!=null){
			Float checkTot = new Float(riga.getQta())*prezzo;
			if (!NumberUtils.roundNumber(checkTot).equals(riga.getTot())){
				throw new Exception("Datin inconsistenti: "+riga);
			}
		}
	}

	private Date findStartDate(List<DdT> allDdT) {
		Date result = null;
		for (DdT ddT : allDdT) {
			Date ddtData = ddT.getData();
			if (result==null || result.after(ddtData)){
				result = ddtData;
			}
		}
		return result;
	}

	private Set<Azienda> findAziende(List<DdT> allDdT) {
		Set<Azienda> result = new TreeSet<Azienda>(new Comparator<Azienda>() {
			@Override
			public int compare(Azienda o1, Azienda o2) {
				return o1.getNome().compareToIgnoreCase(o2.getNome());
			}
		});
		for (DdT ddT : allDdT) {
			result.add(ddT.getCliente());
		}
		return result;
	}
	
}
