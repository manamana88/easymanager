package progettotlp.fatturapa;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import progettotlp.Constants;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.DateUtils;
import progettotlp.facilities.FatturaUtils;
import progettotlp.facilities.NumberUtils;
import progettotlp.fatturapa.jaxb.*;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;

public class FatturaPaConverter {

	private static Logger logger = LoggerFactory.getLogger(FatturaPaConverter.class);
			
	//TODO export to properties as much as possible
	private static final FormatoTrasmissioneType DEFAULT_VERSIONE = FormatoTrasmissioneType.valueOf(ConfigurationManager.getProperty("versione_fattura"));
	private static final String DEFAULT_NAZIONE_TYPE = ConfigurationManager.getProperty("nazione");
	private static final String STANDARD_CODICE_DESTINATARIO = ConfigurationManager.getProperty("codice_destinatario");
	private static final RegimeFiscaleType REGIME_FISCALE = RegimeFiscaleType.valueOf(ConfigurationManager.getProperty("regime_fiscale"));
	private static final CondizioniPagamentoType PAGAMENTO_COMPLETO = CondizioniPagamentoType.valueOf(ConfigurationManager.getProperty("condizioni_pagamento"));
	private static final ModalitaPagamentoType BONIFICO = ModalitaPagamentoType.valueOf(ConfigurationManager.getProperty("modalita_pagamento"));
	private static final String BONIFICO_BENEFICIARIO = ConfigurationManager.getProperty("bonifico_beneficiario");
	private static final String BONIFICO_ISTITUTO_FINANZIARIO = ConfigurationManager.getProperty("bonifico_istituto_finanziario");
	private static final String BONIFICO_IBAN = ConfigurationManager.getProperty("bonifico_iban");
	private static final NaturaType ESENZIONE_IVA = NaturaType.valueOf(ConfigurationManager.getProperty("esenzione_iva"));
	private static final String DEFAULT_DIVISA = ConfigurationManager.getProperty("divisa");

	private static final String CODICE_ARTICOLO_CODICE_KEY = ConfigurationManager.getProperty("codice_articolo_codice");
	private static final String CODICE_ARTICOLO_COMMESSA_KEY = ConfigurationManager.getProperty("codice_articolo_commessa");
	private static final String CODICE_ARTICOLO_CAMPIONARIO_KEY = ConfigurationManager.getProperty("codice_articolo_campionario");
	private static final String CODICE_ARTICOLO_INTERAMENTE_ADESIVATO_KEY = ConfigurationManager.getProperty("codice_articolo_interamente_adesivato");
	private static final String CODICE_ARTICOLO_PIAZZATO_KEY = ConfigurationManager.getProperty("codice_articolo_piazzato");
	private static final String CODICE_ARTICOLO_PRIMO_CAPO_KEY = ConfigurationManager.getProperty("codice_articolo_primo_capo");
	private static final String CODICE_ARTICOLO_PROTOTIPO_KEY = ConfigurationManager.getProperty("codice_articolo_prototipo");
	private static final String CODICE_ARTICOLO_RISPOSTA = ConfigurationManager.getProperty("codice_articolo_risposta");

	private static final BigDecimal DEFAULT_BOLLO = new BigDecimal(2);

	public static String createFatturaElettronicaName(FatturaElettronicaType fattura) {
		DatiTrasmissioneType datiTrasmissione = fattura.getFatturaElettronicaHeader().getDatiTrasmissione();
		IdFiscaleType idTrasmittente = datiTrasmissione.getIdTrasmittente();
		return idTrasmittente.getIdPaese()+idTrasmittente.getIdCodice()+"_"+datiTrasmissione.getProgressivoInvio()+".xml";
	}
	
	public static FatturaElettronicaType convertToFatturaPa(AziendaInterface principale, FatturaInterface fattura) throws DatatypeConfigurationException {
		FatturaElettronicaType result = new FatturaElettronicaType();
		FatturaElettronicaHeaderType header = createFatturaElettronicaHeader(principale, fattura);
		result.setFatturaElettronicaHeader(header);
		List<FatturaElettronicaBodyType> fatturaElettronicaBody = createFatturaElettronicaBodies(fattura);
		result.setFatturaElettronicaBody(fatturaElettronicaBody);
		result.setVersione(DEFAULT_VERSIONE);
		return result;
	}

	private static List<FatturaElettronicaBodyType> createFatturaElettronicaBodies(FatturaInterface fattura) throws DatatypeConfigurationException {
		List<FatturaElettronicaBodyType> result = new ArrayList<>();
		FatturaElettronicaBodyType singleBody = createFatturaElettronicaBody(fattura);
		result.add(singleBody);
		return result;
	}

	private static FatturaElettronicaBodyType createFatturaElettronicaBody(FatturaInterface fattura) throws DatatypeConfigurationException {
		FatturaElettronicaBodyType result = new FatturaElettronicaBodyType();
		DatiGeneraliType datiGenerali = createDatiGenerali(fattura);
		result.setDatiGenerali(datiGenerali);
		DatiBeniServiziType datiBeniServizi = createDatiBeniServizi(fattura);
		result.setDatiBeniServizi(datiBeniServizi);
		result.setDatiPagamento(createDatiPagamento(fattura));
		//TODO evaluate other entries
		//result.setAllegati(allegati);
		//result.setDatiVeicoli(datiVeicoli);
		return result;
	}
	
	private static List<DatiPagamentoType> createDatiPagamento(FatturaInterface fattura) throws DatatypeConfigurationException {
		DatiPagamentoType datiPagamento = new DatiPagamentoType();
		datiPagamento.setCondizioniPagamento(PAGAMENTO_COMPLETO);
		datiPagamento.setDettaglioPagamento(createDettaglioPagamento(fattura));
		return Arrays.asList(datiPagamento);
	}

	private static List<DettaglioPagamentoType> createDettaglioPagamento(FatturaInterface fattura) throws DatatypeConfigurationException {
		DettaglioPagamentoType dettaglioPagamento = new DettaglioPagamentoType();
		dettaglioPagamento.setBeneficiario(BONIFICO_BENEFICIARIO);
		dettaglioPagamento.setModalitaPagamento(BONIFICO);
		Date emissione = fattura.getEmissione();
		dettaglioPagamento.setDataRiferimentoTerminiPagamento(DateUtils.toXmlGregorianCalendar(emissione));
		Date scadenza = fattura.getScadenza();
		dettaglioPagamento.setDataScadenzaPagamento(DateUtils.toXmlGregorianCalendar(scadenza));
		Integer giorni = DateUtils.getTimeFrame(emissione, scadenza);
		dettaglioPagamento.setGiorniTerminiPagamento(giorni);
		dettaglioPagamento.setImportoPagamento(fattura.getTotale());
		dettaglioPagamento.setIstitutoFinanziario(BONIFICO_ISTITUTO_FINANZIARIO);
		dettaglioPagamento.setIBAN(BONIFICO_IBAN);
		return Arrays.asList(dettaglioPagamento);
	}

	private static DatiGeneraliType createDatiGenerali(FatturaInterface fattura) throws DatatypeConfigurationException {
		DatiGeneraliType result = new DatiGeneraliType();
		DatiGeneraliDocumentoType datiGeneraliDocumento = createDatiGeneraliDocumento(fattura);
		result.setDatiGeneraliDocumento(datiGeneraliDocumento);
		//result.setDatiOrdineAcquisto(null);
		//result.setDatiContratto(null);
		//result.setDatiConvenzione(null);
		//result.setDatiRicezione(null);
		//result.setDatiFattureCollegate(null);
		//result.setDatiSAL(null);
		result.setDatiDDT(createDatiDDT(fattura));
		//result.setDatiTrasporto(null);
		//result.setFatturaPrincipale(null);
		return result;
	}

	private static List<DatiDDTType> createDatiDDT(FatturaInterface fattura) throws DatatypeConfigurationException {
		List<DatiDDTType> result = new ArrayList<>();
		int counter = 1;
		for (DdTInterface ddTInterface : fattura.getDdt()) {
			DatiDDTType createDatiDDT = createDatiDDT(ddTInterface, counter);
			counter += ddTInterface.getBeni().size();
			result.add(createDatiDDT);
		}
		return result;
	}

	private static DatiDDTType createDatiDDT(DdTInterface ddTInterface, int counter) throws DatatypeConfigurationException {
		DatiDDTType result = new DatiDDTType();
		Date data = ddTInterface.getData();
		XMLGregorianCalendar xmlGregorianCalendar = DateUtils.toXmlGregorianCalendar(data);
		result.setDataDDT(xmlGregorianCalendar);
		result.setNumeroDDT(Integer.toString(ddTInterface.getId()));
		List<Integer> riferimentoLinee = new ArrayList<>();
		int size = ddTInterface.getBeni().size();
		for (int i=0; i<size; i++) {
			riferimentoLinee.add(counter+i);
		}
		result.setRiferimentoNumeroLinea(riferimentoLinee);
		return result;
	}

	private static DatiGeneraliDocumentoType createDatiGeneraliDocumento(FatturaInterface fattura) throws DatatypeConfigurationException {
		DatiGeneraliDocumentoType result = new DatiGeneraliDocumentoType();
		result.setTipoDocumento(TipoDocumentoType.TD_01);
		result.setDivisa(DEFAULT_DIVISA);
		result.setData(DateUtils.toXmlGregorianCalendar(fattura.getEmissione()));
		result.setNumero(Integer.toString(fattura.getId()));
		//result.setDatiRitenuta(createDatiRitenuta(fattura)); //TODO confermare non applicabile
		result.setDatiBollo(createDatiBollo(fattura)); //TODO confermare applicabile
		//result.setDatiCassaPrevidenziale //TODO aggiungere metodo e confermare non applicabile
		//result.setScontoMaggiorazione //TODO aggiungere metodo e confermare non applicabile
		result.setImportoTotaleDocumento(fattura.getTotale());
		//result.setArrotondamento //TODO aggiungere metodo e confermare non applicabile
		//result.setCausale //TODO aggiungere metodo e confermare non applicabile
		//result.getArt73(Art73Type.SI); //TODO confermare esente
		return result;
	}


	private static DatiBolloType createDatiBollo(FatturaInterface fattura) {
		if (FatturaUtils.isBolloMandatory(fattura)) {
			DatiBolloType result = new DatiBolloType();
			result.setBolloVirtuale(BolloVirtualeType.SI);
			result.setImportoBollo(DEFAULT_BOLLO);
			return result;
		}
		return null;
	}

	private static DatiBeniServiziType createDatiBeniServizi(FatturaInterface fattura) throws DatatypeConfigurationException {
		DatiBeniServiziType result = new DatiBeniServiziType();
		result.setDatiRiepilogo(createDatiRiepilogo(fattura));
		result.setDettaglioLinee(createDettaglioLinee(fattura));
		return result;
	}

	private static List<DettaglioLineeType> createDettaglioLinee(FatturaInterface fattura) throws DatatypeConfigurationException {
		List<DettaglioLineeType> result = new ArrayList<>();
		int linea = 1;
		for (DdTInterface ddt : fattura.getDdt()) {
			for (BeneInterface bene : ddt.getBeni()) {
				DettaglioLineeType createDettaglioLinea = createDettaglioLinea(ddt, bene);
				createDettaglioLinea.setNumeroLinea(linea++);
				createDettaglioLinea.setAliquotaIVA(fattura.getIvaPerc());
				result.add(createDettaglioLinea);
			}
		}
		return result;
	}

	protected static DettaglioLineeType createDettaglioLinea(DdTInterface ddt, BeneInterface bene) throws DatatypeConfigurationException {
		DettaglioLineeType dettaglioLinea = new DettaglioLineeType();
		//dettaglioLinea.setTipoCessionePrestazione(); //TODO confermare esente
		dettaglioLinea.setCodiceArticolo(createCodiceArticolo(bene));
		dettaglioLinea.setDescrizione(bene.getDescrizione());
		BigDecimal qta = bene.getQta();
		dettaglioLinea.setQuantita(qta);
		//TODO handle metri vs capi
		//dettaglioLinea.setUnitaMisura(null);
		//dettaglioLinea.setDataInizioPeriodo(null);
		//dettaglioLinea.setDataFinePeriodo(null);
		BigDecimal prezzo = bene.getPrezzo();
		if (prezzo == null) {
			BigDecimal tot = bene.getTot();
			prezzo = tot.divide(qta, Constants.DEFAULT_SCALE, RoundingMode.HALF_DOWN);
		}
		dettaglioLinea.setPrezzoUnitario(prezzo);
		List<ScontoMaggiorazioneType> scontoMaggiorazioneList = createScontoMaggiorazione(bene, prezzo);
		dettaglioLinea.setScontoMaggiorazione(scontoMaggiorazioneList);
		dettaglioLinea.setPrezzoTotale(bene.getTot());
		//dettaglioLinea.setRitenuta(null); //TODO confermare esente
		//dettaglioLinea.setRiferimentoAmministrazione(null); //TODO confermare esente
		AziendaInterface cliente = ddt.getCliente();
        if (!cliente.isTassabile()) {
            dettaglioLinea.setNatura(ESENZIONE_IVA);
			List<AltriDatiGestionaliType> altriDatiGestinali = createAltriDatiGestionali(cliente);
			dettaglioLinea.setAltriDatiGestionali(altriDatiGestinali);
		}
		return dettaglioLinea;
	}

	protected static List<AltriDatiGestionaliType> createAltriDatiGestionali(AziendaInterface cliente) throws DatatypeConfigurationException {
		AltriDatiGestionaliType altriDatiGestionali = new AltriDatiGestionaliType();
		altriDatiGestionali.setTipoDato("INTENTO");
		altriDatiGestionali.setRiferimentoTesto(cliente.getNumeroProtocollo());
		altriDatiGestionali.setRiferimentoData(DateUtils.toXmlGregorianCalendar(cliente.getDataProtocollo()));
		return Collections.singletonList(altriDatiGestionali);
	}

	protected static List<ScontoMaggiorazioneType> createScontoMaggiorazione(BeneInterface bene, BigDecimal prezzo) {
		List<ScontoMaggiorazioneType> result = new ArrayList<>();
		BigDecimal tot = bene.getTot();
		BigDecimal qta = bene.getQta();
		
		BigDecimal roundDifference = tot.subtract(prezzo.multiply(qta));
		int signum = roundDifference.signum();
		if (signum!=0) {
			ScontoMaggiorazioneType scontoMaggiorazione = new ScontoMaggiorazioneType();
			TipoScontoMaggiorazioneType tipo;
			if (signum>0) {
				tipo = TipoScontoMaggiorazioneType.MG;
			} else {
				tipo = TipoScontoMaggiorazioneType.SC;
			}
			scontoMaggiorazione.setTipo(tipo);
			scontoMaggiorazione.setImporto(NumberUtils.scale(roundDifference.abs()));
			result.add(scontoMaggiorazione);
		}
		return result;
	}

	private static List<CodiceArticoloType> createCodiceArticolo(BeneInterface bene) {
		List<CodiceArticoloType> result = new ArrayList<>();
		String codice2 = bene.getCodice();
		if (codice2!=null) {
			codice2 = replaceNBSP(codice2);
			CodiceArticoloType codice = new CodiceArticoloType();
			codice.setCodiceTipo(CODICE_ARTICOLO_CODICE_KEY);
			codice.setCodiceValore(codice2);
			result.add(codice);
		}
		String commessa2 = bene.getCommessa();
		if (commessa2!=null) {
			commessa2 = replaceNBSP(commessa2);
			CodiceArticoloType commessa = new CodiceArticoloType();
			commessa.setCodiceTipo(CODICE_ARTICOLO_COMMESSA_KEY);
			commessa.setCodiceValore(commessa2);
			result.add(commessa);
		}
		if (Boolean.TRUE.equals(bene.getCampionario())) {
			CodiceArticoloType commessa = new CodiceArticoloType();
			commessa.setCodiceTipo(CODICE_ARTICOLO_CAMPIONARIO_KEY);
			commessa.setCodiceValore(CODICE_ARTICOLO_RISPOSTA);
			result.add(commessa);
		}
		if (Boolean.TRUE.equals(bene.getInteramenteAdesivato())) {
			CodiceArticoloType commessa = new CodiceArticoloType();
			commessa.setCodiceTipo(CODICE_ARTICOLO_INTERAMENTE_ADESIVATO_KEY);
			commessa.setCodiceValore(CODICE_ARTICOLO_RISPOSTA);
			result.add(commessa);
		}
		if (Boolean.TRUE.equals(bene.getPiazzato())) {
			CodiceArticoloType commessa = new CodiceArticoloType();
			commessa.setCodiceTipo(CODICE_ARTICOLO_PIAZZATO_KEY);
			commessa.setCodiceValore(CODICE_ARTICOLO_RISPOSTA);
			result.add(commessa);
		}
		if (Boolean.TRUE.equals(bene.getPrimoCapo())) {
			CodiceArticoloType commessa = new CodiceArticoloType();
			commessa.setCodiceTipo(CODICE_ARTICOLO_PRIMO_CAPO_KEY);
			commessa.setCodiceValore(CODICE_ARTICOLO_RISPOSTA);
			result.add(commessa);
		}
		if (Boolean.TRUE.equals(bene.getPrototipo())) {
			CodiceArticoloType commessa = new CodiceArticoloType();
			commessa.setCodiceTipo(CODICE_ARTICOLO_PROTOTIPO_KEY);
			commessa.setCodiceValore(CODICE_ARTICOLO_RISPOSTA);
			result.add(commessa);
		}
		return result;
	}

	private static List<DatiRiepilogoType> createDatiRiepilogo(FatturaInterface fattura) {
		DatiRiepilogoType datiRiepilogoType = new DatiRiepilogoType();
		//TODO handle esente iva
		datiRiepilogoType.setAliquotaIVA(fattura.getIvaPerc());
		AziendaInterface cliente = fattura.getCliente();
		if (!cliente.isTassabile()) {
			datiRiepilogoType.setNatura(ESENZIONE_IVA); //TODO verificare natura esenzione
			String riferimentoNormativo = createRiferimentoNormativo(cliente);
			datiRiepilogoType.setRiferimentoNormativo(riferimentoNormativo); //TODO verificare riferimento normativo legato a natura
		}
		//datiRiepilogoType.setSpeseAccessorie(null);
		//datiRiepilogoType.setArrotondamento(null);
		datiRiepilogoType.setImponibileImporto(fattura.getNetto());
		datiRiepilogoType.setImposta(fattura.getIva());
		//datiRiepilogoType.setEsigibilitaIVA(); //TODO verificare esigibilita' IVA
		return Arrays.asList(datiRiepilogoType);
	}

	protected static String createRiferimentoNormativo(AziendaInterface cliente) {
		StringBuilder sb = new StringBuilder();
		String nazione = cliente.getNazione();
		String toLowerCase = nazione.toLowerCase();
		if(toLowerCase.startsWith("it")){
			sb.append("Art.8 co.1 lett.C DPR 633/72");
		    //law = "Non imponibile art.8 comma 1 lettera C DPR 633-1972";
		} else {
			sb.append("Art.7/ter co.1 DPR 633/1972");
		}
		sb.append(" come da vs. dichiarazione protocollo n. ");
		sb.append(cliente.getNumeroProtocollo());
//            String bollo = f.getBollo();
//			if (ConfigurationManager.getBolloLimit() < f.getTotale() && bollo != null && !bollo.trim().isEmpty()){
//            	rifBollo = "Imposta di bollo assolta sull'originale ID="+bollo;
//            }
		//TODO verifica bollo e riferimento normativo
		return sb.toString();
	}

	private static FatturaElettronicaHeaderType createFatturaElettronicaHeader(AziendaInterface principale, FatturaInterface fattura) {
		FatturaElettronicaHeaderType result = new FatturaElettronicaHeaderType();
		CedentePrestatoreType cedentePrestatore = createCedentePrestatore(principale);
		result.setCedentePrestatore(cedentePrestatore);
		CessionarioCommittenteType cessionarioCommittente = createCessionarioCommittente(fattura.getCliente());
		result.setCessionarioCommittente(cessionarioCommittente);
		result.setSoggettoEmittente(SoggettoEmittenteType.CC);
		DatiTrasmissioneType datiTrasmissione = createDatiTrasmissione(fattura, principale);
		result.setDatiTrasmissione(datiTrasmissione);
		return result;
	}

	protected static DatiTrasmissioneType createDatiTrasmissione(FatturaInterface fattura, AziendaInterface principale) {
		DatiTrasmissioneType datiTrasmissioneType = new DatiTrasmissioneType();
		datiTrasmissioneType.setIdTrasmittente(createIdIva(principale));
		datiTrasmissioneType.setProgressivoInvio(createProgressivoInvio(fattura));
		datiTrasmissioneType.setFormatoTrasmissione(FormatoTrasmissioneType.FPR_12);
		
		AziendaInterface cliente = fattura.getCliente();
		String codiceFatturaPa = cliente.getCodiceFatturaPa();
		if (codiceFatturaPa!=null && !codiceFatturaPa.trim().isEmpty()) {
			datiTrasmissioneType.setCodiceDestinatario(codiceFatturaPa);
		} else {
			datiTrasmissioneType.setCodiceDestinatario(STANDARD_CODICE_DESTINATARIO);
			datiTrasmissioneType.setPECDestinatario(cliente.getPEC());
		}
		return datiTrasmissioneType;
	}

	protected static String createProgressivoInvio(FatturaInterface fattura) {
		Date emissione = fattura.getEmissione();
		int year = DateUtils.getYear(emissione);
		Integer id = fattura.getId();
		String progressivoInvio = (year-2000)+""+id;
		logger.info("Created progressivoInvio [{}] for fattura with id [{}] and realId [{}]", progressivoInvio, fattura.getId(), fattura.getRealId());
		return progressivoInvio;
	}

	private static CessionarioCommittenteType createCessionarioCommittente(AziendaInterface cliente) {
		CessionarioCommittenteType cessionarioCommittenteType = new CessionarioCommittenteType();
		cessionarioCommittenteType.setSede(createIndirizzo(cliente));
		DatiAnagraficiCessionarioType datiAnagraficiCessionarioType = createDatiAnagraficiCessionario(cliente);
		cessionarioCommittenteType.setDatiAnagrafici(datiAnagraficiCessionarioType);
		return cessionarioCommittenteType;
	}

	protected static DatiAnagraficiCessionarioType createDatiAnagraficiCessionario(AziendaInterface cliente) {
		DatiAnagraficiCessionarioType datiAnagraficiCessionarioType = new DatiAnagraficiCessionarioType();
		datiAnagraficiCessionarioType.setAnagrafica(createAnagraficaType(cliente));
		datiAnagraficiCessionarioType.setCodiceFiscale(cliente.getCodFis());
		datiAnagraficiCessionarioType.setIdFiscaleIVA(createIdIva(cliente));
		return datiAnagraficiCessionarioType;
	}

	private static CedentePrestatoreType createCedentePrestatore(AziendaInterface principale) {
		CedentePrestatoreType cedentePrestatoreType = new CedentePrestatoreType();
		DatiAnagraficiCedenteType datiAnagraficiCedente = createDatiAnagraficiCedente(principale);
		cedentePrestatoreType.setDatiAnagrafici(datiAnagraficiCedente);
		IndirizzoType indirizzoType = createIndirizzo(principale);
		cedentePrestatoreType.setSede(indirizzoType);
		ContattiType contatti = createContatti(principale);
		cedentePrestatoreType.setContatti(contatti);
		return cedentePrestatoreType;
	}

	private static ContattiType createContatti(AziendaInterface principale) {
		ContattiType contatti = new ContattiType();
		contatti.setEmail(principale.getMail());
		contatti.setFax(principale.getFax());
		contatti.setTelefono(principale.getTelefono());
		return contatti;
	}

	private static IndirizzoType createIndirizzo(AziendaInterface azienda) {
		IndirizzoType indirizzoType = new IndirizzoType();
		indirizzoType.setCAP(azienda.getCap());
		indirizzoType.setComune(azienda.getCitta());
		indirizzoType.setIndirizzo(azienda.getVia());
		//indirizzoType.setNazione(azienda.getNazione());
		//TODO handleNazione
		indirizzoType.setNazione(DEFAULT_NAZIONE_TYPE);
		String civico = azienda.getCivico();
		if (civico!=null && !civico.trim().isEmpty()) {
			indirizzoType.setNumeroCivico(civico);
		}
		indirizzoType.setProvincia(azienda.getProvincia());
		return indirizzoType;
	}

	protected static DatiAnagraficiCedenteType createDatiAnagraficiCedente(AziendaInterface principale) {
		DatiAnagraficiCedenteType datiAnagraficiCedenteType = new DatiAnagraficiCedenteType();
		AnagraficaType anagrafica = createAnagraficaType(principale);
		datiAnagraficiCedenteType.setAnagrafica(anagrafica);
		IdFiscaleType idFiscale = createIdIva(principale);
		datiAnagraficiCedenteType.setIdFiscaleIVA(idFiscale);
		datiAnagraficiCedenteType.setRegimeFiscale(REGIME_FISCALE);
		return datiAnagraficiCedenteType;
	}

	private static IdFiscaleType createIdFiscale(AziendaInterface principale) {
		IdFiscaleType idFiscaleType = new IdFiscaleType();
		idFiscaleType.setIdCodice(principale.getCodFis());
		idFiscaleType.setIdPaese(DEFAULT_NAZIONE_TYPE);
		return idFiscaleType;
	}

	private static IdFiscaleType createIdIva(AziendaInterface principale) {
		IdFiscaleType idFiscaleType = new IdFiscaleType();
		idFiscaleType.setIdCodice(principale.getPIva());
		idFiscaleType.setIdPaese(DEFAULT_NAZIONE_TYPE);
		return idFiscaleType;
	}

	private static AnagraficaType createAnagraficaType(AziendaInterface principale) {
		AnagraficaType anagraficaType = new AnagraficaType();
		anagraficaType.setDenominazione(principale.getNome());
		return anagraficaType;
	}

	private static String replaceNBSP(String string) {
		return string.replaceAll( "\u00a0", "" );
	}
}
