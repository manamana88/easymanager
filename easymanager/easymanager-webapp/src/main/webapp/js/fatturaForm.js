$(document).ready(function() {
	fatturaRowTemplate = loadTemplate("templates/fatturaFormRow.html");
	cancellaDdtFatturaModalTemplate = loadTemplate("templates/cancellaDdtFatturaModal.html");

	var fatturaId = getParameterByName("fattura");
	var mode = getParameterByName("action");
	var azienda = getParameterByName("azienda");
	var startDate = getParameterByName("startDate");
	var endDate = getParameterByName("endDate");
	if (fatturaId){
		var targetUrl=getWebappUrl() + "/resources/fattura?id="+encodeURIComponent(fatturaId);
		doCall('GET', targetUrl, {}, "", function (responseData){
			var currentFattura = responseData.items[0];
			fillPage(currentFattura);
			makeCheckboxReadOnly();
			if (mode==="show"){
				makeAllReadonly();
				$("#button").hide();
				$(".breadcrumb li[class*='active']").text("Visualizza fattura numero "+currentFattura.id);
				$("td[id*='-01'").detach(); $($("th")[0]).detach();
			} else {
				suggestPrices();
				enableEditControls();
				$("#button").text("Salva");
				$("#button").bind("click", modificaFattura);
				$(".breadcrumb li[class*='active']").text("Modifica fattura numero "+currentFattura.id);
			}
		});
	} else if (azienda && startDate && endDate){
		var targetUrl=getWebappUrl() + "/resources/fattura/next?azienda="+encodeURIComponent(azienda)+
							"&startDate="+encodeURIComponent(startDate)+"&endDate="+encodeURIComponent(endDate);
		doCall('GET', targetUrl, {}, "", function (responseData){
			var nextFatturaId = responseData.items[0];
			var iva = responseData.items[1];
			var capiTot = responseData.items[2];
			var ddtList = responseData.items[3];
			var azienda = responseData.items[4];
			
			$("#clienteId").val(azienda.id);
			$("#numero").val(nextFatturaId);
			$("#cliente").val(azienda.nome);
			$("#emissione").val(endDate);
			fillTable(ddtList, false);
			$("#capitot").val(capiTot);
			$("#iva").val(iva);
			suggestPrices();
			
			var scadenzaUrl=getWebappUrl() + "/resources/utils/calculateScadenza?date="
					+encodeURIComponent(endDate)+"&giorni="+$("#scadenzaGiorni").val();
			doCall('GET', scadenzaUrl, {}, "", function (responseData){
				$("#scadenza").val(responseData.items[0]);
			});
			enableEditControls();
			$("#button").bind("click", registraFattura);
		});
		
	} else {
		notifyModal("Errore", "Errore inaspettato");
	}
	
});

function suggestPrices(){
	$("tbody tr").each(function(index){
		var tdList=$(this).find("td");
		var bene={};
		bene.codice=$(tdList[2]).text();
	    bene.prototipo=$(tdList[5]).find("input").prop("checked");
	    bene.campionario=$(tdList[6]).find("input").prop("checked");
	    bene.primoCapo=$(tdList[7]).find("input").prop("checked");
	    bene.piazzato=$(tdList[8]).find("input").prop("checked");
	    $.ajax({
			type : "POST",
			async : true,
			cache : false,
			url : getWebappUrl() + "/resources/bene/alreadyBilled",
			headers : {"Content-Type":"application/json"},
			data : JSON.stringify(bene),
			success : function(responseData) {
				var errors = responseData.error;
				if (errors) {
					for ( var i in errors) {
						notifyModal(errors[i].errorUserTitle,
								errors[i].errorUserMsg);
					}
				}
			}
		});
	});
}

function enableEditControls(){
	$(".tdEditable").bind("keyup", recalculateTotHandler);
	$(".tdEditable").focusout(validateValue);
	$(".recalculate").change(recalculateDateHandler);
	addDatepickers(true);
}

function validateValue(event){
	var target = event.target;
	var trimText = $(target).text().trim().replace(",",".");
	var row = getRow(target);
	try {
		if (trimText) {
			eval(trimText);
			$(target).text(trimText);
		}
		var unitario = $("#td"+row+"10").text().trim();
		var flat = $("#td"+row+"11").text().trim();
		if (!unitario && !flat){
			var prev = $("#td"+row+"12").text().trim();
			if (prev){
				$("#td"+row+"12").text("");
				updateBottom(multiply(eval(prev),-1));
			}
			//notifyModal("Prezzo NON Valido", "Nessun prezzo inserito");
		}
	} catch (e){
		var row = getRow(target);
		notifyModal("Valore NON Valido", "Prezzo "+trimText+" non valido");
	}
}

function recalculateTotHandler(event){
	try{
		var target = event.target;
		var keyCode = event.keyCode;
		if (keyCode !== 190 && keyCode !== 188){ //DOT, COMMA, DELETE
			var row = getRow(target);
			var totField = $("#td"+row+"12");
			var firstPriceText = $(totField).text().trim();
			var prezzoText = $(target).text().trim().replace(",",".");
			if (prezzoText){
				//update total column
				var tot;
				if (isUnitario(target)){
					$("#td"+row+"11").text("");
					tot = calculateTotUnitario(row);
				} else {
					$("#td"+row+"10").text("");
					tot = calculateTotFlat(row);
				}
				$(totField).text(tot);
				//update fattura bottom
				//Sottrai vecchio totale, aggiungi nuovo e ricalcola
				var firstPriceVal;
				if (!firstPriceText){
					firstPriceVal=0;
				} else {
					firstPriceVal=eval(firstPriceText);
				}
				var diff = round(tot - firstPriceVal);
				updateBottom(diff);
			}
		}
	} catch (e){
		if (keyCode !== 8){			
			notifyModal("Valore NON Valido", "Prezzo "+$(event.target).text().trim()+" non valido");
		}
	}
}

function updateBottom(diff){
	var netto = $("#netto");
	var nettoText = $(netto).val();
	if (!nettoText){
		nettoText="0";
	}
	var newNetto = round(eval(nettoText)+diff);
	var ivaTot = round(multiply(newNetto, eval($("#iva").val())) / 100);
	var totale = round(newNetto + ivaTot);
	$(netto).val(newNetto);
	$("#ivaTot").val(ivaTot);
	$("#totale").val(totale);
}

function calculateTotFlat(row){
	return eval($("#td"+row+"11").text().trim().replace(",","."));
}

function calculateTotUnitario(row){
	var qta = eval($("#td"+row+"09").text().trim());
	var prezzo = eval($("#td"+row+"10").text().trim().replace(",","."));
	return multiply(qta, prezzo);
}

function isUnitario(target){
	var column = getColumn(target);
	var display;
	if (column === "10"){
		return true;
	} else if (column === "11"){
		return false;
	} else {
		throw "unrecognised field";
	}
}

function getRow(target){
	var targetId = $(target).prop("id");
	var indexOfColumn = targetId.length-2;
	return targetId.substring(2, indexOfColumn);
}

function getColumn(target){
	var targetId = $(target).prop("id");
	var indexOfColumn = targetId.length-2;
	return targetId.substring(indexOfColumn);
}

function recalculateDateHandler(){
	var emissione = $("#emissione").val();
	var giorni = $("#scadenzaGiorni").val();
	var targetUrl=getWebappUrl() + "/resources/utils/calculateScadenza?date="+encodeURIComponent(emissione)+"&giorni="+encodeURIComponent(giorni);
	doCall('GET', targetUrl, {}, "", function (responseData){
		$("#scadenza").val(responseData.items[0]);
	});
	
}

function fillPage(currentFattura){
	$("#realId").val(currentFattura.realId);
	$("#clienteId").val(currentFattura.cliente.id);
	$("#numero").val(currentFattura.id);
	$("#cliente").val(currentFattura.cliente.nome);
	$("#emissione").val(currentFattura.emissione);
	selectOption("scadenzaGiorni",currentFattura.scadenzaGiorni);
	fillTable(currentFattura.ddt, true);
	$("#capitot").val(currentFattura.capiTot);
	$("#netto").val(currentFattura.netto);
	$("#iva").val(currentFattura.ivaPerc);
	$("#ivaTot").val(currentFattura.iva);
	$("#totale").val(currentFattura.totale);
	$("#scadenza").val(currentFattura.scadenza);
}

function fillTable(ddtList, showPrices){
	var table = $("table tbody");
	$(table).empty();
	var row=0;
	for (var j in ddtList){
		var ddt = ddtList[j];
		var beni = ddt.beni;
		for (var i in beni){
			var bene = beni[i];
			bene.idDdt=ddt.id;
			bene.data=ddt.data;
			bene.destinazione=ddt.destinazione.replace(/"/g,"'");
			bene.row=row;
			if (!bene.prezzo || !showPrices){bene.prezzo=""}
			if (!bene.tot || !showPrices){bene.tot=""}
			row++;
			$(table).append(fatturaRowTemplate(bene));
		}
		$("body").append(cancellaDdtFatturaModalTemplate(ddt));
	}
}

function modificaFattura(){
	try {
		var fattura = caricaFattura();
		var targetUrl=getWebappUrl() + "/resources/fattura";
		doCall('PUT', targetUrl, {"Content-Type":"application/json"}, JSON.stringify(fattura), function (responseData){
			notifyModal("Successo", "Fattura salvata con successo");
			_.delay(function(){
				window.location.href=getWebappUrl()+"/fatturaList.xhtml";
			}, 2000);
		});
	} catch (e){
		notifyModal("Errore", e);
	}
}

function registraFattura(){
	try {
		var fattura = caricaFattura();
		var targetUrl=getWebappUrl() + "/resources/fattura";
		doCall('POST', targetUrl, {"Content-Type":"application/json"}, JSON.stringify(fattura), function (responseData){
			notifyModal("Successo", "Fattura registrata con successo");
			_.delay(function(){
				window.location.href=getWebappUrl()+"/fatturaList.xhtml";
			}, 2000);
		});
	} catch (e){
		notifyModal("Errore", e);
	}
}

function caricaFattura(){
	var fattura={};

	var clienteObj={};
	clienteObj.id=$("#clienteId").val();
	clienteObj.nome=$("#cliente").val();
	fattura.cliente=clienteObj;
	
	fattura.realId=$("#realId").val();
	fattura.emissione=$("#emissione").val();
	fattura.scadenza=$("#scadenza").val();
	fattura.id=$("#numero").val();
	fattura.ivaPerc=eval($("#iva").val());
	fattura.ddt=caricaTable();
	var netto = 0;
	for (var i in fattura.ddt){
		var ddt = fattura.ddt[i];
		for (var j in ddt.beni){
			netto += round(ddt.beni[j].tot);
			netto = round(netto);
		}
	}
	fattura.netto=round(netto);
	fattura.iva = round(multiply(netto,fattura.ivaPerc) / 100);
	fattura.totale=round(fattura.netto + fattura.iva);
	return fattura;
}

function caricaTable(){
	var ddtList = [];
	var rows = $("table tbody tr");
	if ($(rows).length===0){
		throw "Nessuna voce da fatturare trovata";
	}
	var tempDdt = {"beni":[]};
	$(rows).each(function(index){
		try {
			var tdList=$(this).find("td");
			var idRef = $(tdList[1]).text().trim();
			var number = eval(idRef.substring(0, idRef.indexOf(" ")));
			if (tempDdt.id && tempDdt.id!==number){
				ddtList.push(tempDdt);
				tempDdt = {"beni":[]};
			}
			tempDdt.id=number;
			tempDdt.data=idRef.substring(idRef.indexOf("del ")+4);
			var bene={};
			bene.id=$(tdList[14]).text().trim();
			var prezzoUnit = $(tdList[11]).text().trim();
			var prezzoFlat = $(tdList[12]).text().trim();
			var singleTot;
			if (prezzoUnit && !prezzoFlat){
				var qta = eval($(tdList[10]).text().trim());
				bene.prezzo = eval(prezzoUnit); 
				singleTot = multiply(qta,bene.prezzo);
			} else if (!prezzoUnit && prezzoFlat){
				singleTot = eval(prezzoFlat);
			} else if (!prezzoUnit && !prezzoFlat){
				throw "Nessun prezzo inserito";
			} else if (prezzoUnit && prezzoFlat){
				throw "Doppio prezzo inserito";
			} 
			bene.tot = singleTot;
			tempDdt.beni.push(bene);
		} catch (e){
			throw "Errore nel prezzo alla riga "+ (index+1)+": "+e;
		}
	});
	ddtList.push(tempDdt);
	return ddtList;
}

function deleteDdTFattura(ddtKey){
	$(".tr"+ddtKey).detach();
}