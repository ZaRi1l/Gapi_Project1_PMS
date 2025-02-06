package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.*;

public class CalDAO {
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/gapi?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
	String user = "apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public JSONArray getTasksByDashboard(String dashboardId) {
		JSONArray tasks = new JSONArray();
		String query = "SELECT JSONSTR FROM TASK WHERE DASHBOARD_ID = ?";

		try {
			connDB();
			stmt = con.prepareStatement(query);
			stmt.setString(1, dashboardId);
			rs = stmt.executeQuery();
			JSONParser parser = new JSONParser();

			while (rs.next()) {
				String jsonStr = rs.getString("JSONSTR");
				try {
					JSONObject jsonObject = (JSONObject) parser.parse(jsonStr);
					String taskName = (String) jsonObject.get("task"); // 작업 이름
					String startDate = (String) jsonObject.get("startdate"); // 시작 날짜
	
	               

					// JSON 객체 생성
					JSONObject taskObj = new JSONObject();
					taskObj.put("title", taskName);
					taskObj.put("start", startDate);

					tasks.add(taskObj);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return tasks;
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