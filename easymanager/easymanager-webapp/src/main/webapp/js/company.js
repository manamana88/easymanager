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
	
	$("#giuridica").change(toggleReadOnly);
	$("#piva").change(function(){
		if ($("#giuridica").prop("checked")){
			$("#codfis").val($("#piva").val());
		}
	});
});

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
	$("#codfis").val(currentCompany.codFis);
	$("#via").val(currentCompany.via);
	$("#numero").val(currentCompany.civico);
	$("#cap").val(currentCompany.cap);
	$("#citta").val(currentCompany.citta);
	$("#provincia").val(currentCompany.provincia);
	$("#nazione").val(currentCompany.nazione);
	$("#telefono").val(currentCompany.telefono);
	$("#fax").val(currentCompany.fax);
	$("#email").val(currentCompany.mail);
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

	var urlString = formToUrlString();
	var targetUrl=getWebappUrl() + "/resources/azienda";

	doCall('POST', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Azienda registrata con successo");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}

function modificaAzienda(){
	
	var urlString = formToUrlString();
	var targetUrl=getWebappUrl() + "/resources/azienda";
	
	doCall('PUT', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Azienda modificata con successo");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}

function formToUrlString(){
	var id = $("#id").val();
	var nome = $("#nome").val();
	var giuridica = $("#giuridica").prop("checked");
	var tassabile = $("#tassabile").prop("checked");
	var piva = $("#piva").val();
	var codFis = $("#codfis").val();
	var via = $("#via").val();
	var civico = $("#numero").val();
	var cap = $("#cap").val();
	var citta = $("#citta").val();
	var provincia = $("#provincia").val();
	var nazione = $("#nazione").val();
	var telefono = $("#telefono").val();
	var fax = $("#fax").val();
	var email = $("#email").val();
	var principale = $("#principale").val();
	
	var urlString="";
	urlString += "id="+encodeURIComponent(id);
	urlString += "&nome="+encodeURIComponent(nome);
	urlString += "&piva="+encodeURIComponent(piva);
	urlString += "&codFis="+encodeURIComponent(codFis);
	urlString += "&via="+encodeURIComponent(via);
	urlString += "&civico="+encodeURIComponent(civico);
	urlString += "&cap="+encodeURIComponent(cap);
	urlString += "&citta="+encodeURIComponent(citta);
	urlString += "&provincia="+encodeURIComponent(provincia);
	urlString += "&nazione="+encodeURIComponent(nazione);
	urlString += "&mail="+encodeURIComponent(email);
	urlString += "&telefono="+encodeURIComponent(telefono);
	urlString += "&fax="+encodeURIComponent(fax);
	urlString += "&tassabile="+encodeURIComponent(tassabile);
	urlString += "&principale="+encodeURIComponent(principale);
	return urlString;
}