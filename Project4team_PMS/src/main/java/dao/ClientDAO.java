package dao;

import java.sql.*;
import java.text.ParseException;
import java.util.*;

import javax.naming.NamingException;

public class ClientDAO {
   String driver = "oracle.jdbc.driver.OracleDriver";
   String url = "jdbc:oracle:thin:@localhost:1521/orcl";
   String user = "c##apple";
   String password = "1111";

   private Connection con;
   private PreparedStatement stmt;
   private ResultSet rs;

   public ArrayList<String> list(String name) {

      ArrayList<String> str = new ArrayList<>();

      try {
         connDB();

         // SQL 쿼리에서 파라미터 바인딩
         String query = "SELECT * FROM Client WHERE name = ?";
         stmt = con.prepareStatement(query); // PreparedStatement 사용
         stmt.setString(1, name); // 첫 번째 파라미터에 name 값을 바인딩

         rs = stmt.executeQuery(); // 쿼리 실행

         while (rs.next()) {
            String irum = rs.getString("NAME");
            String email = rs.getString("EMAIL");
            String ps = rs.getString("PASSWORD");

            // name, email, password 값을 하나의 문자열로 결합하여 리스트에 추가
            String record = "Name: " + irum + ", Email: " + email + ", Password: " + ps;
            str.add(record);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return str; // 결과 반환
   }
   
   public boolean insert(String cEmail, String cName, String cPassword) throws NamingException, SQLException {
       PreparedStatement stmt = null;
       connDB();
       
       try {
    	   // 이메일 중복 확인
    	   String checkSql = "SELECT COUNT(*) FROM client WHERE email = ?";
           PreparedStatement checkStmt = con.prepareStatement(checkSql);
           checkStmt.setString(1, cEmail);
           rs = checkStmt.executeQuery();
           if (rs.next() && rs.getInt(1) > 0) {
               return false; // 이메일 중복
           }
    	   
    	   
    	   
    	   
    	   // 이메일 삽입
           String sql = "INSERT INTO client(email, name, password) VALUES(?, ?, ?)";
           
           
           stmt = con.prepareStatement(sql);
           stmt.setString(1, cEmail);
           stmt.setString(2, cName);
           stmt.setString(3, cPassword);
           
           
           
           int count = stmt.executeUpdate();
           return (count == 1) ? true : false;
           
       } finally {
           if (stmt != null) stmt.close(); 
           if (con != null) con.close();
       }
   }
   
   
   public int login(String cEmail, String cPassword) throws NamingException, SQLException, ParseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        connDB();
        
        try {
           connDB();

         // SQL 쿼리에서 파라미터 바인딩
         String query = "SELECT * FROM Client WHERE email = ?";
         stmt = con.prepareStatement(query); // PreparedStatement 사용
         stmt.setString(1, cEmail); // 첫 번째 파라미터에 name 값을 바인딩

         rs = stmt.executeQuery(); // 쿼리 실행
         if (!rs.next()) return 1;
         
         String name = rs.getString("NAME");
         String email = rs.getString("EMAIL");
         String password = rs.getString("PASSWORD");
         
         
         if (cPassword.equals(password)) return 2;
         
         return 0;
         
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (con != null) con.close();
        }
    }
   

   public void connDB() {
      try {
         Class.forName(driver); // JDBC 드라이버 로드
         System.out.println("jdbc driver loading success.");

         // 첫 번째 URL로 연결 시도
         try {
            con = DriverManager.getConnection(url, user, password); // DB 연결
            System.out.println("oracle connection success with URL: " + url);
         } catch (Exception e) {
            System.out.println("Connection failed with URL: " + url);
            System.out.println("Retrying with alternate URL...");

            // 두 번째 URL로 재시도
            String alternateUrl = "jdbc:oracle:thin:@localhost:1521/XE";
            con = DriverManager.getConnection(alternateUrl, user, password);
            System.out.println("oracle connection success with alternate URL: " + alternateUrl);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
}
