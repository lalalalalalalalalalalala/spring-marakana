package com.marakana.contacts.repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.marakana.contacts.entities.Address;

public class AddressRepository {

	private final DataSource ds;

	public AddressRepository() {

		try {
			Context context = new InitialContext();

			try {
				ds = (DataSource) context
						.lookup("java:comp/env/jdbc/trainingdb");
			} finally {
				context.close();
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public void init() throws SQLException {
		Connection connection = ds.getConnection();
		try {
			Statement statement = connection.createStatement();

			try {
				statement
						.execute("create table address (id integer generated by default as identity primary key, street varchar(255), city varchar(255), state varchar(255), zip varchar(255))");

			} finally {
				statement.close();
			}

		} finally {
			connection.close();
		}
	}

	public Address find(long id) throws SQLException {
		Connection connection = ds.getConnection();
		try {
			Statement statement = connection.createStatement();
			try {
				ResultSet results = statement
						.executeQuery("select * from address whereid = " + id);

				try {

					if (!results.next()) {

						return null;
					} else {
						return unmarshal(results);
					}

				} finally {
					results.close();
				}

			} finally {
				statement.close();
			}

		} finally {
			connection.close();
		}
	}

	private Address unmarshal(ResultSet results) throws SQLException {
		Address address = new Address();
		address.setId(results.getLong("id"));
		address.setStreet(results.getString("street"));
		address.setCity(results.getString("city"));
		address.setState(results.getString("state"));
		address.setZip(results.getString("zip"));

		return address;

	}

	public void create(Address address) throws SQLException {
		Connection connection = ds.getConnection();
		try {
			Statement statement = connection.createStatement();
			try {

				statement.executeUpdate(
						"insert into address (street, city, state, zip) values ('"
								+ address.getStreet() + "', '"
								+ address.getCity() + "', '"
								+ address.getState() + "', '"
								+ address.getZip() + "')",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet generatedKeys = statement.getGeneratedKeys();

				try {
					if (generatedKeys.next())
						address.setId(generatedKeys.getLong("id"));
				} finally {

					generatedKeys.close();
				}

			} finally {
				statement.close();
			}

		} finally {
			connection.close();
		}
	}

	public void update(Address address) throws SQLException {
		Connection connection = ds.getConnection();
		try {
			Statement statement = connection.createStatement();
			try {

				statement.executeUpdate(
						"update address set street='" + address.getStreet() 
						+ "', city='" + address.getCity() 
						+ "', state='" + address.getState() 
						+ "', zip='" + address.getZip() 
						+ "' where id=" + address.getId());

			} finally {
				statement.close();
			}

		} finally {
			connection.close();
		}		

	}

	public void delete(Address address) throws SQLException {
		Connection connection = ds.getConnection();
		try {
			Statement statement = connection.createStatement();
			try {
				statement.executeUpdate(
						"delete from addresss where id=" + address.getId());
			} finally {
				statement.close();
			}

		} finally {
			connection.close();
		}	
	}

}
