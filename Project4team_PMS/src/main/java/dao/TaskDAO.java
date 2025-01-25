package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import vo.TaskVo;

public class TaskDAO {
	// 데이터베이스 연결 정보
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521/orcl";
	String user = "c##apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private ResultSet rs;

	// 1. Task 목록 조회
	public ArrayList<TaskVo> getTasks() {
		ArrayList<TaskVo> tasks = new ArrayList<>();
		String query = "SELECT c.NAME, t.TASK, t.STATUS FROM CLIENT c, TASK t WHERE c.EMAIL = t.EMAIL";

		try {
			connDB(); // DB 연결
			stmt = con.prepareStatement(query);
			rs = stmt.executeQuery();
			System.out.println("select문 실행");
			
			while (rs.next()) {
				TaskVo task = new TaskVo();
				task.setName(rs.getString("NAME"));
				task.setTask(rs.getString("TASK")); // 필드명 TASK
				task.setStatus(rs.getString("STATUS")); // 필드명 STATUS
				tasks.add(task);
			}
			System.out.println("select문으로 부터 조회한 값");
			for(TaskVo t : tasks) {
				System.out.println(t);
			}
			System.out.println("--------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
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
