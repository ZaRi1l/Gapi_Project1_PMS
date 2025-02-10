package dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TaskDAO {
	// 데이터베이스 연결 정보
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/gapi?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
	String user = "apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	public JSONArray getTasks() { // 그냥 모든 작업을 다 보는 메서드
		JSONArray tasks = new JSONArray();
		// CLIENT 테이블이랑 TASK 테이블이랑 조인해서 누가 어느 작업을 올렸는지 조회한다.
		String query = "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
				+ "FROM CLIENT C, TASK T " + "WHERE C.CUSTOMER_ID = T.CUSTOMER_ID";
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
	
	public JSONObject getTask(String taskId) { // 그냥 모든 작업을 다 보는 메서드
		// CLIENT 테이블이랑 TASK 테이블이랑 조인해서 누가 어느 작업을 올렸는지 조회한다.
		JSONObject taskObj = new JSONObject();
		String query = "SELECT JSONSTR FROM TASK WHERE TASK_ID = ?";
		try {
			connDB();
			stmt = con.prepareStatement(query);
			stmt.setString(1, taskId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				// CLIENT 테이블 JSONSTR 파싱
				String taskJsonStr = rs.getString("JSONSTR");
				JSONObject taskJson = (JSONObject) new JSONParser().parse(taskJsonStr);
				
				System.out.println(taskJsonStr);

			    String task = taskJson.get("task") != null ? taskJson.get("task").toString() : "";
			    String status = taskJson.get("status") != null ? taskJson.get("status").toString() : "";
			    String estimited_ep = taskJson.get("Estimated_SP") != null ? taskJson.get("Estimated_SP").toString() : "";
			    String epic = taskJson.get("epic") != null ? taskJson.get("epic").toString() : "";

				// TASK 테이블 데이터 생성
				taskObj.put("task", task);
				taskObj.put("status", status);
				taskObj.put("Estimated_SP", estimited_ep);
				taskObj.put("epic", epic);

			}
			System.out.println(taskObj.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return taskObj;
	}
	

	public JSONArray getFriendTasks(String customerId) { // 동료과의 작업물만 보는 메서드
		System.out.println(customerId);
		JSONArray tasks = new JSONArray();
		// 로그인한 사용자의 동료들 + 나자신과의 관련된 작업들만 조회
		String query = "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
				+ "FROM FRIENDLIST F " + "JOIN CLIENT C ON C.CUSTOMER_ID = F.FRIEND_ID "
				+ "JOIN TASK T ON T.CUSTOMER_ID = F.FRIEND_ID " + "WHERE F.CUSTOMER_ID = ? "

				+ "UNION "

				+ "SELECT C.CUSTOMER_ID, C.JSONSTR AS CLIENT_JSON, T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON "
				+ "FROM CLIENT C " + "JOIN TASK T ON C.CUSTOMER_ID = T.CUSTOMER_ID " + "WHERE C.CUSTOMER_ID = ?";

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
//	                System.out.println(tasks.toJSONString());
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

	public JSONArray getDashboardTasks(String dashboardId) { // 대시보드 작업물 보는 메서드
		System.out.println(dashboardId);
		JSONArray tasks = new JSONArray();
		// 대시보드 작업물 조회
		String query = "SELECT T.TASK_ID, T.DASHBOARD_ID, T.JSONSTR AS TASK_JSON, C.JSONSTR AS CLIENT_JSON "
				+ "FROM TASK T" + " JOIN CLIENT C ON T.CUSTOMER_ID = C.CUSTOMER_ID " + "WHERE T.DASHBOARD_ID = ?";

		try {
			connDB(); // DB 연결
			stmt = con.prepareStatement(query);
			stmt.setString(1, dashboardId); // DASHBOARD_ID를 인자로 이용
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
			// System.out.println(tasks.toJSONString());
			closeResources();
		}

		return tasks;
	}

	public int insertTask(String taskName, String customerId, int dashboardId, String status, String estimatedSP, String epic) {
	    String insertTaskQuery = "INSERT INTO TASK (TASK_ID, DASHBOARD_ID, CUSTOMER_ID, JSONSTR) VALUES (?, ?, ?, ?)";
	    String maxTaskIdQuery = "SELECT MAX(TASK_ID) + 1 AS MAXTASKID FROM TASK";

	    Date now = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String strNow = sdf.format(now);

	    int TASK_ID = 0;
	    int DASHBOARD_ID = dashboardId;

	    // ✅ JSON 데이터에 필요한 값 추가
	    JSONObject jsonstr = new JSONObject();
	    jsonstr.put("task", taskName);
	    jsonstr.put("startdate", strNow);
	    jsonstr.put("status", status);
	    jsonstr.put("Estimated_SP", estimatedSP);
	    jsonstr.put("epic", epic);

	    try {
	        connDB();
	        stmt = con.prepareStatement(maxTaskIdQuery);
	        rs = stmt.executeQuery();
	        if (rs.next()) {
	            TASK_ID = rs.getInt("MAXTASKID");
	        }

	        // ✅ Task 생성 시 customerId를 변경할 수 없도록 함
	        stmt = con.prepareStatement(insertTaskQuery);
	        stmt.setInt(1, TASK_ID);
	        stmt.setInt(2, DASHBOARD_ID);
	        stmt.setString(3, customerId); // ✅ 로그인한 사용자 ID 고정
	        stmt.setString(4, jsonstr.toJSONString());

	        return stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    } finally {
	        closeResources();
	    }
	}

	public boolean updateTask(String taskId, String task, String status, String estimited_ep, String epic) throws ParseException {
	    String selectQuery = "SELECT JSONSTR FROM TASK WHERE TASK_ID = ?";
	    String updateQuery = "UPDATE TASK SET JSONSTR = ? WHERE TASK_ID = ?";
	    JSONObject updateJson = null;
	    
	    try {
	        connDB();

	        // 1. 기존 JSON 데이터 조회
	        stmt = con.prepareStatement(selectQuery);
	        stmt.setString(1, taskId);
	        rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            String updateStr = rs.getString("JSONSTR");
	            updateJson = (JSONObject) new JSONParser().parse(updateStr);
	            updateJson.put("task", task);
	            updateJson.put("status", status);
	            updateJson.put("Estimated_SP", estimited_ep);  // 변수 오타 수정
	            updateJson.put("epic", epic);
	        } else {
	            return false;  // 해당 TASK_ID가 없으면 업데이트하지 않음
	        }

	        // 2. 업데이트 실행
	        stmt = con.prepareStatement(updateQuery);
	        stmt.setString(1, updateJson.toJSONString()); // JSON 객체를 문자열로 변환
	        stmt.setString(2, taskId);

	        int rowsUpdated = stmt.executeUpdate();
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

	// 삭제
	public boolean deleteTask(String taskId) {
		String query = "DELETE FROM TASK WHERE TASK_ID = ?";

		try {
			connDB();
			stmt = con.prepareStatement(query);
			stmt.setString(1, taskId);

			int rowsDeleted = stmt.executeUpdate(); // 삭제된 행 수 반환
			return rowsDeleted > 0; // 성공 시 true, 실패 시 false
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}
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
}
