$("#searchButtonLabel").click(function(){
	var label = $("#queryText").val();
	var data = {};
	data["query"] = label;
	data["type"] = $("input[name=options]:checked").val();
	data["category"] = $("#sel1").val()
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(data),
		url: "/search",
		timeout: 100000,
		contentType: "application/json",
		async: true,	
	})
	.done(function(data){
		renderTable(data);
	});
});
function renderTable(data){
	var obj = JSON.parse(data);
	if(obj.boolean == false){
		alert("Search Failed");
	}
	else{
		
	var tableData = "<table align=\"center\"><tr><th>LABEL</th><th>URL</th></tr>";	
	for (var i = 0; i < obj.results.bindings.length; i++) {
	    var counter = obj.results.bindings[i];
	    var temp = "<tr>";
	    temp = "<td>" + counter.label.value + "</td>";
	    temp += "<td>" + counter.uri.value + "</td>";
	    temp += "</tr>";
	    tableData += temp;
	}
	tableData += "</table><h1 align=\"center\">End of Results</h1>";
	$("#results").html(tableData);
	}
}
$("input[name=options]").change(function () {
	if(this.value == "uri"){
		$("#queryText").attr("placeholder", "Enter Query URL");
	}
	else{
		$("#queryText").attr("placeholder", "Enter Query Label");
	}
    
});