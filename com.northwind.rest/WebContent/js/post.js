
var baseUrl = "http://69.243.204.126:91/com.northwind.rest/";
$(document)
		.ready(
				function() {
					// console.log("ready");
					var $insertProduct = $('#insertProduct');
					/**
					 * This is for the Submit button It will trigger a ajax POST
					 * call to: api/v2/inventory This will submit a item entry
					 * to our inventory database
					 */
					$('#submit_it')
							.click(
									function(e) {
										// console.log("submit button has been
										// clicked");
										e.preventDefault(); // cancel form
															// submit
										var jsObj = $insertProduct
												.serializeObject(), ajaxObj = {};
										// console.log(jsObj);
										ajaxObj = {
											type : "POST",
											url : baseUrl + "api/v1/products/add/",
											data : JSON.stringify(jsObj),
											contentType : "application/json",
											error : function(jqXHR, textStatus,
													errorThrown) {
												console
														.log("Error "
																+ jqXHR
																		.getAllResponseHeaders()
																+ " "
																+ errorThrown);
											},
											success : function(data) {
												// console.log(data);
												if (data[0].HTTP_CODE == 200) {
													$('#div_ajaxResponse')
															.text(data[0].MSG);
												}
											},
											complete : function(XMLHttpRequest) {
												// console.log(
												// XMLHttpRequest.getAllResponseHeaders()
												// );
											},
											dataType : "json" // request JSON
										};
										$.ajax(ajaxObj);
									});
					/**
					 * This is for the 2nd Submit button "Submit v2" It will do
					 * the same thing as Submit above but the api will process
					 * it in a different way.
					 */
					$('#submit_it2')
							.click(
									function(e) {
										// console.log("submit button has been
										// clicked");
										e.preventDefault(); // cancel form
															// submit
										var jsObj = $insertProduct
												.serializeObject(), ajaxObj = {};
											// console.log(jsObj);
											ajaxObj = {
											type : "POST",
											url : "api/v2/products/add/",
											data : JSON.stringify(jsObj),
											contentType : "application/json",
											error : function(jqXHR, textStatus,
													errorThrown) {
												console
														.log("Error "
																+ jqXHR
																		.getAllResponseHeaders()
																+ " "
																+ errorThrown);
											},
											success : function(data) {
												// console.log(data);
												if (data[0].HTTP_CODE == 200) {
													$('#div_ajaxResponse')
															.text(data[0].MSG);
												}
											},
											complete : function(XMLHttpRequest) {
												// console.log(
												// XMLHttpRequest.getAllResponseHeaders()
												// );
											},
											dataType : "json" // request JSON
										};
										$.ajax(ajaxObj);
									});
				});