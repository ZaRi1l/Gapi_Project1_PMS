package dao;

import java.sql.*;

import javax.naming.NamingException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientDAO {
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/gapi?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
	String user = "apple";
	String password = "1111";

	private Connection con;
	private PreparedStatement stmt;
	private PreparedStatement stmt2; // invite 테이블에 있나 확인용
	private ResultSet rs;

	public int insert(String cEmail, String jsonstr) throws NamingException, SQLException {
		System.out.println(cEmail);
		System.out.println(jsonstr);
		stmt = null;
		connDB();

		try {
			// 이메일 중복 확인
			String checkSql = "SELECT COUNT(*) FROM client WHERE customer_id = ?";
			PreparedStatement checkStmt = con.prepareStatement(checkSql);
			checkStmt.setString(1, cEmail);
			rs = checkStmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return 1; // 이메일 중복
			}

			// 이메일 삽입
			String sql = "INSERT INTO client(customer_id, jsonstr) VALUES(?, ?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, cEmail);
			stmt.setString(2, jsonstr);
			int count = stmt.executeUpdate();

			// 초대받은 사용자인지 확인
			String inviteSql = "SELECT FRIEND_ID, DASHBOARD_ID FROM INVITE WHERE FRIEND_ID = ?";
			stmt2 = con.prepareStatement(inviteSql);
			stmt2.setString(1, cEmail);
			rs = stmt2.executeQuery();

			// 대시보드 할당해주기, invite 테이블에서 삭제하기
			while (rs.next()) {
				String friendId = rs.getString("FRIEND_ID");
				String dashboardId = rs.getString("DASHBOARD_ID");

				// 대시보드 할당하기
				String queryAddClient_dashboard = "INSERT INTO CLIENT_DASHBOARD (CUSTOMER_ID, DASHBOARD_ID) VALUES (?, ?)";
				stmt = con.prepareStatement(queryAddClient_dashboard);
				stmt.setString(1, friendId);
				stmt.setString(2, dashboardId);
				stmt.executeUpdate();

				// invite 테이블에서 삭제하기
				String queryDeleteInvite = "DELETE FROM INVITE WHERE FRIEND_ID = ? AND DASHBOARD_ID = ?";
				stmt = con.prepareStatement(queryDeleteInvite);
				stmt.setString(1, friendId);
				stmt.setString(2, dashboardId);
				stmt.executeUpdate();
			}

			return (count == 1) ? 0 : 2;

		} finally {
			closeResources();
		}
	}

	public int login(String email, String password) throws SQLException, ParseException {
		try {
			connDB();
			System.out.println(email);
			String query = "SELECT jsonstr FROM client WHERE customer_id = ?";
			stmt = con.prepareStatement(query);
			stmt.setString(1, email);

			rs = stmt.executeQuery();
			if (!rs.next())
				return 1; // 일치하는 이메일(id)X

			String jsonstr = rs.getString("jsonstr");
			JSONObject obj = (JSONObject) (new JSONParser()).parse(jsonstr);
			String pass = obj.get("password").toString();
			System.out.println(pass);

			if (!password.equals(pass)) // 비민번호 틀림
				return 2;

			return 0; // 로그인 성공
		} finally {
			closeResources();
		}
	}

	public void connDB() {
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
