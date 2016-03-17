datepickerOptions = {
	format : "dd/mm/yyyy",
	language : "it",
	todayHighlight : true,
	autoclose : true,
	forceParse: false
}

function addDatepickers(){
	$('.data input').datepicker(datepickerOptions);
}

function makeTextInputReadOnly(){
	$("input").prop("readonly", "readonly");
	$("textarea").prop("readonly", "readonly");
	$("*[contenteditable='true']").prop("contenteditable", "false").prop("readonly", "readonly");
}

function makeCheckboxReadOnly(){
	$("input[type='checkbox']").click(function(event) {
		event.preventDefault();
	});
}

function makeSelectReadonly(){
	$("select").prop("disabled", "true");
}

function addDisabledColorToTable(){
	$("table").css("background-color", "#eee").css("opacity", "1");
}

function selectOption(selectId, value){
	$("#"+selectId+" option[value='"+value+"']").prop("selected", "selected");
}

function loadTemplate(path) {
	var loadedTemplate = "";
	$.ajax({
		url : path,
		cache : false,
		success : function(response) {
			loadedTemplate = _.template(response);
		},
		async : false
	});
	return loadedTemplate;
}

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

function handleResponse(response) {
	JSON.parse(response);
}

function notifyModal(label, body, autoclose) {
	$("#myModalLabel").text(label);
	$("#modal-body").text(body);
	$("#myModal").modal("show");
}

function doCall(callType, callUrl, headers, callData, success) {
	if (!headers) {
		headers = {};
	}
	$.ajax({
		type : callType,
		async : true,
		cache : false,
		url : callUrl,
		headers : headers,
		data : callData,

		beforeSend : showLoader,

		error : function(responseData) {
			alert('Failed.');
		},

		success : function(responseData) {
			var errors = responseData.error;
			if (errors) {
				for ( var i in errors) {
					notifyModal(errors[i].errorUserTitle,
							errors[i].errorUserMsg);
				}
			} else if (success) {
				if (typeof success === "string") {
					notifyModal("Successo", success);
				} else {
					success(responseData);
				}
			}
		},

		complete : hideLoader

	});
}

function getParameterByName(name, url) {
	if (!url)
		url = window.location.href;
	url = url.toLowerCase(); // This is just to avoid case sensitiveness
	name = name.replace(/[\[\]]/g, "\\$&").toLowerCase();// This is just to
															// avoid case
															// sensitiveness for
															// query parameter
															// name
	var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"), results = regex
			.exec(url);
	if (!results)
		return null;
	if (!results[2])
		return '';
	return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function iterateTable(toApply) {
	$("tbody tr").each(function(index) {
		$(this).find("td").each(function(index2) {
			toApply(this, index, index2);
		});
	});
}
