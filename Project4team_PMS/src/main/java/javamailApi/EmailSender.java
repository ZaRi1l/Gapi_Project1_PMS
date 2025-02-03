package javamailApi;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
	
	public static void sendInvitationEmail(String customerId, String recipientEmail, String userName) {
        final String username = customerId; // 발신 이메일 (SMTP 서버에서 설정)
        final String password = "vfehgoglioazgnjo"; // 발신 이메일 비밀번호

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");  // SMTP 서버 (예: Gmail)
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // TLS 암호화

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("PMS Project 동료 초대");
            message.setText("안녕하세요, " + userName + "님!\n\n" +
                            "PMS 프로젝트에 동료로 초대되었습니다. 프로젝트에 참여하여 함께 작업을 진행해 주세요.\n\n" +
                            "감사합니다.\nPMS Project Team");

            Transport.send(message);

            System.out.println("초대 이메일이 전송되었습니다.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
