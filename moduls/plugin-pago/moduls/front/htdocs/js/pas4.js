// Paso 4

$(document).ready(function() {
	// iconografia
	$("#iconografiaMasInfo").bind("click", function(e){
		var display = $("#iconografia ul span").css("display");
		if (display == 'none') {
			$("#iconografia ul span").fadeIn("slow");
			$("#iconografia ul").addClass("masInfo");
		} else {
			$("#iconografia ul span").fadeOut("slow", function () {
        $("#iconografia ul").removeClass();
      });
		}
	});
	// instrucciones pago
	$("#pagoBEA h3 a").click(function () {
		$(this).parent().parent().find(".instrucciones").slideToggle("slow");
	});
	$("#pagoFPA h3 a").click(function () {
		$(this).parent().parent().find(".instrucciones").slideToggle("slow");
	});
	$("#pagoInstrucciones h3 a").click(function () {
		$(this).parent().parent().find(".instrucciones").slideToggle("slow");
	});
	//seleccion pago
	$("#seleccionDatos a#P").click(function() {
		$("#banca").hide();
		$("#tarjeta").hide();
		$("#presencial").show();
	});
	$("#seleccionDatos a#T").click(function() {
		$("#banca").hide();
		$("#presencial").hide();
		$("#errorsForm").hide();
		$("#tarjeta").show();
	});
	$("#seleccionDatos a#B").click(function() {
		$("#presencial").hide();
		$("#tarjeta").hide();
		$("#banca").show();
	});
	$("#fPresencial").submit(function() {
		$("#banca").hide();
		$("#tarjeta").hide();
		$("#presencial").show();
	});
	//seleccion de banco
	$("#banca a").click(function() {
	    var value = $(this).attr( 'href' );
	    if (value == 'DS') {
	    	pagoDesactivado();
	    } else {
	    	accediendoEnviando(mensajeEnviando + value);
	    	$("#banco").val(value);
	    	$("#pagoBancaForm").submit();
	    }
	    return false;
	});
	
});