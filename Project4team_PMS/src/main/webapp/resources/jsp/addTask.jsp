<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject, org.json.simple.parser.JSONParser" %>
<%@ page import="dao.*" %>

<%
    request.setCharacterEncoding("UTF-8");

    String taskName = request.getParameter("task");
    String customerId = request.getParameter("customerId");
    int dashboardId = Integer.valueOf(request.getParameter("dashboardId"));
    System.out.println("작업 추가를 위해 받은 값들 task: " + taskName + " customerId 값:" + customerId + "dashboardId 값" + dashboardId);
    TaskDAO dao = new TaskDAO();

    try {

        int result = dao.insertTask(taskName, customerId, dashboardId);

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