package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Cliend_DashboardDAO extends ParentDAO{

	public JSONArray showFriends(int dashboardId) { // 현재 대시보드 ID로 대시보드 안에 사람들 조회
		JSONArray friends = new JSONArray();
		// CLIENT_DASHBOARD 테이블이랑 CLIENT 테이블 조인해서 해당 dashboardId에 속해있는 사람들만 조회하는 쿼리문
		String query = "SELECT JSON_VALUE(JSONSTR, '$.name') AS NAME " + "FROM CLIENT_DASHBOARD CD , CLIENT C "
				+ "WHERE CD.customer_id = C.customer_id and dashboard_id = ?";
		try {
			connDB();
			stmt = con.prepareStatement(query);
			stmt.setInt(1, dashboardId);
			rs = stmt.executeQuery();
			int index = 0;
			while (rs.next()) {
				// JSONObject(friendsObj)에 추가
				JSONObject friendsObj = new JSONObject();
				friendsObj.put("name", rs.getString("NAME"));

				// JSONArray(friends)에 추가
				friends.add(friendsObj);
			}
			System.out.println(friends.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}

		return friends;
	}

	public int deleteFriends(int dashboardId, String name) throws SQLException { 
	    // 해당 이름의 customer_id 가져오기
	    String selectQuery = "SELECT CD.customer_id " +
	                         "FROM CLIENT_DASHBOARD CD, CLIENT C " +
	                         "WHERE CD.customer_id = C.customer_id " +
	                         "AND JSON_VALUE(JSONSTR, '$.name') = ?";

	    // 해당 이름의 customer_id + 해당 dashboard_id 로 삭제
	    String deleteQuery = "DELETE FROM CLIENT_DASHBOARD WHERE customer_id = ? AND dashboard_id = ?";

	    String uid = null;
	    int result = 0; // 삭제 결과 반환 변수

	    try {
	        connDB(); // DB 연결

	        // 1️. customer_id 조회
	        PreparedStatement selectStmt = con.prepareStatement(selectQuery);
	        selectStmt.setString(1, name);
	        ResultSet rs = selectStmt.executeQuery();

	        if (rs.next()) {
	            uid = rs.getString("customer_id");
	        }
	        rs.close();
	        selectStmt.close(); // 리소스 정리

	        // 2️. 해당 customer_id가 존재하는 경우 삭제 실행
	        if (uid != null) {
	            PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
	            deleteStmt.setString(1, uid);
	            deleteStmt.setInt(2, dashboardId);
	            result = deleteStmt.executeUpdate(); // DELETE 실행
	            deleteStmt.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        closeResources(); // 연결 닫기
	    }
	    
	    return result; // 성공 시 1 이상, 실패 시 0 반환
	}

}
