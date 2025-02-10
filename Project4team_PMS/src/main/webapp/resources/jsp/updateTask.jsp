<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, dao.TaskDAO, org.json.simple.*" %>
<%
    request.setCharacterEncoding("UTF-8");

    String taskId = request.getParameter("taskId");
/*     String task = request.getParameter("task");
    String status = request.getParameter("status");
    String estimited_ep = request.getParameter("Estimated_SP");
    String epic = request.getParameter("epic"); */
    String field = request.getParameter("field");
    String value = request.getParameter("value");

    System.out.println("------field 값 :" + field);
    
    if(field == null) out.print("FAIL");
    
    TaskDAO dao = new TaskDAO();
    JSONObject taskObj = dao.getTask(taskId);
    
    switch(field) {
    case "task":
    	taskObj.put("task", value);
    	break;
    case "status":
    	taskObj.put("status", value);
    	break;
    case "Estimated_SP":
    	taskObj.put("Estimated_SP", value);
    	break;
    case "epic":
    	taskObj.put("epic", value);
    	break;
    }
    
    
    boolean success = dao.updateTask(taskId, taskObj.get("task").toString(), taskObj.get("status").toString(), taskObj.get("Estimated_SP").toString(), taskObj.get("epic").toString());

    if (success) {
        out.print("OK");
    } else {
        out.print("FAIL");
    } 

%>
