<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dao.ClientDAO"%>
<%
   request.setCharacterEncoding("utf-8");
   
   String cEmail = request.getParameter("Email");
   String cName = request.getParameter("Name");
   String cPassword = request.getParameter("Password");
   
   System.out.println(cEmail + cName + cPassword);
   
   ClientDAO dao = new ClientDAO();
   boolean code = dao.insert(cEmail, cName, cPassword);

   if (code){      // 회원가입 성공
//       session.setAttribute("id", cid);
      out.println("OK");
   } else {				// 회원가입 실패
	   out.println("NE");
   }
%>