package progettotlp.rest.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import progettotlp.classes.Azienda;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.YetExistException;
import progettotlp.facilities.*;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.fatturapa.FatturaPaConverter;
import progettotlp.fatturapa.jaxb.FatturaElettronicaType;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;
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
		FatturaInterface first = fatturaManager.get(Fattura.class, id);
		FatturaInterface initialized = fatturaManager.getFattura(first.getId(), true, true);
		return Response.ok(BeanUtils.createResponseBean(new FatturaBean(initialized)), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@GET
	@Path("/elettronica")
	@Produces(MediaType.APPLICATION_XML)
	public Response getElettronica(
			@QueryParam("id") Long id) throws Exception {
		FatturaElettronicaType converted = getFatturaPA(id);
		String result = marshalFatturaPA(converted);
		String filename = FatturaPaConverter.createFatturaElettronicaName(converted);
		return Response.ok(result).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+filename+"\"").build();
	}

	private String marshalFatturaPA(FatturaElettronicaType converted) throws JAXBException, SAXException, PropertyException {
		JAXBContext jaxbContext = JAXBContext.newInstance(FatturaElettronicaType.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        StreamSource source = new StreamSource(FatturaPaConverter.class.getClassLoader().getResourceAsStream("fatturapa_v1.2.1.xsd"));
        Schema schema = schemaFactory.newSchema(source);
        jaxbMarshaller.setSchema(schema);

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter stringWriter = new StringWriter();
		jaxbMarshaller.marshal(converted, stringWriter);
		
		String result = stringWriter.toString();
		return result;
	}

	private FatturaElettronicaType getFatturaPA(Long id) throws DatatypeConfigurationException {
		FatturaInterface first = fatturaManager.get(Fattura.class, id);
		FatturaInterface initialized = fatturaManager.getFattura(first.getId(), true, true);
		AziendaInterface aziendaPrincipale = aziendaManager.getAziendaPrincipale();
		FatturaElettronicaType converted = FatturaPaConverter.convertToFatturaPa(aziendaPrincipale, initialized);
		return converted;
	}

	@GET
	@Path("/elettronica/web")
	@Produces(MediaType.TEXT_HTML)
	public Response getElettronica(
			@QueryParam("id") Long id,
			@Context UriInfo uriInfo) throws Exception {
		FatturaElettronicaType converted = getFatturaPA(id);
		String result = marshalFatturaPA(converted);
		
		TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(FatturaPaConverter.class.getClassLoader().getResourceAsStream("fatturaordinaria_v1.2.1.xsl"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new StringReader(result));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		transformer.transform(text, new StreamResult(outputStream));
		byte[] byteArray = outputStream.toByteArray();
		return Response.ok(byteArray).build();
	}
	
	@GET
	@Path("print")
	@Produces(MediaType.APPLICATION_JSON)
	public Response print(
			@QueryParam("id") Long id) throws GenericExceptionToPrint {
        try {
        	System.out.println("ResourceStart"+System.currentTimeMillis());
            FatturaInterface toPrint = fatturaManager.get(Fattura.class, id);
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
            throw new GenericExceptionToPrint("Errore", "Siamo spiacenti, si � verificato un errore."+'\n'+"Impossibile stampare la fattura",ex);
        }
	}
	
	@GET
	@Path("candidates")
	@Produces(MediaType.APPLICATION_JSON)
	public Response candidates() throws ParseException {
		List<DdT> allDdT = ddtManager.getAllDdTWithoutFattura(false, false);
		Set<AziendaInterface> aziende = findAziende(allDdT);
		Date referenceDate = findStartDate(allDdT);
		Date endDate = DateUtils.getFatturaDay(referenceDate);
		Date startDate = DateUtils.getFirstDayOfMonth(referenceDate);
		return Response.ok(BeanUtils.createResponseBean(startDate, endDate, aziende), MediaType.APPLICATION_JSON_TYPE).build();
	}

	@GET
	@Path("next")
	@Produces(MediaType.APPLICATION_JSON)
	public Response next(
			@QueryParam("ddt") Long ddtId,
			@QueryParam("azienda") Long aziendaId,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) throws ParseException {
		List<DdTInterface> allDdT;
		AziendaInterface azienda;
		if (ddtId != null) {
			DdTInterface ddt = ddtManager.getDdT(ddtId, true, false);
			allDdT = Arrays.asList(ddt);
			azienda = ddt.getCliente();
		} else {
			azienda = aziendaManager.get(Azienda.class, aziendaId);
			Date startDateObject = DateUtils.parseDate(startDate);
			Date endDateObject = DateUtils.parseDate(endDate);
			allDdT = ddtManager.getAllDdTWithoutFattura(azienda, startDateObject, endDateObject);
		}
		int lastFattura = fatturaManager.getLastFattura();
		BigDecimal totCapi = Utility.getTotCapi(allDdT);
		BigDecimal ivaDefault;
		if (azienda.isTassabile()){
			ivaDefault = new BigDecimal(ConfigurationManager.getProperty(Property.IVA_DEFAULT));
		} else {
			ivaDefault = new BigDecimal("0");
		}
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
			for (FatturaInterface fattura : lst) {
				fattura.setDdt(null);
			}
		}
		return Response.ok(BeanUtils.createResponseBean(lst.toArray()), MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
			@QueryParam("id") Long id) throws PersistenzaException {
		FatturaInterface fattura = fatturaManager.get(Fattura.class, id);
		fatturaManager.cancellaFattura(fattura.getId());
		return Response.ok().build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(Fattura f) throws Exception {
		FatturaInterface fattura = caricaFattura(f);
        if (!fatturaManager.existsFattura(fattura.getId())){
            try{
                fatturaManager.registraFattura(fattura);
                return Response.ok(fattura, MediaType.APPLICATION_JSON_TYPE).build();
            } catch (Exception e){
                throw new GenericExceptionToPrint("Errore", "Errore nella registrazione della fattura n� "+f.getId()+".",e);
            }
        } else {
            throw new YetExistException("Errore","Esiste gi� una fattura con lo stesso numero.");
        }
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response modifica(Fattura f) throws Exception {
		FatturaInterface fattura = caricaFattura(f);
		try{
            fatturaManager.modificaFattura(fattura);
            return Response.ok(fattura, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (Exception e){
            throw new GenericExceptionToPrint("Errore", String.format("Errore nella registrazione della fattura numero %d.", fattura.getId()),e);
        }
	}

	private FatturaInterface caricaFattura(FatturaInterface f) throws Exception {
		AziendaInterface clienteAzienda = aziendaManager.get(Azienda.class, f.getCliente().getId());
		int month = DateUtils.getMonth(f.getEmissione());
		List<DdTInterface> listaDdT=ddtManager.getAllDdT(clienteAzienda, month,true,true);
        Map<Integer,Map<Long,BeneInterface>> mapping=Utility.mapDdT(listaDdT);
        List<DdTInterface> ddtToAdd = new ArrayList<>();
        for (DdTInterface d : f.getDdt()){
        	Integer idDdT = d.getId();
        	for (BeneInterface bene : d.getBeni()) {
	            Long idBene = bene.getId();
	            BeneInterface riga = mapping.get(idDdT).get(idBene);
	            if (riga == null){
	            	throw new Exception("Impossibile trovare questo bene: ["+bene.toString()+"]");
	            }
	            BigDecimal prezzo = bene.getPrezzo();
	            if (prezzo!=null) {
	            	riga.setPrezzo(NumberUtils.scale(prezzo));
	            }
	            riga.setTot(NumberUtils.scale(bene.getTot()));
	            checkConsistency(riga);
        	}
        	for (DdTInterface ddt : listaDdT) {
        		if (ddt.getId().equals(idDdT)){
        			ddtToAdd.add(ddt);
        		}
			}
        }
        Boolean tassabile = clienteAzienda.isTassabile();
        BigDecimal netto = NumberUtils.scale(f.getNetto());
        BigDecimal ivaPerc;
        BigDecimal ivaTotale;
        BigDecimal totale;
		if (tassabile){
			ivaPerc = f.getIvaPerc();
			ivaTotale = NumberUtils.scale(f.getIva());
			totale = NumberUtils.scale(f.getTotale());
        } else {
        	ivaPerc = new BigDecimal("0");
        	ivaTotale = new BigDecimal("0");
        	totale = netto;
        }
		FatturaInterface fattura = new Fattura(ddtToAdd, f.getEmissione(), f.getScadenza(), f.getId(), clienteAzienda,
        		netto, ivaPerc, ivaTotale, totale, f.getBollo());
        checkConsistency(fattura, tassabile);
        Long realId = f.getRealId();
		if (realId!=null){
        	fattura.setRealId(realId);
        }
		return fattura;
	}
	
	private void checkConsistency(FatturaInterface fattura, Boolean tassabile) throws Exception {
		BigDecimal netto=new BigDecimal("0");
		for (DdTInterface ddt : fattura.getDdt()) {
			for (BeneInterface bene : ddt.getBeni()) {
				netto = netto.add(bene.getTot());
			}
		}
		BigDecimal roundedNetto = NumberUtils.scale(netto);
		if (!fattura.getNetto().equals(roundedNetto)){
			throw new Exception("Netto inconsistente: "+fattura.getNetto()+" contro "+roundedNetto);
		}
		
		if (tassabile){
			BigDecimal netto2 = fattura.getNetto();
			BigDecimal multiply = netto2.multiply(fattura.getIvaPerc());
			BigDecimal ivaTot = multiply.divide(NumberUtils.scale(new BigDecimal("100")));
			if (!fattura.getIva().equals(ivaTot)){
				throw new Exception("Iva inconsistente: "+fattura.getIva()+" contro "+ivaTot);
			}
			BigDecimal totale = NumberUtils.scale(roundedNetto.add(ivaTot));
			if (!fattura.getTotale().equals(totale)){
				throw new Exception("Totale inconsistente: "+fattura.getTotale()+" contro "+totale);
			}
		} else {
			if (!fattura.getIva().equals(0F)){
				throw new Exception("Iva inconsistente: "+fattura.getIva()+" contro "+0);
			}
			if (!fattura.getTotale().equals(roundedNetto)){
				throw new Exception("Totale inconsistente: "+fattura.getTotale()+" contro "+roundedNetto);
			}
		}
	}

	private void checkConsistency(BeneInterface riga) throws Exception {
		BigDecimal prezzo = riga.getPrezzo();
		BigDecimal tot = riga.getTot();
		if (prezzo==null && tot==null){
			throw new Exception("Prezzo non trovato: "+riga.toString());
		} else if (prezzo!=null){
			BigDecimal checkTot = riga.getQta().multiply(prezzo);
			if (!NumberUtils.scale(checkTot).equals(riga.getTot())){
				throw new Exception("Dati inconsistenti: "+riga);
			}
		}
	}

	private Date findStartDate(List<DdT> allDdT) {
		Date result = null;
		for (DdTInterface ddT : allDdT) {
			Date ddtData = ddT.getData();
			if (result==null || result.after(ddtData)){
				result = ddtData;
			}
		}
		return result;
	}

	private Set<AziendaInterface> findAziende(List<DdT> allDdT) {
		Set<AziendaInterface> result = new TreeSet<AziendaInterface>(new Comparator<AziendaInterface>() {
			@Override
			public int compare(AziendaInterface o1, AziendaInterface o2) {
				return o1.getNome().compareToIgnoreCase(o2.getNome());
			}
		});
		for (DdTInterface ddT : allDdT) {
			result.add(ddT.getCliente());
		}
		return result;
	}
	
}
