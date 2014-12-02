package hirondelle.predict.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import hirondelle.predict.config.Configuration;

public class DatabaseHelper {
	
	private Connection connection;
	
	public DatabaseHelper() {
		openConnection();
	}
	
	private void openConnection() {
		try {
			String url = Configuration.getProperty("databaseConnectionString");
			String user = Configuration.getProperty("databaseUser");
			String password = Configuration.getProperty("databasePassword");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int executeUpdateQuery(String query) {
		try {
			Statement st = connection.createStatement();
			int updateCount = st.executeUpdate(query);
			st.close();
			return updateCount;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int executeUpdateQuery(String query, String... parameters) {
		try {
			PreparedStatement st = connection.prepareStatement(query);
			for(int i=1; i<=parameters.length; i++) {
				st.setString(i, parameters[i-1]);
			}
			int updateCount = st.executeUpdate();
			st.close();
			return updateCount;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public PreparedStatement createStatement(String query, String... parameters) {
		try {
			PreparedStatement st = connection.prepareStatement(query);
			for(int i=1; i<=parameters.length; i++) {
				st.setString(i, parameters[i-1]);
			}
			return st;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public boolean checkUser(String username) {
		try {
			String queryString = "SELECT COUNT(*) FROM Users WHERE LoginName = ?";
			PreparedStatement st = createStatement(queryString, username);
			ResultSet rs = st.executeQuery();
			boolean hasData = rs.next() && rs.getInt(1) > 0;
			rs.close();
			st.close();
			return hasData;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean checkUserByEmail(String email) {
		try {
			String queryString = "SELECT COUNT(*) FROM Users WHERE Email = ?";
			PreparedStatement st = createStatement(queryString, email);
			ResultSet rs = st.executeQuery();
			boolean hasData = rs.next() && rs.getInt(1) > 0;
			rs.close();
			st.close();
			return hasData;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void deleteUser(String username) {
		String[] queries = {
			"DELETE FROM Prediction where PredictionListFK = (SELECT Id FROM PredictionList where UserFK = (SELECT Id FROM Users where LoginName = ?));",
			"DELETE FROM PredictionList where UserFK = (SELECT Id FROM Users where LoginName = ?);",
			"DELETE FROM UserRole where LoginName = ?;",
			"DELETE FROM Users where LoginName = ?;"
		};
		for(String query : queries) {
			executeUpdateQuery(query, username);
		}
	}
	
	public void resetUserPasswordWithNonce(String loginName, String password, String passwordNonce) {
		String[] queries = {
			"UPDATE Users SET Password = ?, PasswordNonce=?, PasswordNonceCreatedOn=NOW()  where LoginName = ?;"
		};
		for(String query : queries) {
			executeUpdateQuery(query, password, passwordNonce, loginName);
		}
	}
	
	public void resetUserPassword(String loginName, String password) {
		String[] queries = {
			"UPDATE Users SET Password = ?, PasswordNonce=null, PasswordNonceCreatedOn=null  where LoginName = ?;"
		};
		for(String query : queries) {
			executeUpdateQuery(query, password, loginName);
		}
	}
	
	public void deleteUserByEmail(String email) {
		String[] queries = {
			"DELETE FROM Prediction where PredictionListFK = (SELECT Id FROM PredictionList where UserFK = (SELECT Id FROM Users where Email = ?));",
			"DELETE FROM PredictionList where UserFK = (SELECT Id FROM Users where Email = ?);",
			"DELETE FROM UserRole where LoginName = (SELECT LoginName FROM Users where Email = ?);",
			"DELETE FROM Users where Email = ?;"
		};
		for(String query : queries) {
			executeUpdateQuery(query, email);
		}
	}
	
	public void createUser(String loginName, String screenName, String password, String email) {
        String userQuery = "INSERT INTO predict.Users (Id, LoginName, ScreenName, Password, Email) VALUES (99, ?, ?, ?, ?);";
        executeUpdateQuery(userQuery, loginName, screenName, password, email);
      //insert with default Rule user
        String userRoleQuery = "INSERT INTO predict.UserRole (LoginName, Role) VALUES (?,'user');";
        executeUpdateQuery(userRoleQuery, loginName);
    }
	
}
