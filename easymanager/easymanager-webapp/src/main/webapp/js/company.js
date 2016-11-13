$( document ).ready(function() {
	
	var companyId = getParameterByName("company");
	var currentCompany;
	if (companyId){
		var targetUrl=getWebappUrl() + "/resources/azienda?id="+encodeURIComponent(companyId);
		doCall('GET', targetUrl, {}, "", function (responseData){
			var currentCompany = responseData.items[0];
			fillPage(currentCompany);
		});
	}
	
	var mode = getParameterByName("action");
	if (mode==="insert"){
		$("#button").bind("click", registraAzienda);
	}
	$("#tassabile").change(tassabileChangeHandler);
	if (mode!=="show"){
		addDatepickers(true);
		tassabileChangeHandler();
	}
	$("#giuridica").change(toggleReadOnly);
	$("#piva").change(function(){
		if ($("#giuridica").prop("checked")){
			$("#codfis").val($("#piva").val());
		}
	});
});

function tassabileChangeHandler(){
	if ($("#tassabile").prop("checked")){
		$("#numAut").prop("readonly", "readonly");
		$("#numAut").val("");
		$("#dataAut").prop("readonly", "readonly");
		$("#dataAut").val("");
		$("#numReg").prop("readonly", "readonly");
		$("#numReg").val("");
		$("#dataReg").prop("readonly", "readonly");
		$("#dataReg").val("");
		removeDatepickers();
	} else {
		$("#numAut").removeAttr("readonly");
		$("#dataAut").removeAttr("readonly");
		$("#numReg").removeAttr("readonly");
		$("#dataReg").removeAttr("readonly");
		addDatepickers(true);
	}
}

function fillPage(currentCompany){
	var mode = getParameterByName("action");
	if (mode==="show"){
		$(".breadcrumb li[class*='active']").text("Visualizza "+currentCompany.nome);
		fillForm(currentCompany);
		makeCheckboxReadOnly();
		makeTextInputReadOnly();
		$("#button").hide();
	} else if (mode === "edit"){
		$(".breadcrumb li[class*='active']").text("Modifica "+currentCompany.nome);
		fillForm(currentCompany);
		$("#button").text("Salva");
		$("#button").bind("click", modificaAzienda);
		toggleReadOnly();
	}
}

function fillForm(currentCompany){
	$("#id").val(currentCompany.id);
	$("#principale").val(currentCompany.principale);
	$("#nome").val(currentCompany.nome);
	$('#giuridica').prop('checked', currentCompany.pIva === currentCompany.codFis);
	$('#tassabile').prop('checked', currentCompany.tassabile);
	$("#piva").val(currentCompany.pIva);
	$("#numAut").val(currentCompany.numeroAutorizzazione); 
	$("#dataAut").val(currentCompany.dataAutorizzazione); 
	$("#codfis").val(currentCompany.codFis);
	$("#numReg").val(currentCompany.numeroRegistrazione); 
	$("#dataReg").val(currentCompany.dataRegistrazione); 
	$("#via").val(currentCompany.via);
	$("#numero").val(currentCompany.civico);
	$("#cap").val(currentCompany.cap);
	$("#citta").val(currentCompany.citta);
	$("#provincia").val(currentCompany.provincia);
	$("#nazione").val(currentCompany.nazione);
	$("#telefono").val(currentCompany.telefono);
	$("#fax").val(currentCompany.fax);
	$("#email").val(currentCompany.mail);
	$("#numAut").val(currentCompany.numeroAutorizzazione);
	$("#dataAut").val(currentCompany.dataAutorizzazione);
	$("#numReg").val(currentCompany.numeroRegistrazione);
	$("#dataReg").val(currentCompany.dataRegistrazione);
}

function lockForm(){
	$("input[type='text']").prop('readonly','readonly');
}

function toggleReadOnly(){
	if ($("#giuridica").prop("checked")){
		$("#codfis").prop("readonly", "readonly");
		$("#codfis").val($("#piva").val());
	} else {
		$("#codfis").removeProp("readonly");
	}
}

function registraAzienda(){

	var aziendaObject = loadAzienda();
	var aziendaJson = JSON.stringify(aziendaObject);
	var headers = {"Content-Type": "application/json"};
	var targetUrl=getWebappUrl() + "/resources/azienda";

	doCall('POST', targetUrl, headers, aziendaJson, function (responseData){
		notifyModal("Successo", "Azienda registrata con successo");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}

function modificaAzienda(){
	
	var aziendaObject = loadAzienda();
	var aziendaJson = JSON.stringify(aziendaObject);
	var headers = {"Content-Type": "application/json"};
	var targetUrl=getWebappUrl() + "/resources/azienda";
	
	doCall('PUT', targetUrl, headers, aziendaJson, function (responseData){
		notifyModal("Successo", "Azienda modificata con successo");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}

function loadAzienda(){
	var azienda = {};
	azienda.id = $("#id").val();
    azienda.nome = $("#nome").val();
    azienda.pIva = $("#piva").val();
    azienda.codFis = $("#codfis").val();
    azienda.via = $("#via").val();
    azienda.civico = $("#numero").val();
    azienda.cap = $("#cap").val();
    azienda.citta = $("#citta").val();
    azienda.provincia = $("#provincia").val();
    azienda.nazione = $("#nazione").val();
    azienda.mail = $("#email").val();
    azienda.telefono = $("#telefono").val();
    azienda.fax = $("#fax").val();
    azienda.tassabile = $("#tassabile").prop("checked");
    azienda.principale = $("#principale").val();
    if (!azienda.principale) {
    	azienda.principale = false;
    }
    azienda.numeroAutorizzazione = $("#numAut").val();
    azienda.dataAutorizzazione = $("#dataAut").val();
    azienda.numeroRegistrazione = $("#numReg").val();
    azienda.dataRegistrazione = $("#dataReg").val();
    return azienda;
}