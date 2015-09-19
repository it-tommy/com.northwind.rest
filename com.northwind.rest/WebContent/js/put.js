
var baseUrl = "http://69.243.204.126:91/com.northwind.rest/";
$(document)
		.ready(
				function() {
					var $updateRecord = $('#updateRecord'), $PRODUCT_ID = $('#PRODUCT_ID');
					//var $updateRecord = $('#updateRecord');
					//alert($PRODUCT_ID.text);
					getInventory();
					$(document.body).on('click',':button, .UPDATE_BTN',
									function(e) {
										 //console.log(this);
										var $this = $(this), PRODUCT_PK = $this.val(), 
											$tr = $this.closest('tr'), 
											ProductID = $tr.find('.CL_PRODUCT_ID').text(),
											ProductName = $tr.find('.CL_PRODUCT_NAME').text(), 
											QuantityPerUnit = $tr.find('.CL_QUANTITY_PER_UNIT').text(), 
											UnitPrice = $tr.find('.CL_UNIT_PRICE').text(), 
											UnitsInStock = $tr.find('.CL_UNITS_IN_STOCK').text(),
											UnitsOnOrder = $tr.find('.CL_UNITS_ON_ORDER').text(),
											ReorderLevel = $tr.find('.CL_REORDER_LEVEL').text(),
											Discontinued = $tr.find('.CL_DISCONTINUED').text();
										
										//$('#SET_PRODUCTS_PK').val(PRODUCT_PK);

										$('#SET_PRODUCT_ID').text(ProductID);
										$('#SET_PRODUCT_NAME').text(ProductName);
										$('#SET_QUANTITY_PER_UNIT').text(QuantityPerUnit);
										$('#SET_UNIT_PRICE').val(UnitPrice);
										$('#SET_UNITS_IN_STOCK').text(UnitsInStock);
										$('#SET_UNITS_ON_ORDER').text(UnitsOnOrder);
										$('#SET_REORDER_LEVEL').text(ReorderLevel);
										$('#SET_DISCONTINUED').text(Discontinued);
										$('#update_response').text("");

									});
					//alert(ProductName.text);
					$updateRecord
							.submit(function(e) {
								e.preventDefault(); // cancel form submit
								var obj = $updateRecord.serializeObject(),
													unit_price = $('#SET_UNIT_PRICE').val(), 
													item_id = $('#SET_PRODUCT_ID').text();
														updateInventory(obj, unit_price, item_id);
							});
				});
function updateInventory(obj, unit_price, item_id) {
	ajaxObj = {
		type : "PUT",
		url : baseUrl + "api/v1/products/update/" + unit_price + "/" + item_id,
		data : JSON.stringify(obj),
		contentType : "application/json",
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.responseText);
		},
		success : function(data) {
			// console.log(data);
			$('#update_response').text(data[0].MSG);
		},
		complete : function(XMLHttpRequest) {
			// console.log( XMLHttpRequest.getAllResponseHeaders() );
			getInventory();
		},
		dataType : "json" // request JSON
	};
	return $.ajax(ajaxObj);
}
function getInventory() {
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
			 //console.log(data);
			var html_string = "";
			$.each(data, function(index1, val1) {
				//console.log(val1);
				html_string = html_string + templateGetInventory(val1);
			});
			$('#get_inventory').html(
					"<table border='1'>" + html_string + "</table>");
		},
		complete : function(XMLHttpRequest) {
			// console.log( XMLHttpRequest.getAllResponseHeaders() );
		},
		dataType : "json" // request JSON
	};
	return $.ajax(ajaxObj);
}
function templateGetInventory(param) {
	return '<tr>'
			+ '<td class="CL_PRODUCT_ID">'
			+ param.ProductID
			+ '</td>'
			+ '<td class="CL_PRODUCT_NAME">'
			+ param.ProductName
			+ '</td>'
			+ '<td class="CL_QUANTITY_PER_UNIT">'
			+ param.QuantityPerUnit
			+ '</td>'
			+ '<td class="CL_UNIT_PRICE">'
			+ param.UnitPrice
			+ '</td>'
			+ '<td class="CL_UNITS_IN_STOCK">'
			+ param.UnitsInStock
			+ '</td>'
			+ '<td class="CL_UNITS_ON_ORDER">'
			+ param.UnitsOnOrder
			+ '</td>'
			+ '<td class="CL_REORDER_LEVEL">'
			+ param.ReorderLevel
			+ '</td>'
			+ '<td class="CL_DISCONTINUED">'
			+ param.Discontinued
			+ '</td>'
			+ '<td class="CL_PC_PARTS_BTN"> <button class="UPDATE_BTN" value=" '
			+ param.PC_PARTS_PK + ' " type="button">Update</button> </td>'
			+ '</tr>';
}