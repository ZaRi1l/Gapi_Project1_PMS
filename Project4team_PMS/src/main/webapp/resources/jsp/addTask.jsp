<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.JSONObject, org.json.simple.parser.JSONParser" %>
<%@ page import="dao.*" %>

<%
    request.setCharacterEncoding("UTF-8");

    String taskName = request.getParameter("task");
    String customerId = request.getParameter("customerId");
    System.out.println("작업 추가를 위해 받은 값들 task: " + taskName + " 세션 값: " + customerId);
    TaskDAO dao = new TaskDAO();

    try {

        int result = dao.insertTask(taskName, customerId);

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