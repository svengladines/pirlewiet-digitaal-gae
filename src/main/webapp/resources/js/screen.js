function selectScreen(screen){

	var dataObject = { "screen": screen.value};
	
	$.ajax({
		type : "POST",
		cache : false,
		url : "selectScreen",
		data : dataObject,
		success : function(response) {
			console.log("Succes back from server");
			//location.reload();
		}
	});
}

function activeerDeactiveerDienst(id){
	var dataObject = {"id": id };
	
	var isChecked =$("#checkbox_"+id).attr("checked");
	console.log(id + " " + isChecked);
	var url = "deactiveerDienst";
	if(isChecked == "checked"){
		url = "activeerDienst";
	}
	
	
	
	$.ajax({
		type : "POST",
		cache : false,
		url : url,
		data : dataObject,
		success : function(response) {
			console.log("Succes back from server");
			console.log(response.msg);

		}
	});
	
}


function getJSPPage(){
	$.ajax({                                       
		  url : 'getJSP',
		  type : 'GET',
		  async: true,
		  data : {},
		  success : function(data) {  
		    //data shoud be rendered jsp with model from the controller  
			  
			  console.log(data);
			  
			  $("#vakantie").html(data);
		  },
		  error: function (jqXHR, textStatus, errorThrown) {
		    alert(jqXHR + " : " + textStatus + " : " + errorThrown); 
		  }
	})
}



