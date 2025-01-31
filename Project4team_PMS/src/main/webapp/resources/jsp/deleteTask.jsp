<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dao.*" %>

<%
    request.setCharacterEncoding("UTF-8");

    String taskId = request.getParameter("taskId");

    if (taskId == null || taskId.trim().isEmpty()) {
        out.print("ERROR");
        return;
    }

    TaskDAO dao = new TaskDAO();
    boolean isDeleted = dao.deleteTask(taskId);

    if (isDeleted) {
        out.print("OK");
    } else {
        out.print("ERROR");
    }
%>