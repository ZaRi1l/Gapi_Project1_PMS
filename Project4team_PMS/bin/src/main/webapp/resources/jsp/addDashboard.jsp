<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.json.simple.JSONObject, org.json.simple.parser.JSONParser"%>
<%@ page import="dao.*"%>

<%
request.setCharacterEncoding("UTF-8");

String addDashboardName = request.getParameter("addDashboardName");
String customerId = (String) session.getAttribute("customerId");
System.out.println("대시보드 생성을 위해 받은 값들 task: " + addDashboardName + " 세션 값: " + customerId);
DashboardDAO dao = new DashboardDAO();

try {

	int result = dao.insertDashboard(addDashboardName, customerId);

	if (result == 1) {
		out.print("OK");
	} else {
		out.print("ERROR");
	}
} catch (Exception e) {
	e.printStackTrace();
	out.print("ERROR");
}
%>