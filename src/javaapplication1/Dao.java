package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE tboyne_ticketsV2(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), gender VARCHAR(30), ticket_description VARCHAR(200), ticket_priority VARCHAR(10) )";
		final String createUsersTable = "CREATE TABLE tboyne_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into tboyne_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketerGender, String ticketDesc, String ticketPriority) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("Insert into tboyne_ticketsV2" + "(ticket_issuer, gender, ticket_description, ticket_priority) values(" + " '"
					+ ticketName + "','" + ticketerGender + "','" + ticketDesc + "','" + ticketPriority + "')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}
  
  //readRecords method to view tickets
	public ResultSet readRecords(boolean isAdmin, String username) {

		ResultSet results = null;
		try {

      if(isAdmin) {
        statement = connect.createStatement();
        results = statement.executeQuery("SELECT * FROM tboyne_ticketsV2");
      } 

      else {
        statement = connect.createStatement();
        results = statement.executeQuery("SELECT * FROM tboyne_ticketsV2 WHERE ticket_issuer = '" + username + "'");
      }
			
		} 
    
    catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}

  //updateRecords method to update ticket description
  public void updateRecords(int ticketID, String newDesc, boolean isAdmin) {

    String sqlForPstmtDesc = "UPDATE tboyne_ticketsV2 SET ticket_description = ? WHERE ticket_id = ?";

    try {
      Connection conn = getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sqlForPstmtDesc);
      pstmt.setString(1, newDesc);
      pstmt.setInt(2, ticketID);
      pstmt.executeUpdate();
    }

    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  //updatePriority method to update ticket description
  public void updatePriority(int ticketID, String newPrio, boolean isAdmin) {

    String sqlForPstmtPrio = "UPDATE tboyne_ticketsV2 SET ticket_priority = ? WHERE ticket_id = ?";
    
    try {
      Connection conn = getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sqlForPstmtPrio);
      pstmt.setString(1, newPrio);
      pstmt.setInt(2, ticketID);
      pstmt.executeUpdate();
    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  //deleteRecords method to delete ticket
  public void deleteRecords (int ticketID, boolean isAdmin) {
    String sqlForPstmtDel = "DELETE FROM tboyne_ticketsV2 WHERE ticket_id = ?";
    try {
      Connection conn = getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sqlForPstmtDel);
      pstmt.setInt(1, ticketID);
      pstmt.executeUpdate();

    }
    catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }




	// continue coding for deleteRecords implementation
}
