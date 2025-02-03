<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.FriendDAO" %>
<%@ page import="java.sql.*" %>
<%
    String customerId = request.getParameter("customerId");
    String email = request.getParameter("email");

    FriendDAO dao = new FriendDAO();
    String result = dao.addFriend(customerId, email);

    out.print(result); // 결과를 응답으로 반환
%>
