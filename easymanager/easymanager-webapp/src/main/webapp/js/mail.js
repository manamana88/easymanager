$( document ).ready(function() {

	$("#destinatario").change(filterAttachments);
	
	var targetUrl=getWebappUrl() + "/resources/account/all";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var accounts = responseData.items;
		var accountsSelect = $("#account");
		for (var i in accounts){
			var account = accounts[i];
			$(accountsSelect).append("<option value=\""+account.id+"\" >"+account.username+"</option>");
		}
	});
	
	var targetUrl=getWebappUrl() + "/resources/azienda/all";
	doCall('GET', targetUrl, {}, "", function (responseData){
		var aziende = responseData.items;
		var destinatarioSelect = $("#destinatario");
		for (var i in aziende){
			var azienda = aziende[i];
			$(destinatarioSelect).append("<option value=\""+azienda.id+"\" >"+azienda.nome+"</option>");
		}
		var companyId = getParameterByName("company");
		if (companyId){
			selectOption("destinatario", companyId);
			$("#destinatario").triggerHandler("change");
		}
	});
	
});

function invia(){
	var targetUrl=getWebappUrl() + "/resources/mail";
	var data="account=" + encodeURIComponent($("#account").val()) +
			"&company=" + encodeURIComponent($("#destinatario").val()) +
			"&recipients=" + encodeURIComponent($("#altri").val()) +
			"&attachment=" + encodeURIComponent($("#allega").val()) +
			"&object=" + encodeURIComponent($("#oggetto").val()) +
			"&text=" + encodeURIComponent($("#testo").val());
	doCall('POST', targetUrl, {}, data, function (responseData){
		notifyModal("Successo", "Email inviata con successo");
		_.delay(function(){
			window.location.href=getWebappUrl();
		}, 2000);
	});
	
}

function filterAttachments(){
	var destinatario = $("#destinatario").val();
	if (destinatario) {
		var targetUrl=getWebappUrl() + "/resources/fattura/all?company="+encodeURIComponent(destinatario);
		doCall('GET', targetUrl, {}, "", function (responseData){
			var fatture = responseData.items;
			var allegaSelect = $("#allega");
			$(allegaSelect).empty();
			$(allegaSelect).append("<option >-</option>");
			for (var i in fatture){
				var fattura = fatture[i];
				$(allegaSelect).append("<option value=\"F"+fattura.realId+"\" >Fattura "+fattura.id+" del "+fattura.emissione+"</option>");
			}
			var targetUrl=getWebappUrl() + "/resources/ddt/all?company="+encodeURIComponent(destinatario);
			doCall('GET', targetUrl, {}, "", function (responseData){
				var ddtList = responseData.items;
				for (var i in ddtList){
					var ddt = ddtList[i];
					$(allegaSelect).append("<option value=\"D"+ddt.realId+"\" >Ddt "+ddt.id+" del "+ddt.data+"</option>");
				}
				var ddtId = getParameterByName("ddt");
				var fatturaId = getParameterByName("fattura");
				if (fatturaId){
					selectOption("allega", "F"+fatturaId);
				} else if (ddtId){
					selectOption("allega", "D"+ddtId);
				}
			});
		});
	}
	
}
