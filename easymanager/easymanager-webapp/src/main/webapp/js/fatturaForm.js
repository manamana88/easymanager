var datepickerOptions = {
	    format: "dd/mm/yyyy",
	    language: "it",
	    todayHighlight: true,
	    autoclose: true,
	} 

$('#emissione').datepicker(datepickerOptions);

$("input[type='checkbox']").click(function(event){
	event.preventDefault();
});
