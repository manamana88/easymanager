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

function stampaDdt(id){
	var targetUrl=getWebappUrl() + "/resources/ddt/print?id="+encodeURIComponent(id);
	var urlString = "";
	doCall('GET', targetUrl, {}, urlString, function (responseData){
		notifyModal("Successo", "Ddt stampato con successo"); 
		_.delay(function(){
			window.open(getWebappUrl() + "/resources/filesystem"+responseData.items[0],'_blank');
		}, 2000);
	});
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