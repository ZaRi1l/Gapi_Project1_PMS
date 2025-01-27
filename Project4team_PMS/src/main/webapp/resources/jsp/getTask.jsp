<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="dao.*" %>
<%@ page import="org.json.simple.*" %>

<%	
	String customerId = (String) session.getAttribute("customerId"); // 세션 테이블에서 로그인한 id얻어오기
	JSONArray tasks = new TaskDAO().getFriendTasks(customerId); // TaskDAO를 통해서 DB접근 후 Task 데이터 가져오기
	out.print(tasks);
%>
