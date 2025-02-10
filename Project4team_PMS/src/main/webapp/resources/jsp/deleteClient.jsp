<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="dao.*"%>

<%
	request.setCharacterEncoding("utf-8");

	String customerId = (String) session.getAttribute("customerId");
		
	ClientDAO dao = new ClientDAO();
	boolean isDeleted = dao.deleteClient(customerId);
	
	if (isDeleted) {
		out.print("OK");
	} else {
		out.print("ERROR");
	}
%>