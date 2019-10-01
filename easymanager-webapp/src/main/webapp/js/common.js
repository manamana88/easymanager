commonMultiplier = 100;

datepickerOptions = {
	format : "dd/mm/yyyy",
	language : "it",
	todayHighlight : true,
	autoclose : true,
	forceParse: false
}

function addDatepickers(updateDate){
	$('.data input').datepicker(datepickerOptions);
	if (updateDate){
		$('.data input').each(function (){
			var currentDateString = $(this).val();
			if (currentDateString){
				var currentDate = new Date(currentDateString.substring(6), currentDateString.substring(3,5), currentDateString.substring(0,2));
			}
		});
	}
}

function removeDatepickers(){
	$('.data input').datepicker("remove");
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

function makeAllReadonly(){
	makeCheckboxReadOnly();
	makeTextInputReadOnly();
	makeSelectReadonly();
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

function multiply(a, b){
	var roundedA = round(a * commonMultiplier) ;
	var roundedB = round(b * commonMultiplier);
	return round((roundedA * roundedB) / (commonMultiplier * commonMultiplier));
}

function round(a){
	return Math.round(a*commonMultiplier) / 100;
}

function isChecked(checkbox){
	return $(checkbox).prop("checked");
}

function setChecked(checkbox, value){
	var toSet = value ? "checked" : "";
	return $(checkbox).prop("checked", toSet);
}

function displaySelezionaAnnoModal(){
	var baseUrl = getWebappUrl() + "/resources/anno";
	var targetUrl=baseUrl + "/list";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var anni = responseData.items;
		var annoSelect = $("#annoGenerale");
		$(annoSelect).empty();
		for (var i in anni){
			var anno = anni[i];
			$(annoSelect).append("<option value=\""+anno+"\">"+anno+"</option>");
		}
		doCall('GET', baseUrl, {}, "", function (responseData){
			$("#annoGenerale").val(responseData.items[0]);
			$("#selectAnnoModal").modal("show");
		});
	});
}

function salvaAnno(){
	var anno = $("#annoGenerale").val();
	if (!anno){
		notifyModal("Errore", "Anno non selezionato");
	}
	var targetUrl = getWebappUrl() + "/resources/anno?year="+anno;
	doCall('POST', targetUrl, {}, "", function (responseData){
		notifyModal("Successo", "Anno registrato con successo");
		_.delay(function(){
			window.location.reload(true);
		}, 2000);
	});
}
