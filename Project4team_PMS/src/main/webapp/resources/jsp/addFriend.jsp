<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.*" %>
<%@ page import="java.sql.*" %>
<%
    String customerId = request.getParameter("customerId");	// 내 아이디(이메일)
	String dashboardId = request.getParameter("dashboardId");
    String email = request.getParameter("email");	// 친구 아이디

    FriendDAO dao = new FriendDAO();
    String result = dao.addFriend(customerId, dashboardId, email);

    out.print(result); // 결과를 응답으로 반환
%>
