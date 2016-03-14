function loadTemplate(path) {
	var loadedTemplate = "";
	$.ajax({
		url : path,
		cache: false,
		success : function(response) {
			loadedTemplate = _.template(response);
		},
		async: false
	});
	return loadedTemplate;
}

function nextRowHandler(event){
	var parentTr = $(event.currentTarget).parent();
	var parentTbody = parentTr.parent();
	if (parentTr.next().length==0 && parentTbody.find("tr").length <10){
		parentTbody.append(ddtRowTemplate());
		parentTbody.find("tr:last-child td[contenteditable='true']").focus(nextRowHandler);
	}
}

ddtRowTemplate = loadTemplate("templates/ddtRow.html");

var datepickerOptions = {
	    format: "dd/mm/yyyy",
	    language: "it",
	    todayHighlight: true,
	    autoclose: true,
	} 

$('#data').datepicker(datepickerOptions);
$('#ritiro').datepicker(datepickerOptions);
$('#ordinedata').datepicker(datepickerOptions);

$("td[contenteditable='true']").focus(nextRowHandler);
