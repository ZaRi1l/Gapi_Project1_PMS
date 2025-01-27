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
	    //로그인한 사용자의 동료들과 관련된 작업들만 조회
	    String query = "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
	                    + "FROM FRIENDLIST F "
	                    + "JOIN CLIENT C ON C.CUSTOMER_ID = F.FRIEND_ID "
	                    + "JOIN TASK T ON T.CUSTOMER_ID = F.FRIEND_ID "
	                    + "WHERE F.CUSTOMER_ID = ?";


	    try {
	        connDB(); // DB 연결
	        stmt = con.prepareStatement(query);
	        stmt.setString(1, customerId); // 사용자의 CUSTOMER_ID를 인자로 이용
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
