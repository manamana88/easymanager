$( document ).ready(function() {
	$("input[type='checkbox'].preventCheckbox").click(function(event){
		event.preventDefault();
	});
});

function showLoader() {
	$('#sk-circle-container').show();
}

function hideLoader() {
	$('#sk-circle-container').hide();
}

function getWebappUrl() {
	var pathArray = location.href.split('/');
	var protocol = pathArray[0];
	var host = pathArray[2];
	var webappName = pathArray[3];
	return protocol + '//' + host + '/' + webappName;
}

function handleResponse(response){
	JSON.parse(response);
}

function doCall(callType, callUrl, callData, success){
	$.ajax({
		type : callType,
		async : true,
		cache: false,
		url : callUrl,
		data : callData,

		beforeSend : showLoader,

		error : function(responseData) {
			alert('Failed.');
		},

		success : function(responseData) {
			var errors = responseData.error;
			if (errors){
				for (var i in errors){
					$("#myModalLabel").text(errors[i].errorUserTitle);
					$("#modal-body").text(errors[i].errorUserMsg);
					$("#myModal").modal("show");
				}
			} else if (success){
				if (typeof success === "string"){
					$("#myModalLabel").text("Successo");
					$("#modal-body").text(success);
					$("#myModal").modal("show");
				} else {
					success(responseData);
				}
			}
		},

		complete : hideLoader

	});
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    url = url.toLowerCase(); // This is just to avoid case sensitiveness  
    name = name.replace(/[\[\]]/g, "\\$&").toLowerCase();// This is just to avoid case sensitiveness for query parameter name
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}