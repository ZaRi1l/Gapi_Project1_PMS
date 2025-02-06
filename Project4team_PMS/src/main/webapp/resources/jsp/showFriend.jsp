<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="dao.*" %>
<%@ page import="org.json.simple.*" %>

<%	
	int dashboardId = Integer.parseInt(request.getParameter("dashboardId")); // 세션 테이블에서 대시보드 id 얻어오기
	System.out.print(dashboardId);
	
	JSONArray friends = new Cliend_DashboardDAO().showFriends(dashboardId); // 현재 대시보드 ID로 대시보드 안에 사람들 조회
	
	out.print(friends);
%>
