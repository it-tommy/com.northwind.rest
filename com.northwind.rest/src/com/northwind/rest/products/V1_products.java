package com.northwind.rest.products;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.northwind.dao.MySQLDB;
import com.northwind.dao.SchemaProducts;
import com.northwind.dto.Product;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

import com.northwind.util.*;


@Path("/v1/products")
public class V1_products {
	
	@Path("/delete/{productName}/{productID}")
	@DELETE
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteItem(@PathParam("productName") String productName,
							   @PathParam("productID") String productID,
							   String incomingData)throws Exception{
		
		int pk;
		int http_code;
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		SchemaProducts dao = new SchemaProducts();
		
		try{
			JSONObject partsData = new JSONObject(incomingData);
			//pk = partsData.optInt("ProductName", 0);
			//pk = partsData.optInt("PRODUCT_PK", 0);
			pk = Integer.parseInt(productID);
			//System.out.println("PK->" + pk );
			//System.out.println(incomingData);
			http_code = dao.deleteProducts(pk);
			if(http_code == 200){
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item was deleted succussfully");
			}else{
				jsonObject.put("HTTP_CODE", "500");
				jsonObject.put("MSG", "You do not have permission to delete this object");
				//return Response.status(500).entity("Server was not able to process the request").build();
			}
			returnString = jsonArray.put(jsonObject).toString();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Server was unable to process the request").build();
		}
		return Response.ok(returnString).build();
	}
	
	
	
	@Path("/update/{unit_price}/{item_id}")
	@PUT
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateItem(@PathParam("unit_price") double unitPrice, 
							   @PathParam("item_id") int item_id,
							   String incomingData) throws Exception{
		int pk;
		int avail;
		int http_code;
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		SchemaProducts dao = new SchemaProducts();
		
		try{
			//System.out.println("incomingData->" + incomingData);
			JSONObject partsData = new JSONObject(incomingData);
			//pk = partsData.optInt("ProductID", 0);
			pk = item_id;
			avail = partsData.optInt("UnitsInStock", 0);
			http_code = dao.updateProductUnitsInStock(pk, unitPrice);
			if(http_code == 200){
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item has been updated successfully");
			}else{
				return Response.status(500).entity("Server was unable to process your request").build();
			}
			returnString = jsonArray.put(jsonObject).toString();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		return Response.ok(returnString).build();
	}
	
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
			//System.out.println("Incoming Data: " + incomingData);
			
			ObjectMapper mapper = new ObjectMapper();
			Product productEntry = mapper.readValue(incomingData, Product.class);
			
			int http_code = dao.insertIntoProducts(productEntry.ProductName, 
												   productEntry.QuantityPerUnit, 
												   productEntry.UnitPrice, 
												   productEntry.UnitsInStock, 
												   productEntry.UnitsOnOrder, 
												   productEntry.ReorderLevel, 
												   productEntry.Discontinued);
			if(http_code == 200){
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item has been persisted successfully, version 1");
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
	
	
	@Path("/inventory")
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public Response returnAllProuducts() throws Exception{

		Response rb = null;
		String returnString = null;
		
		try{
			SchemaProducts dao = new SchemaProducts();
			returnString = dao.returnAllProudcts().toString();
			rb = Response.ok(returnString).build();
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("The server was unable to proccess the request.").build();
		}
		
		return rb;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnProductByQueryParam(@QueryParam("product_id") String product_id) throws Exception{
		
		Response rb = null;
		String returnString = null;
		
		try{
			if(product_id == null || Integer.parseInt(product_id) < 0){
				return Response.status(400).entity("Error Code 400 - Bad Request...Unable to retrieve product. Please specify a valid product id").build();
			}
			SchemaProducts dao = new SchemaProducts();
			returnString = dao.returnProductByQueryParam(product_id).toString();
			if(returnString.length() > 3){
				rb = Response.ok(returnString).build();
			}else{
				return Response.status(400).entity("Error Code 400 - Bad Request...Product with product_id=" + product_id + " does not exist").build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		return rb;
		
	}
	
	@Path("/{product_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnProductByPathParam(@PathParam("product_id") int product_id) throws Exception{
		
		Response rb = null;
		String returnString = null;
		
		try{
			if(product_id < 0){
				return Response.status(400).entity("Error Code 400 - Bad Request...Unable to retrieve product. Please specify a valid product id").build();
			}
			SchemaProducts dao = new SchemaProducts();
			returnString = dao.returnProductByPathParam(product_id).toString();
			if(returnString.length() > 3){
				rb = Response.ok(returnString).build();
			}else{
				return Response.status(400).entity("Error Code 400 - Bad Request...Product with product_id=" + product_id + " does not exist").build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		return rb;

	}
	
	
	
	
	
	
	
	
	
	
	
}
