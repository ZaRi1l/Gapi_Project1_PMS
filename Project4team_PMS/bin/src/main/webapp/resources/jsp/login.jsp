<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="dao.ClientDAO"%>

<%
	request.setCharacterEncoding("utf-8");
	
	String uid = request.getParameter("id");
	String upass = request.getParameter("ps");
	
	ClientDAO dao = new ClientDAO();
	int code = dao.login(uid, upass);
	if (code == 1) {
		out.print("NE"); // 일치하는 이메일(id)X
	} else if (code == 2) {
		out.print("PE"); // 비민번호 틀림
	} else {
		session.setAttribute("customerId", uid); // 세션 테이블에 아이디 저장 <-- 로그인 한 사람 + 동료만의 작업들을 보기 위해
		out.print("OK"); // 로그인 성공
	}
%>