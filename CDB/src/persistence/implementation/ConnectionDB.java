package persistence.implementation;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.MysqlDataSource;

final class ConnectionDB {
	
	private static final String url =
			"jdbc:mysql://localhost/computer-database-db";
	
	private static final String userName = "admincdb";
	
	private static final String password = "qwerty1234";
	
	private static final MysqlDataSource dataSource = new MysqlDataSource();
	
	static {
		dataSource.setUrl(url);
		dataSource.setUser(userName);
		dataSource.setPassword(password);
	}
	
	public static Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}

	private ConnectionDB() {}
}
