package mp.utp.xyz.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection
{
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			Class.forName("org.sqlite.JDBC");

			String url = "jdbc:sqlite:database.db";
			conn = DriverManager.getConnection(url);

			return conn;
		}
		catch (SQLException | ClassNotFoundException e)
		{
			System.out.println("DB CONNECTION ERROR");
			System.out.println(e.getMessage());
			return null;
		}
	}
}
