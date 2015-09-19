
var baseUrl = "http://69.243.204.126:91/com.northwind.rest/";
$(document)
		.ready(
				function() {
					getInventoryForRemoval();
					$(document.body)
							.on(
									'click',
									':button, .DELETE_BTN',
									function(e) {
										//console.log(this);
										var $this = $(this), PRODUCT_PK = $this.val(), obj = {PRODUCT_PK : PRODUCT_PK}, 
												$tr = $this.closest('tr'), 
												product = $tr.find('.CL_PRODUCT_NAME').text(), 
												ProductID = $tr.find('.CL_PRODUCT_ID').text();
										alert(product);
										deleteInventory(obj, product, ProductID);
									});
				});
function deleteInventory(obj, product, ProductID) {
	//alert(productName + "/n" + ProductID);
	ajaxObj = {
		type : "DELETE",
		url : baseUrl + "api/v1/products/delete/"
				+ product + "/" + ProductID,
		data : JSON.stringify(obj),
		contentType : "application/json",
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.responseText);
		},
		success : function(data) {
			// console.log(data);
			$('#delete_response').text(data[0].MSG);
		},
		complete : function(XMLHttpRequest) {
			// console.log( XMLHttpRequest.getAllResponseHeaders() );
			getInventoryForRemoval();
		},
		dataType : "json" // request JSON
	};
	return $.ajax(ajaxObj);
}
function getInventoryForRemoval() {
	var d = new Date(), n = d.getTime();
ajaxObj = {
		type : "GET",
		url : baseUrl + "api/v1/products/inventory",
		data : "ts=" + n,
		contentType : "application/json",
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.responseText);
		},
		success : function(data) {
			// console.log(data);
			var html_string = "";
			$.each(data, function(index1, val1) {
				//console.log(val1);
				html_string = html_string + templategetInventoryForRemoval(val1);
			});
			$('#get_inventory_for_removal').html(
					"<table border='1'>" + html_string + "</table>");
		},
		complete : function(XMLHttpRequest) {
			// console.log( XMLHttpRequest.getAllResponseHeaders() );
		},
		dataType : "json" // request JSON
	};
	return $.ajax(ajaxObj);
}
function templategetInventoryForRemoval(param) {
	return '<tr>'
			+ '<td class="CL_PRODUCT_NAME">'
			+ param.ProductName
			+ '</td>'
/*			+ '<td class="CL_PC_PARTS_CODE">'
			+ param.PC_PARTS_CODE
			+ '</td>'
			+ '<td class="CL_PC_PARTS_TITLE">'
			+ param.PC_PARTS_TITLE
			+ '</td>'
			+ '<td class="CL_PC_PARTS_AVAIL">'
			+ param.PC_PARTS_AVAIL
			+ '</td>'
			*/
			+ '<td class="CL_PRODUCT_ID">'
			+ param.ProductID
			+ '</td>'
			+ '<td class="CL_PC_PARTS_BTN"> <button class="DELETE_BTN" value=" '
			+ param.PRODUCTS_PK + ' " type="button">Delete</button> </td>'
			+ '</tr>';
}