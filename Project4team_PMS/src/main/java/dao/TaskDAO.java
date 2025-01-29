package dao;

import java.sql.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class TaskDAO {
	// 데이터베이스 연결 정보
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521/orcl";
	String user = "c##apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public JSONArray getTasks() {  // 그냥 모든 작업을 다 보는 메서드
		JSONArray tasks = new JSONArray();
		// CLIENT 테이블이랑 TASK 테이블이랑 조인해서 누가 어느 작업을 올렸는지 조회한다.
		String query = "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
                + "FROM CLIENT C, TASK T "
                + "WHERE C.CUSTOMER_ID = T.CUSTOMER_ID";
		try {
			connDB();
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				// CLIENT 테이블 JSONSTR 파싱
				String clientJsonStr = rs.getString("CLIENT_JSON");
				JSONObject clientJson = (JSONObject) new JSONParser().parse(clientJsonStr);
				String customerName = clientJson.get("name").toString();

				// TASK 테이블 데이터 생성
				JSONObject taskObj = new JSONObject();
				taskObj.put("taskData", rs.getString("TASK_JSON"));
				taskObj.put("dashboardId", rs.getString("DASHBOARD_ID"));
				taskObj.put("customerName", customerName);

				// JSONArray에 추가
				tasks.add(taskObj);
			}
			System.out.println(tasks.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}

		return tasks;
	}
	
	public JSONArray getFriendTasks(String customerId) { // 동료과의 작업물만 보는 메서드
		System.out.println(customerId);
	    JSONArray tasks = new JSONArray();
	    //로그인한 사용자의 동료들 + 나자신과의 관련된 작업들만 조회
	    String query = "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
	             + "FROM FRIENDLIST F "
	             + "JOIN CLIENT C ON C.CUSTOMER_ID = F.FRIEND_ID "
	             + "JOIN TASK T ON T.CUSTOMER_ID = F.FRIEND_ID "
	             + "WHERE F.CUSTOMER_ID = ? "

	             + "UNION "

	             + "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
	             + "FROM CLIENT C "
	             + "JOIN TASK T ON C.CUSTOMER_ID = T.CUSTOMER_ID "
	             + "WHERE C.CUSTOMER_ID = ?";


	    try {
	        connDB(); // DB 연결
	        stmt = con.prepareStatement(query);
	        stmt.setString(1, customerId); // 사용자의 CUSTOMER_ID를 인자로 이용
	        stmt.setString(2, customerId);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            try {
	                // CLIENT JSON 파싱
	                String clientJsonStr = rs.getString("CLIENT_JSON");
	                JSONObject clientJson = (JSONObject) new JSONParser().parse(clientJsonStr);
	                String customerName = clientJson.get("name").toString();

	                // TASK JSON 생성
	                String taskJsonStr = rs.getString("TASK_JSON");
	                JSONObject taskJson = (JSONObject) new JSONParser().parse(taskJsonStr);
	                // taskId 추가!!
	                taskJson.put("taskId", rs.getString("TASK_ID")); 
	                taskJson.put("dashboardId", rs.getString("DASHBOARD_ID"));
	                taskJson.put("customerName", customerName);

	                tasks.add(taskJson); // 결과 배열에 추가
	                System.out.println(tasks.toJSONString());
	            } catch (Exception e) {
	                System.err.println("JSON 파싱 오류: " + e.getMessage());
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace(); // 에러 출력
	    } finally {
	    	closeResources();
	    }

	    return tasks;
	}
	
	public int insertTask(String taskName, String customerId) {
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
