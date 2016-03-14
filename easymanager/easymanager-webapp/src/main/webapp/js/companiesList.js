
function deleteAzienda(id){
	
	var targetUrl=getWebappUrl() + "/resources/azienda?id="+encodeURIComponent(id);
	var urlString = "";
	doCall('DELETE', targetUrl, urlString, function (responseData){
		$("#myModalLabel").text("Successo");
		$("#modal-body").text("Azienda cancellata con successo");
		$("#myModal").modal("show");
		_.delay(function(){
			window.location.href=getWebappUrl()+"/companiesList.xhtml";
		}, 2000);
	});
}