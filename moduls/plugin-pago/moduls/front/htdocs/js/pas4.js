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
	
});