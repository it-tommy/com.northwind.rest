package com.northwind.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.codehaus.jettison.json.*;

import com.northwind.util.*;

public class SchemaProducts extends MySQLDB {

	public int insertIntoProducts(String ProductName, String QuantityPerUnit,
			double UnitPrice, int UnitsInStock, int UnitsOnOrder,
			int ReorderLevel, byte Discontinued) throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn
					.prepareStatement("insert into Products "
							+ "(ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, UnitsOnOrder, ReorderLevel, Discontinued ) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?) ");
			query.setString(1, ProductName);
			query.setString(2, QuantityPerUnit);
			query.setDouble(3, UnitPrice);
			query.setInt(4, UnitsInStock);
			query.setInt(5, UnitsOnOrder);
			query.setInt(6, ReorderLevel);
			query.setByte(7, Discontinued);
			query.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			return 500;

		} finally {
			if (conn != null)
				conn.close();
		}
		return 200;

	}

	public JSONArray returnAllProudcts() throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		JSONArray jsonArray = new JSONArray();

		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn
					.prepareStatement(" select ProductID, ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, "
							+ "UnitsOnOrder, ReorderLevel, Discontinued from products");
			ResultSet rs = query.executeQuery();
			ToJSON converter = new ToJSON();
			jsonArray = converter.toJSONArray(rs);
			query.close();
		} catch (SQLException sqlError) {
			sqlError.printStackTrace();
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return jsonArray;
		} finally {
			if (conn != null)
				conn.close();
		}
		return jsonArray;
	}

	public Object returnProductByQueryParam(String product_id) throws Exception {
		Connection conn = null;
		PreparedStatement query = null;

		ToJSON converter = new ToJSON();
		JSONArray jsonArray = new JSONArray();

		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn
					.prepareStatement(" select ProductID, ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, "
							+ "UnitsOnOrder, ReorderLevel, Discontinued from products "
							+ "where ProductID = ? ");
			query.setInt(1, Integer.parseInt(product_id));
			ResultSet rs = query.executeQuery();

			jsonArray = converter.toJSONArray(rs);
			query.close();
		} catch (SQLException sqlError) {
			sqlError.printStackTrace();
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return jsonArray;
		} finally {
			if (conn != null)
				conn.close();
		}
		return jsonArray;
	}

	public Object returnProductByPathParam(int product_id) throws Exception {
		Connection conn = null;
		PreparedStatement query = null;
		ToJSON converter = new ToJSON();
		JSONArray jsonArray = new JSONArray();

		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn
					.prepareStatement(" select ProductName, ProductID, QuantityPerUnit, UnitPrice, UnitsInStock, "
							+ "UnitsOnOrder, ReorderLevel, Discontinued from products "
							+ "where ProductID = ? ");
			query.setInt(1, product_id);
			ResultSet rs = query.executeQuery();
			jsonArray = converter.toJSONArray(rs);
			query.close();

		} catch (SQLException sqlError) {
			sqlError.printStackTrace();
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return jsonArray;
		} finally {
			if (conn != null)
				conn.close();
		}
		return jsonArray;
	}

	public JSONArray queryReturnBrandParts(String brand) throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();

		try {
			conn = mySQlPCPartsConnection();
			query = conn
					.prepareStatement("Select PC_PARTS_PK, PC_PARTS_TITLE, PC_PARTS_CODE, PC_PARTS_MAKER, "
							+ "PC_PARTS_AVAIL, PC_PARTS_DESC from PC_PARTS "
							+ "where UPPER(PC_PARTS_MAKER) = ? ");
			query.setString(1, brand.toUpperCase());
			ResultSet rs = query.executeQuery();

			json = converter.toJSONArray(rs);
			query.close();
		} catch (SQLException sqlError) {
			sqlError.printStackTrace();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return json;
		} finally {
			if (conn != null)
				conn.close();
		}

		return json;
	}

	public JSONArray queryReturnBrandItemNumber(String brand, int item_number)
			throws Exception {
		PreparedStatement query = null;
		Connection conn = null;

		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();

		try {
			conn = mySQlPCPartsConnection();
			query = conn
					.prepareStatement("Select PC_PARTS_PK, PC_PARTS_TITLE, PC_PARTS_CODE, PC_PARTS_MAKER, "
							+ "PC_PARTS_AVAIL, PC_PARTS_DESC from PC_PARTS "
							+ "where UPPER(PC_PARTS_MAKER) = ? and PC_PARTS_CODE = ? ");
			query.setString(1, brand.toUpperCase());
			query.setInt(2, item_number);
			ResultSet rs = query.executeQuery();

			json = converter.toJSONArray(rs);
			query.close();
		} catch (SQLException sqlError) {
			sqlError.printStackTrace();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return json;
		} finally {
			if (conn != null)
				conn.close();
		}

		return json;
	}

	public int updateProductUnitsInStock(int pk, double unitPrice)
			throws Exception {
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn.prepareStatement(" update products "
					+ "set UnitPrice = ? " + "where ProductID = ?");

			query.setDouble(1, unitPrice);
			query.setInt(2, pk);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		} finally {
			if (conn != null)
				conn.close();
		}
		return 200;
	}

	public int deleteProducts(int pk) throws Exception {
		PreparedStatement query = null;
		Connection conn = null;
		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn.prepareStatement("delete from products "
					+ "where ProductID = ? AND ProductID > 77");
			query.setInt(1, pk);

			if (query.executeUpdate() == 0)
				return 500;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		} finally {
			if (conn != null)
				conn.close();
		}
		return 200;
	}

	public int insertIntoProductsJSONArray(String ProductName, String QuantityPerUnit,
			String UnitPrice, String UnitsInStock, String UnitsOnOrder,
			String ReorderLevel, String Discontinued) throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		try {
			conn = MySQLDB.MySQLDBConn().getConnection();
			query = conn
					.prepareStatement("insert into Products "
							+ "(ProductName, QuantityPerUnit, UnitPrice, UnitsInStock, UnitsOnOrder, ReorderLevel, Discontinued ) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?) ");
			query.setString(1, ProductName);
			query.setString(2, QuantityPerUnit);
			query.setDouble(3, Double.parseDouble(UnitPrice));
			query.setInt(4, Integer.parseInt(UnitsInStock));
			query.setInt(5, Integer.parseInt(UnitsOnOrder));
			query.setInt(6, Integer.parseInt(ReorderLevel));
			query.setByte(7, Byte.parseByte(Discontinued));
			query.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			return 500;

		} finally {
			if (conn != null)
				conn.close();
		}
		return 200;

	}

}
