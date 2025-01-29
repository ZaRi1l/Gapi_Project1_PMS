<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, dao.TaskDAO" %>
<%
    request.setCharacterEncoding("UTF-8");

    String taskId = request.getParameter("taskId");
    String task = request.getParameter("task");
    String status = request.getParameter("status");
    String estimited_ep = request.getParameter("Estimated_SP");
    String epic = request.getParameter("epic");

    TaskDAO dao = new TaskDAO();
    boolean success = dao.updateTask(taskId, task, status, estimited_ep, epic);

    if (success) {
        out.print("OK");
    } else {
        out.print("FAIL");
    }
%>
