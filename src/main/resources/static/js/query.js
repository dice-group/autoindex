$("#searchButtonLabel").click(function(){
	var label = $("#queryText").val();
	console.log(label);
	$.ajax({
		type : "POST",
		dataType: 'text',
		data: JSON.stringify(label),
		 headers: {
                'Accept': 'application/json; charset=utf-8',
                'Content-Type': 'application/json; charset=utf-8'
         },
         url: '/searchByLabel',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
		},
        error : function(e) {
				console.log("ERROR: ", e);
        }
	});
});
$("#searchButtonURL").click(function(){
	var url = encodeURIComponent($("#urlQuery").val());
	console.log(url);
	$.ajax({
		type : "POST",
		dataType: 'text',
		data: JSON.stringify(url),
		 headers: {
                'Accept': 'application/json; charset=utf-8',
                'Content-Type': 'application/json; charset=utf-8'
         },
         url: '/searchByURL',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
		},
        error : function(e) {
				console.log("ERROR: ", e);
        }
	});
});