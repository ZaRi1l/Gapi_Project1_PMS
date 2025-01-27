<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dao.ClientDAO"%>
<%
	request.setCharacterEncoding("utf-8");
	
	String uid = request.getParameter("id");
	String jsonstr = request.getParameter("jsonstr");
	
	System.out.println(uid);
	System.out.println(jsonstr);
	
	ClientDAO dao = new ClientDAO();
	if (dao.insert(uid, jsonstr) == 0) {
		//session.setAttribute("id", uid);
		out.print("OK");
	} else if (dao.insert(uid, jsonstr) == 2) {
		out.print("ER");
	} else {
		out.print("EX");
	}
%>