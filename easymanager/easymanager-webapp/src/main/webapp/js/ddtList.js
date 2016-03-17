$(document).ready(function() {
	ddtRowTemplate = loadTemplate("templates/ddtListRow.html");
	ddtDeleteModalTemplate = loadTemplate("templates/ddtListDeleteModal.html");
	
	loadDdts();
});

function loadDdts(){
	var targetUrl=getWebappUrl() + "/resources/ddt/all";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var ddts = responseData.items;
		fillTable(ddts);
	});
}

function fillTable(ddts){
	var tableBody = $("table tbody");
	var body = $("body");
	for (var i in ddts){
		var ddt = ddts[i];
		$(tableBody).append(ddtRowTemplate(ddt));
		$(body).append(ddtDeleteModalTemplate(ddt));
	}
}

function deleteDdt(id){
	
	var targetUrl=getWebappUrl() + "/resources/ddt?id="+encodeURIComponent(id);
	var urlString = "";
	doCall('DELETE', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Ddt cancellato con successo");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/ddtList.xhtml";
		}, 2000);
	});
}