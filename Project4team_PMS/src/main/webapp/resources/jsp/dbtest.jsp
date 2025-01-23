<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dao.ClientDAO"%>
<%@ page import="java.util.ArrayList"%>
<%
	// "post"방식에서 한글 처리를 위한 인코딩 설정
	request.setCharacterEncoding("utf-8");

	String uname = request.getParameter("name");

	if (uname != null && !uname.isEmpty()) {
		// DAO 객체 생성 및 list() 메서드를 호출
		ClientDAO cdao = new ClientDAO();
		ArrayList<String> clients = cdao.list(uname); // DAO에서 name을 전달하여 조회

		// 결과 출력
		if (clients != null && !clients.isEmpty()) {
			out.println("<h3>조회된 결과:</h3>");
			for (String client : clients) {
		out.println("<p>" + client + "</p>");
			}
		} else {
			out.println("<p>조회된 결과가 없습니다.</p>");
		}
	} else {
		out.println("<p>이름을 입력해주세요.</p>");
	}
	%>

