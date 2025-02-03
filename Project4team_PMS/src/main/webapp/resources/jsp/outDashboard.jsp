<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dao.DashboardDAO" %>

<%
    request.setCharacterEncoding("UTF-8");

	String customerId = request.getParameter("customerId");
    String dashboardId = request.getParameter("dashboardId");

    DashboardDAO dao = new DashboardDAO();
    boolean isOuted = dao.outDashboard(customerId, dashboardId);

    if (isOuted) {
        out.print("OK");
    } else {
        out.print("ERROR");
    }
%>