package dao;

import java.net.UnknownHostException;
import java.sql.*;
import javamailApi.*;

public class FriendDAO extends ParentDAO{

	// 동료 추가 및 이메일 전송
	public String addFriend(String customerId, String dashboardId, String email) throws UnknownHostException {
		String queryCheckDashboard = "SELECT CUSTOMER_ID, DASHBOARD_ID FROM CLIENT_DASHBOARD WHERE CUSTOMER_ID = ? AND DASHBOARD_ID = ?";
		String queryCheckUser = "SELECT CUSTOMER_ID, JSONSTR FROM CLIENT WHERE CUSTOMER_ID = ?";
//		String queryAddFriend = "INSERT INTO FRIENDLIST (CUSTOMER_ID, FRIEND_ID) VALUES (?, ?)";
		String queryAddClient_dashboard = "INSERT INTO CLIENT_DASHBOARD (CUSTOMER_ID, DASHBOARD_ID) VALUES (?, ?)";

		String queryAddInvite = "INSERT INTO INVITE (FRIEND_ID, DASHBOARD_ID) VALUES (?, ?)";

		try {
			connDB();

			System.out.println("보내는 사람: " + customerId + " 받는 사람: " + email + " 초대하는 대시보드: " + dashboardId);

			// 0. 이메일로 이미 대시보드에 있는 사용자인지 확인
			stmt = con.prepareStatement(queryCheckDashboard);
			stmt.setString(1, email);
			stmt.setString(2, dashboardId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return "이미 대시보드에 존재하는 사용자입니다";
			}

			// 1. 이메일로 사용자가 존재하는지 확인
			stmt = con.prepareStatement(queryCheckUser);
			stmt.setString(1, email);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String userName = rs.getString("CUSTOMER_ID");

				// 2. 사용자 존재 시 동료 목록에 추가
//				stmt = con.prepareStatement(queryAddFriend);
//				stmt.setString(1, customerId);
//				stmt.setString(2, email);
//				int result = stmt.executeUpdate();

				stmt = con.prepareStatement(queryAddClient_dashboard);
				stmt.setString(1, email);
				stmt.setString(2, dashboardId);
				int result = stmt.executeUpdate();

				if (result > 0) {
					// 이메일 전송
					EmailSender.sendInvitationEmail(customerId, email, userName);
					return "OK";
				} else {
					return "동료 추가 실패";
				}
			} else {
				// 초대 메일에 추가 (나중에 회원가입할때 불러올 것임)
				stmt = con.prepareStatement(queryAddInvite);
				stmt.setString(1, email);
				stmt.setString(2, dashboardId);
				int result = stmt.executeUpdate();

				if (result > 0) {
					// 이메일 전송
					EmailSender.sendInvitationEmail2(customerId, email, customerId);
					return "OK";
				} else {
					return "동료 추가 실패";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "데이터베이스 오류";
		} finally {
			closeResources();
		}
	}
}
