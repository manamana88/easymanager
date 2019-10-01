$(document).ready(function() {
	companyRowTemplate = loadTemplate("templates/companyListRow.html");
	companyDeleteModalTemplate = loadTemplate("templates/companyListDeleteModal.html");
	
	loadCompanies();
});

function loadCompanies(){
	var targetUrl=getWebappUrl() + "/resources/azienda/all";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var aziendeNonPrincipali = responseData.items;
		fillTable(aziendeNonPrincipali);
	});
}

function fillTable(aziendeNonPrincipali){
	var tableBody = $("table tbody");
	var body = $("body");
	for (var i in aziendeNonPrincipali){
		var azienda = aziendeNonPrincipali[i];
		$(tableBody).append(companyRowTemplate(azienda));
		$(body).append(companyDeleteModalTemplate(azienda));
	}
}

function deleteAzienda(id){
	
	var targetUrl=getWebappUrl() + "/resources/azienda?id="+encodeURIComponent(id);
	var urlString = "";
	doCall('DELETE', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Azienda cancellata con successo"); 
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}