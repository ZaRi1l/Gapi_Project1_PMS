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

	public JSONArray getDashboard(String customerId) {  // 현재 고객 ID로 대시보드 조회
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
	
	
	
	public int insertDashboard(String taskName, String customerId) {
	    String checkDashboardQuery = "SELECT COUNT(*) FROM DASHBOARD WHERE DASHBOARD_ID = ?";
	    String insertDashboardQuery = "INSERT INTO DASHBOARD (DASHBOARD_ID, JSONSTR) VALUES (?, ?)";
	    String insertTaskQuery = "INSERT INTO TASK (TASK_ID, DASHBOARD_ID, CUSTOMER_ID, JSONSTR) VALUES (?, ?, ?, ?)";

	    String TASK_ID = customerId + "_" + taskName;
	    String DASHBOARD_ID = taskName + "_dashboard";
	    JSONObject jsonstr = new JSONObject();
	    jsonstr.put("task", taskName); 

	    try {
	        connDB();

	        // 1. DASHBOARD_ID 존재 여부 확인
	        stmt = con.prepareStatement(checkDashboardQuery);
	        stmt.setString(1, DASHBOARD_ID);
	        rs = stmt.executeQuery();
	        rs.next();
	        int count = rs.getInt(1);
	        
	        // 2. 만약 DASHBOARD_ID가 없으면 먼저 삽입 DASHBOARD_ID 이게 문제인데; x 100
	        if (count == 0) {
	            stmt = con.prepareStatement(insertDashboardQuery);
	            stmt.setString(1, DASHBOARD_ID);
	            stmt.setString(2, "{}");  // 기본 JSON 데이터 저장 (빈 객체)
	            stmt.executeUpdate();
	        }

	        // 3. TASK 삽입
	        stmt = con.prepareStatement(insertTaskQuery);
	        stmt.setString(1, TASK_ID);
	        stmt.setString(2, DASHBOARD_ID);
	        stmt.setString(3, customerId);
	        stmt.setString(4, jsonstr.toJSONString());

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
            
            //문자열 포매팅으로 사용자의 입력값 할당
            String jsonStr = String.format("{\"task\": \"%s\", \"status\": \"%s\", \"Estimeted_SP\": \"%s\", \"epic\": \"%s\"}", 
                task, status, estimited_ep, epic);

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
