<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.ClientDAO"%>
<%
   request.setCharacterEncoding("utf-8");
   
   String cEmail = request.getParameter("Email");
   String cPassword = request.getParameter("Password");
   
   ClientDAO dao = new ClientDAO();
   int code = dao.login(cEmail, cPassword);

   if (code == 1) {   // 이메일 없음 % 이메일 값이 넘어옴
      out.println("NE");
   } 
   if (code == 0) {   // 비번 틀림
      out.println("PE");
   } 
   if (code == 2){      // 로그인 성공
//       session.setAttribute("id", cid);
      out.println("OK");
   }
%>