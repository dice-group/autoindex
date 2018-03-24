$("#searchButtonLabel").click(function(){
	var label = $("#queryText").val();
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(label),
		 headers:{
                "Accept": "application/json; charset=utf-8",
                "Content-Type": "application/json; charset=utf-8"
         },
         url: "/searchByLabel",
		timeout : 100000,
		
	});
});
$("#searchButtonURL").click(function(){
	var url = encodeURIComponent($("#urlQuery").val());
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(url),
		 headers:{
                "Accept": "application/json; charset=utf-8",
                "Content-Type": "application/json; charset=utf-8"
         },
         url: "/searchByURL",
		timeout : 100000,
		
	});
});