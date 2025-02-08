<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dao.*, org.json.simple.JSONArray" %>

<%
    String customerId = (String) session.getAttribute("customerId");
	System.out.println(customerId);

    if (customerId == null || customerId.isEmpty()) {
        out.print("[]"); // 고객 ID가 없으면 빈 배열 반환
        return;
    }

   
    TaskDAO calDAO = new TaskDAO();

    // 고객의 대시보드 목록 조회
    JSONArray dashboards = calDAO.getDashboard(customerId);
    JSONArray allTasks = new JSONArray();

    for (int i = 0; i < dashboards.size(); i++) {
        org.json.simple.JSONObject dashboard = (org.json.simple.JSONObject) dashboards.get(i);
        String dashboardId = (String) dashboard.get("dashboardId");
        System.out.println(dashboardId + "111111111111");

        // 해당 대시보드의 Task 목록 조회
        JSONArray tasks = calDAO.getTasksByDashboard(dashboardId);
        allTasks.addAll(tasks);
    }

    System.out.println("📢 JSON 응답: " + allTasks.toJSONString()); // 서버 콘솔에 출력 (디버깅용)
    out.print(allTasks.toJSONString()); // JSON 응답
%>