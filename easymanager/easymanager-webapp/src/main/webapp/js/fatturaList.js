$(document).ready(function() {
	fatturaRowTemplate = loadTemplate("templates/fatturaListRow.html");
	fatturaDeleteModalTemplate = loadTemplate("templates/fatturaListDeleteModal.html");
	
	loadFatture();
});

function loadFatture(){
	var targetUrl=getWebappUrl() + "/resources/fattura/all";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var fatture = responseData.items;
		fillTable(fatture);
	});
}

function fillTable(fatture){
	var tableBody = $("table tbody");
	var body = $("body");
	for (var i in fatture){
		var fattura = fatture[i];
		$(tableBody).append(fatturaRowTemplate(fattura));
		$(body).append(fatturaDeleteModalTemplate(fattura));
	}
}

function stampaFattura(id){
	
	var targetUrl=getWebappUrl() + "/resources/fattura/print?id="+encodeURIComponent(id);
	var urlString = "";
	doCall('GET', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Fattura stampata con successo"); 
		_.delay(function(){
			window.open(getWebappUrl() + "/resources/filesystem"+responseData.items[0],'_blank');
		}, 2000);
	});
}

function generaFatturaElettronica(id){
	var targetUrl=getWebappUrl() + "/resources/fattura/elettronica?id="+encodeURIComponent(id);
	window.open(targetUrl,'_blank');
}

function generaFatturaElettronicaWeb(id){
	var targetUrl=getWebappUrl() + "/resources/fattura/elettronica/web?id="+encodeURIComponent(id);
	window.open(targetUrl,'_blank');
}

function deleteFattura(id){
	
	var targetUrl=getWebappUrl() + "/resources/fattura?id="+encodeURIComponent(id);
	var urlString = "";
	doCall('DELETE', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Fattura cancellata con successo"); 
		_.delay(function(){
			window.location.href=getWebappUrl()+"/fatturaList.xhtml";
		}, 2000);
	});
}

function loadFatturaModal(){
	var targetUrl=getWebappUrl() + "/resources/fattura/candidates";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var startDate = responseData.items[0];
		var endDate = responseData.items[1];
		var aziende = responseData.items[2];
		$("#startDate").val(startDate);
		$("#endDate").val(endDate);
		var azienzaSelect = $("#azienda");
		$(azienzaSelect).empty();
		for (var i in aziende){
			var azienda = aziende[i];
			$(azienzaSelect).append("<option value=\""+azienda.id+"\">"+azienda.nome+"</option>");
		}
		addDatepickers(true);
		$("#newFatturaModal").modal("show");
	});
	
}