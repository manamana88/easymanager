$(document).ready(function() {
	ddtRowTemplate = loadTemplate("templates/ddtFormRow.html");

	var ddtId = getParameterByName("ddt");
	var currentDdt;
	var targetUrl=getWebappUrl() + "/resources/ddt/next";
	doCall('GET', targetUrl, {}, "", function (responseData){
		$("#numero").val(responseData.items[0]);
		$("#data").val(responseData.items[1]);
		setChecked($("#fatturabile"), true);
		loadCompanySelector(responseData.items[2]);
		initTable();

		if (ddtId){
			var targetUrl=getWebappUrl() + "/resources/ddt?id="+encodeURIComponent(ddtId);
			doCall('GET', targetUrl, {}, "", function (responseData){
				var currentDdt = responseData.items[0];
				fillPage(currentDdt);
			});
		}
		
		var mode = getParameterByName("action");
		if (mode==="insert"){
			$("#button").bind("click", registraDdt);
		}
		if (mode!=="show"){
			addDatepickers();
		}

	});
	
});

function loadCompanySelector(companies){
	var aziendaSelect = $("#azienda");
	for (var i in companies){
		var company = companies[i];
		$(aziendaSelect).append("<option value='"+company.id+"'>"+company.nome+"</option>");
	}
	
	aziendaSelect.change(function(){
		var selectedAzienda = $("#azienda").val();
		var options = _.map(destinations[selectedAzienda],function (obj){
			return obj.name+"\n"+obj.address1+"\n"+obj.address2;
		});
		$("#destinazione").autocomplete({
			source:options.sort()
		});
	});
}

function initTable(){
	for (var i=0; i<3; i++){
		$("table tbody").append(ddtRowTemplate(createEmptyInput()));
	}
	$("td[contenteditable='true']").focus(nextRowHandler);
}

function createEmptyInput(){
	return {
	      id : "",
	      codice : "",
	      commessa : "",
	      descrizione : "",
	      qta : "",
	      prototipo : false,
	      campionario : false,
	      primoCapo : false,
	      piazzato : false,
	      interamenteAdesivato : false
	    }
}

function fillPage(currentDdt){
	fillForm(currentDdt);
	var mode = getParameterByName("action");
	if (mode==="show"){
		$(".breadcrumb li[class*='active']").text("Visualizza ddt numero "+currentDdt.id+" del "+currentDdt.data);
		makeCheckboxReadOnly();
		makeTextInputReadOnly();
		makeSelectReadonly();
		addDisabledColorToTable();
		$("#button").hide();
	} else if (mode === "edit"){
		$("td[contenteditable='true']").focus(nextRowHandler);
		$(".breadcrumb li[class*='active']").text("Modifica ddt numero "+currentDdt.id+" del "+currentDdt.data);
		$("#button").text("Salva");
		$("#button").bind("click", modificaDdt);
	}
}

function fillForm(currentDdt){
	$("#realId").val(currentDdt.realId);
	$("#numero").val(currentDdt.id);
	$("#data").val(currentDdt.data);
	setChecked($("#fatturabile"),currentDdt.fatturabile);
	selectOption("azienda", currentDdt.cliente.id);
	fillTable(currentDdt.beni);
	selectOption("mezzo", currentDdt.mezzo);
	$("#causale").val(currentDdt.causale);
	$("#colli").val(currentDdt.colli);
	$("#peso").val(currentDdt.peso);
	$("#destinazione").text(currentDdt.destinazione.replace(/"/g,"'"));
	$("#aspetto").val(currentDdt.aspettoEsteriore);
	selectOption("porto", currentDdt.porto);
	$("#ritiro").val(currentDdt.ritiro);
	$("#ordine").val(currentDdt.vostroOrdine);
	$("#ordinedata").val(currentDdt.vostroOrdineDel);
	selectOption("tipo", currentDdt.tipo);
	$("#progressivo").val(currentDdt.progressivo);
	$("#annotazioni").text(currentDdt.annotazioni);
}

function fillTable(beni){
	var table = $("table tbody");
	$(table).empty();
	for (var i in beni){
		var bene = beni[i];
		$(table).append(ddtRowTemplate(bene));
	}
}

function nextRowHandler(event) {
	var parentTr = $(event.currentTarget).parent();
	var parentTbody = parentTr.parent();
	if (parentTr.next().length == 0 && parentTbody.find("tr").length < 10) {
		parentTbody.append(ddtRowTemplate(createEmptyInput()));
		parentTbody.find("tr:last-child td[contenteditable='true']").focus(
				nextRowHandler);
	}
}

function modificaDdt() {
	try {
		var ddtObject = loadDdt();
		if (ddtObject.beni.length==0){
			throw "Ddt vuoto";
		}
		var ddt= JSON.stringify(ddtObject);
		var targetUrl=getWebappUrl() + "/resources/ddt";
		var headers = {"Content-Type": "application/json"};
		
		doCall('PUT', targetUrl, headers, ddt, function (responseData){
			notifyModal("Successo", "Ddt registrato con successo");
			_.delay(function(){
				window.location.href=getWebappUrl()+"/ddtList.xhtml";
			}, 2000);
		});
	} catch (e){
		notifyModal("Errore", e);
	}
}

function registraDdt() {
	try {
		var ddtObject = loadDdt();
		if (ddtObject.beni.length==0){
			throw "Ddt vuoto";
		}
		var ddt= JSON.stringify(ddtObject);
		var targetUrl=getWebappUrl() + "/resources/ddt";
		var headers = {"Content-Type": "application/json"};

		doCall('POST', targetUrl, headers, ddt, function (responseData){
			notifyModal("Successo", "Ddt registrato con successo");
			_.delay(function(){
				window.location.href=getWebappUrl()+"/ddtList.xhtml";
			}, 2000);
		});
	} catch (e){
		notifyModal("Errore", e);
	}
}

function loadDdt() {
	var ddtJson = {};
	ddtJson.annotazioni = $("#annotazioni").val();
	ddtJson.aspettoEsteriore = $("#aspetto").val();
	ddtJson.beni = loadBeni();
	ddtJson.causale = $("#causale").val();
	ddtJson.data = $("#data").val();
	ddtJson.destinazione = $("#destinazione").val().replace(/"/g,"'");
	ddtJson.fatturabile = isChecked($("#fatturabile"));
	ddtJson.mezzo = $("#mezzo").val();
	ddtJson.porto = $("#porto").val();
	ddtJson.ritiro = $("#ritiro").val();
	ddtJson.tipo = $("#tipo").val();
	ddtJson.vostroOrdine = $("#ordine").val();
	ddtJson.vostroOrdineDel = $("#ordinedata").val();
	ddtJson.cliente = eval($("#azienda").val());
	ddtJson.realId = eval($("#realId").val());
	try {
		ddtJson.colli = eval($("#colli").val());
	} catch (e){
		throw "Colli non valido";
	}
	try {
		ddtJson.id = eval($("#numero").val());
	} catch (e){
		throw "Numero Ddt non valido";
	}
	try {
		ddtJson.peso = eval($("#peso").val());
	} catch (e){
		throw "Peso non valido";
	}
	try {
		ddtJson.progressivo = eval($("#progressivo").val());
	} catch (e){
		throw "Numero progressivo non valido";
	}
	return ddtJson;
}

function loadBeni() {
	var result = [];
	var newBene = {};
	iterateTable(function(element, index, index2) {
		switch(index2){
		case 0:
			newBene.codice = $(element).text();
			break;
		case 1:
			newBene.commessa = $(element).text();
			break;
		case 2:
			newBene.descrizione = $(element).text();
			break;
		case 3:
			try{
				newBene.qta = eval($(element).text());
			} catch (e){
				throw "Quantità alla riga "+(eval(index)+1)+" non valida"; 
			}
			break;
		case 4:
			newBene.prototipo = $(element).find("input").prop("checked");
			break;
		case 5:
			newBene.campionario = $(element).find("input").prop("checked");
			break;
		case 6:
			newBene.primoCapo = $(element).find("input").prop("checked");
			break;
		case 7:
			newBene.piazzato = $(element).find("input").prop("checked");
			break;
		case 8:
			newBene.interamenteAdesivato = $(element).find("input").prop("checked");
			break;
		case 9:
			newBene.id = eval($(element).text());
			result.push(newBene);
			newBene = {};
			break;
		}
	});
	var newResult=[];
	for (var i in result){
		var bene = result[i];
		if ((bene.codice.trim() === "")
			&& bene.commessa.trim() === ""	
				&& bene.descrizione.trim() === ""){
			
		} else if (!bene.qta){
			var message = "Selezionare una quantità per la riga "+ (eval(i)+1);
			alert(message);
			throw message;
		} else {
			newResult.push(bene);
		}
	}
	return newResult;
}
