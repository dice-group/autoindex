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
$("input[name=options]").change(function () {
	if(this.value == "uri"){
		$("#queryText").attr("placeholder", "Enter Query URL");
	}
	else{
		$("#queryText").attr("placeholder", "Enter Query Label");
	}
    
});