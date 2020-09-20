var waypoint;
var currentOffset = 0;

$(document).ready(function() {
	ddtRowTemplate = loadTemplate("templates/ddtListRow.html");
	ddtDeleteModalTemplate = loadTemplate("templates/ddtListDeleteModal.html");

	loadDdts();
});

function loadDdts(){
	var targetUrl=getWebappUrl() + "/resources/ddt/all?limit=20&offset="+currentOffset;
	doCall('GET', targetUrl, {}, "", function (responseData){
		var ddts = responseData.items;
		fillTable(ddts);
		currentOffset += ddts.length;
		setWaypoint(ddts.length);
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

function setWaypoint(lastQuerySize){
	if (!_.isUndefined(waypoint)){
		waypoint.destroy();
		waypoint = undefined;
	}
	if(lastQuerySize>0){
		waypoint = new Waypoint({
			element: $("table tbody tr").last(),
			offset: "bottom-in-view",
			handler: loadDdts
		})
	}
}

function stampaDdt(id){
	window.open(getWebappUrl() + "/resources/ddt/print?id="+encodeURIComponent(id),'_blank');
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