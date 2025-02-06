package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.sql.*;

public class CalDashboardDAO {
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/gapi?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
	String user = "apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	// 고객 ID로 대시보드 조회
	public JSONArray getDashboard(String customerId) {
		JSONArray dashboards = new JSONArray();
		String query = "SELECT CD.DASHBOARD_ID FROM CLIENT_DASHBOARD CD WHERE CD.CUSTOMER_ID = ?";

		try {
			connDB();
			stmt = con.prepareStatement(query);
			stmt.setString(1, customerId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				JSONObject dashboardObj = new JSONObject();
				dashboardObj.put("dashboardId", rs.getString("DASHBOARD_ID"));
				dashboards.add(dashboardObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return dashboards;
	}

	private void connDB() {
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

	private void closeResources() {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (con != null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}