$("#searchButtonLabel").click(function(){
	var label = $("#queryText").val();
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(label),
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
         url: "/searchByURL",
		timeout : 100000,
		
	});
});