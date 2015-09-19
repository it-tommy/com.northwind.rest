package com.northwind.rest.products;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.northwind.dao.SchemaProducts;


@Path("/v2/products")
public class V2_products {
	
	
	@Path("/add")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response addProductItem(String incomingData) throws Exception{
		
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		SchemaProducts dao = new SchemaProducts();
		
		try{
			JSONObject partsData = new JSONObject(incomingData);
			//System.out.println("Incoming Data: " + incomingData);
			int http_code = dao.insertIntoProductsJSONArray(partsData.optString("ProductName"), 
							partsData.optString("QuantityPerUnit"), 
							partsData.optString("UnitPrice"), 
							partsData.optString("UnitsInStock"), 
							partsData.optString("UnitsOnOrder"), 
							partsData.optString("ReorderLevel"), 
							partsData.optString("Discontinued"));
			if(http_code == 200){
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item has been persisted successfully, version 2");
				returnString = jsonArray.put(jsonObject).toString();
			}else{
				return Response.status(500).entity("Unable to insert item").build();
			}
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Server unable to process the request").build();
		}
		return Response.ok(returnString).build();
	}

}
