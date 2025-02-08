<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*, java.sql.*, dao.*, org.json.simple.*" %>

<%
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String customerId = request.getParameter("customerId");
    String newName = request.getParameter("newName");
    String newPassword = request.getParameter("newPassword");

    JSONObject jsonResponse = new JSONObject();

    if (customerId == null || customerId.isEmpty()) {
        jsonResponse.put("status", "error");
        jsonResponse.put("message", "회원 ID가 없습니다.");
        out.print(jsonResponse.toJSONString());
        return;
    }

    ClientDAO dao = new ClientDAO();
    
    int result = dao.updateUserInfo(customerId, newName, newPassword);
    
    if (result == 0) {
        jsonResponse.put("status", "success");
    } else {
        jsonResponse.put("status", "error");
    }
    
    out.print(jsonResponse.toJSONString());
%>