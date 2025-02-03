<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.json.simple.JSONObject, org.json.simple.parser.JSONParser"%>
<%@ page import="dao.*"%>

<%
	request.setCharacterEncoding("UTF-8");
	
	String taskName = request.getParameter("task");
	String customerId = request.getParameter("customerId");
	int dashboardId = Integer.valueOf(request.getParameter("dashboardId"));
	
	System.out.println("작업 추가를 위해 받은 값들 task: " + taskName + " customerId 값:" + customerId + "dashboardId 값" + dashboardId);
	
	int result = new TaskDAO().insertTask(taskName, customerId, dashboardId); // TaskDAO로 데이터 송,수신
	out.print(result); // 반환 값 index.html로 보내기
%>