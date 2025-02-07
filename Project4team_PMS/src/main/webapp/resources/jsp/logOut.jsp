<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="dao.ClientDAO"%>

<%
	request.setCharacterEncoding("utf-8");
	

	session.removeValue("customerId"); //세션 비우기
	out.print("OK"); // 로그인 성공
	
%>