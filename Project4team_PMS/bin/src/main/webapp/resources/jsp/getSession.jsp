<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String customerId = (String) session.getAttribute("customerId"); // 세션에서 customerId 가져오기
    if (customerId == null) {
        out.print("noting"); // 세션이 없을 경우 처리
    } else {
        out.print(customerId);
    }
%>