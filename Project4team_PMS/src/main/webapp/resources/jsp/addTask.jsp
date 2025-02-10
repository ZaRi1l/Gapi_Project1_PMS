<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="dao.*" %>
<%@ page import="org.json.simple.*" %>

<%
    // ✅ 현재 로그인한 사용자 ID를 세션에서 가져오기
    String customerId = (String) session.getAttribute("customerId"); 
    String taskName = request.getParameter("task");
    String status = request.getParameter("status");
    String estimatedSP = request.getParameter("Estimated_SP");
    String epic = request.getParameter("epic");
    String dashboardIdParam = request.getParameter("dashboardId");

    int dashboardId = 0;
    if (dashboardIdParam != null && !dashboardIdParam.isEmpty()) {
        dashboardId = Integer.parseInt(dashboardIdParam);
    }

    TaskDAO taskDAO = new TaskDAO();
    int result = taskDAO.insertTask(taskName, customerId, dashboardId, status, estimatedSP, epic);

    out.print(result);
%>