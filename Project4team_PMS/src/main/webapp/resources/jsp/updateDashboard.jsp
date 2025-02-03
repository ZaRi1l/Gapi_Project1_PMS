<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, dao.DashboardDAO" %>
<%
    request.setCharacterEncoding("UTF-8");

    String dashboardId = request.getParameter("dashboardId");
    String dashboardName = request.getParameter("dashboardName");
    String dashboardStartDate = request.getParameter("dashboardStartDate");

    DashboardDAO dao = new DashboardDAO();
    boolean success = dao.updateDashboard(dashboardId, dashboardName, dashboardStartDate);

    if (success) {
        out.print("OK");
    } else {
        out.print("FAIL");
    }
%>
