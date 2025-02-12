package dao;

import java.sql.*;

public class ParentDAO {
	
	protected String driver = "com.mysql.jdbc.Driver";
	protected String url = "jdbc:mysql://localhost:3306/gapi?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
	protected String user = "apple";
	protected String password = "1111";

	protected Connection con;
	protected PreparedStatement stmt;
	protected PreparedStatement stmt2; // invite 테이블에 있나 확인용
	protected ResultSet rs;
	
	protected void connDB() {
		try {
			Class.forName(driver); // JDBC 드라이버 로드
			System.out.println("jdbc driver loading success.");

			// 첫 번째 URL로 연결 시도
			try {
				con = DriverManager.getConnection(url, user, password); // DB 연결
				System.out.println("ocrl로 OracleDB접속 성공!: " + url);
			} catch (Exception e) {
				System.out.println("orcl --> XE");

				// 두 번째 URL로 재시도
				String alternateUrl = "jdbc:oracle:thin:@localhost:1521/XE";
				con = DriverManager.getConnection(alternateUrl, user, password);
				System.out.println("XE로 OracleDB접속 성공: " + alternateUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void closeResources() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			System.out.println("JDBC 닫음");
		}
	}

}
