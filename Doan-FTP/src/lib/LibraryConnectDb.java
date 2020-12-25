package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LibraryConnectDb {
	private Connection conn;
	private String url;
	private String user;
	private String password;
	
	public LibraryConnectDb(){
		
		this.url = "jdbc:mysql://localhost:3306/user";
		this.user = "root";
		this.password ="";
		
	}
	public Connection getConnectMySql(){
		//nạp driver
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			if (conn != null ) System.out.println("Ket noi thanh cong");
			//sql server sua doi lai, gg
			Class.forName("com.mysql.jdbc.Driver");
			//String sql = "select * from account ";
			//st = conn.createStatement();
			//rs = st.executeQuery(sql);
		} catch (Exception e) {
			System.out.println("Khong the nap Driver");
			//khi lam du an sau nay nen throw ra de xu ly
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public static void main(String[] args) {
		LibraryConnectDb lDb = new LibraryConnectDb();
		System.out.println(lDb.getConnectMySql());
	}
}
