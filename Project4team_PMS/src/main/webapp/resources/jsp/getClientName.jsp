<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="dao.*" %>
<%@ page import="org.json.simple.*" %>

<%
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String customerId = (String) session.getAttribute("customerId"); 
    if (customerId == null) {
        customerId = "로그인이 필요합니다.";
    }

    String userName = (new ClientDAO()).getUserNameByCustomerId(customerId); 
    if (userName == null) {
        userName = "이름 정보 없음";
    }

    JSONObject userInfo = new JSONObject();
    userInfo.put("customerId", customerId);
    userInfo.put("userName", userName);

    out.print(userInfo.toJSONString());
%>
