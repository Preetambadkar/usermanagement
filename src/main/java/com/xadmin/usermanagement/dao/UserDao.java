package com.xadmin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xadmin.usermanagement.bean.User;

public class UserDao {
	private String jdbcUrl = "jdbc:mysql://localhost:3306/userdb?useSSl=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	private String jdbcDriver = "com.mysql.jdbc.Driver";

	private static final String INSERT_USER_SQL =  "INSERT INTO user (name, email, country) VALUES (?, ?, ?);";
	private static final String SELECT_USER_BYID = "select id,name,email,country from user  where id=?";
	private static final String DELETE_USER_SQL = "delete from user where id=?;";
	private static final String UPDATE_USER_SQL = "update user set name= ?, email=? ,country=? where id=?;";
	private static final String SELECT_ALL_USER = "select*from user";

	public UserDao() {

	}

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}

	// insert user
	public void InsertUser(User user) throws SQLException {
		System.out.println(INSERT_USER_SQL);
		try (Connection connection = getConnection();
				PreparedStatement preparedstatement = connection.prepareStatement(INSERT_USER_SQL);) {
			preparedstatement.setString(1, user.getName());
			preparedstatement.setString(2, user.getEmail());
			preparedstatement.setString(3, user.getCountry());
			System.out.println(preparedstatement);
			preparedstatement.executeUpdate();
		} catch (SQLException e) {
			// Print detailed information about the SQL exception
			printSQLException(e);
		}
	}

	// Select user by id
	public User SelectUser(int id) {
		User user = null;
		// Step1:Establishing the connection
		try (
			Connection connection = getConnection();
			// Step2:Create a statement using connection object
			PreparedStatement preparedstatement = connection.prepareStatement(SELECT_USER_BYID);){
			preparedstatement.setInt(1, id);
			System.out.println(preparedstatement);
			// step3:Execute the query or update query
			ResultSet rs = preparedstatement.executeQuery();
			// step4:Process the resultset Object.
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);

			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;

	}
// Select All  User 
	public List <User> selectAllUsers(){
		//	using try-with resources TO avoid closing resource(boiler plate code)
		List<User> users=new ArrayList<>();
		//Step 1: Establishing a Connection
		try(
			Connection connection = getConnection();
			// Step2:Create a statement using connection object
			PreparedStatement preparedstatement = connection.prepareStatement(SELECT_ALL_USER);){
			System.out.println(preparedstatement);
			//step3:Execute the query or update query
			ResultSet rs= preparedstatement.executeQuery();
			//Step4: Process the result set object
			while(rs.next()) {
				int id=rs.getInt("id");
				String name=rs.getString("name");
				String email=rs.getString("email");
				String country=rs.getString("country");
				users.add(new User(id,name,email,country));
				
			}
					
			}catch (SQLException e) {
				printSQLException(e);
			
		}
		return users;
				
	}
	
	  
	//Update the User
	
	public boolean updateUser(User user)throws SQLException{
		boolean rowUpdate;
		//step 1:Establishing a Connection
		try(
			Connection connection = getConnection();
			
			//step2:Create a statement using connection object
			PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL);){
			System.out.println("Upadate User"+statement);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
  			statement.setInt(4, user.getId());  // Ensure the id is set as the fourth parameter
			
			rowUpdate = statement.executeUpdate()>0;
	
		}
		
		return rowUpdate;
	}
	
	
	//delete User
	public boolean deleteUser(int id)throws SQLException{
		boolean rowDelete;
		//step 1:Establishing a Connection
		try(
			Connection connection = getConnection();
			
			//step2:Create a statement using connection object
			PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL);){
		    statement.setInt(1, id);
		    rowDelete=statement.executeUpdate()>0;
		    
		
		}
		return rowDelete;
		
		
	}
	
	private void printSQLException(SQLException ex) {
		// TODO Auto-generated method stub
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState:" + ((SQLException) e).getSQLState());
				System.err.println("Error Code:" + ((SQLException) e).getErrorCode());
				System.err.println("Message:" + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause:" + t);
					t = t.getCause();
				}
			}

		}
	}

}
