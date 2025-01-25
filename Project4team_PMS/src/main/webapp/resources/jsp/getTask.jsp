<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dao.TaskDAO,vo.TaskVo"%>

<%
TaskDAO taskDAO = new TaskDAO();
	ArrayList<TaskVo> tasks = taskDAO.getTasks();
	
	System.out.println("테이블로 부터 불러온 값");
	for (TaskVo task : tasks) {
		System.out.println(task);
	}
	System.out.println("--------------------------------");
	
	// 문자열 형식으로 데이터 반환
	StringBuilder responseString = new StringBuilder();
	for (TaskVo task : tasks) {
		responseString.append(task.getTask()).append("|").append(task.getName()).append("|").append(task.getStatus()).append("\n");
	}
	
	// 마지막 줄바꿈 제거 (선택 사항)
	if (responseString.length() > 0) {
		responseString.setLength(responseString.length() - 1);
	}
	
	System.out.println("테이블로 부터 불러온 값을 가공함\n" + responseString);
	
	response.setContentType("text/plain");
	response.getWriter().write(responseString.toString());
%>
