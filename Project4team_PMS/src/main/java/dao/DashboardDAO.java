package dao;

import java.sql.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class DashboardDAO {
	// 데이터베이스 연결 정보
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521/orcl";
	String user = "c##apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public JSONArray getDashboard(String customerId) { // 현재 고객 ID로 대시보드 조회
		JSONArray dashboards = new JSONArray();
		// CLIENT 테이블이랑 TASK 테이블이랑 조인해서 누가 어느 작업을 올렸는지 조회한다.
		String query = "SELECT CD.DASHBOARD_ID, D.JSONSTR DASHBOARD_JSON "
				+ "FROM DASHBOARD D, CLIENT C, CLIENT_DASHBOARD CD "
				+ "WHERE C.CUSTOMER_ID = ? AND CD.DASHBOARD_ID = D.DASHBOARD_ID AND C.CUSTOMER_ID = CD.CUSTOMER_ID";
		try {
			connDB();
			stmt = con.prepareStatement(query);
			stmt.setString(1, customerId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				// DASHBOARD 테이블 데이터 생성
				JSONObject dashboardObj = new JSONObject();
				dashboardObj.put("dashboardData", rs.getString("DASHBOARD_JSON"));
				dashboardObj.put("dashboardId", rs.getString("DASHBOARD_ID"));

				// JSONArray에 추가
				dashboards.add(dashboardObj);
			}
			System.out.println(dashboards.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}

		return dashboards;
	}

	public int insertDashboard(String dashboardName, String customerId) {
		String checkDashboardKey = "SELECT MAX(Dashboard_id) + 1 AS MK FROM DASHBOARD";
		String insertDashboardQuery = "INSERT INTO DASHBOARD (DASHBOARD_ID, JSONSTR) VALUES (?, ?)";
		String insertClientDashboardQuery = "INSERT INTO CLIENT_DASHBOARD (CUSTOMER_ID, DASHBOARD_ID) VALUES (?, ?)";

		// dashboard 테이블에 들어갈 json
		JSONObject jsonstr = new JSONObject();
		jsonstr.put("name", dashboardName);
		jsonstr.put("startdate", "2024-12-12");
		jsonstr.put("enddate", "2025-12-11");

		try {
			connDB();

			// 1. DASHBOARD의 최대 키 값 + 1 가져오기
			stmt = con.prepareStatement(checkDashboardKey);
			rs = stmt.executeQuery();
			rs.next();
			int index = rs.getInt("MK");

			// 2. 대시보드 삽입
			stmt = con.prepareStatement(insertDashboardQuery);
			stmt.setLong(1, index);
			stmt.setString(2, jsonstr.toJSONString()); // JSON 데이터 저장
			stmt.executeUpdate();

			// 3. CLIENT DASHBOARD 삽입
			stmt = con.prepareStatement(insertClientDashboardQuery);
			stmt.setString(1, customerId);
			stmt.setLong(2, index);

			return stmt.executeUpdate(); // 성공 시 1 반환
		} catch (SQLException e) {
			e.printStackTrace();
			return 0; // 실패 시 0 반환
		} finally {
			closeResources();
		}
	}

	public boolean updateTask(String taskId, String task, String status, String estimited_ep, String epic) {
		String query = "UPDATE TASK SET JSONSTR = ? WHERE TASK_ID = ?";
		try {
			connDB();
			stmt = con.prepareStatement(query);

			// 문자열 포매팅으로 사용자의 입력값 할당
			String jsonStr = String.format(
					"{\"task\": \"%s\", \"status\": \"%s\", \"Estimeted_SP\": \"%s\", \"epic\": \"%s\"}", task, status,
					estimited_ep, epic);

			stmt.setString(1, jsonStr);
			stmt.setString(2, taskId);

			int rowsUpdated = stmt.executeUpdate(); // 수정 성공하면 '1' 반환
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}

	public void connDB() {
		try {
			Class.forName(driver); // JDBC 드라이버 로드
			System.out.println("JDBC driver loading success.");

			try {
				con = DriverManager.getConnection(url, user, password); // 첫 번째 URL로 연결
				System.out.println("Oracle connection success with URL: " + url);
			} catch (Exception e) {
				System.out.println("Connection failed with URL: " + url);
				System.out.println("Retrying with alternate URL...");

				// 대체 URL로 연결
				String alternateUrl = "jdbc:oracle:thin:@localhost:1521/XE";
				con = DriverManager.getConnection(alternateUrl, user, password);
				System.out.println("Oracle connection success with alternate URL: " + alternateUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 6. 자원 해제
	private void closeResources() {
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
	}
}
