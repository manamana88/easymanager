$( document ).ready(function() {
	if (getParameterByName("action")!=="show"){
		toggleReadOnly();
	}
	$("#giuridica").change(toggleReadOnly);
	$("#piva").change(function(){
		if ($("#giuridica").prop("checked")){
			$("#codfis").val($("#piva").val());
		}
	});
});

function toggleReadOnly(){
	if ($("#giuridica").prop("checked")){
		$("#codfis").prop("readonly", "readonly");
	} else {
		$("#codfis").removeProp("readonly");
	}
}

function registraAzienda(){

	var urlString = formToUrlString();
	var targetUrl=getWebappUrl() + "/resources/azienda";

	doCall('POST', targetUrl, urlString, function (responseData){
		$("#myModalLabel").text("Successo");
		$("#modal-body").text("Azienda registrata con successo");
		$("#myModal").modal("show");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}

function modificaAzienda(){
	
	var urlString = formToUrlString();
	var targetUrl=getWebappUrl() + "/resources/azienda";
	
	doCall('PUT', targetUrl, urlString, function (responseData){
		$("#myModalLabel").text("Successo");
		$("#modal-body").text("Azienda modificata con successo");
		$("#myModal").modal("show");
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