//On load
$(function() {
	//validation on change
    $('#discontinued').change($.fn.dateOrder);
    $('#introduced').change($.fn.dateOrder);
});

/**
 * 
 */
(function ( $ ) {

    $.fn.dateOrder = function() {
    	console.log("bob");
    	var introduced = $('#introduced')[0];
    	var discontinued = $('#discontinued')[0];
    	var introVal = introduced.value;
    	var discoVal = discontinued.value;
        var introDate = new Date(introVal);
        var discoDate = new Date(discoVal);
        if(introDate > discoDate){
        	introduced.setCustomValidity("introduction date must be anterior to discontinuation date");
        	discontinued.setCustomValidity("introduction date must be anterior to discontinuation date");
        	this.reportValidity();
        } else {
        	introduced.setCustomValidity("");
        	discontinued.setCustomValidity("");
        }
    };

}( jQuery ));