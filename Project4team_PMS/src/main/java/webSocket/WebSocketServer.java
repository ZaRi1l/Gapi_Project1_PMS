package webSocket;

import java.io.IOException;
import java.util.*;

import javax.websocket.*;
import javax.websocket.Session;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

@javax.websocket.server.ServerEndpoint("/ws")
public class WebSocketServer {

    // 연결된 모든 세션을 저장하는 Set
    private static Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New connection: " + session.getId());
		sessions.add(session);  // 새로 연결된 세션 추가
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message: " + message);
        
        // JSON 파싱 후, 메시지 타입에 따라 처리
        try {
            // JSONParser로 메시지 파싱
            JSONParser parser = new JSONParser();
            JSONObject jsonMessage = (JSONObject) parser.parse(message);  // message를 JSONObject로 변환
            
            String type = (String) jsonMessage.get("type");

            // 'updateTasks'나 'deleteTask' 타입 메시지를 처리
            if ("updateTasks".equals(type)) {
                notifyClients("새로운 작업이 수정되었습니다: " + jsonMessage.get("task"));
            } else if ("deleteTask".equals(type)) {
                notifyClients("작업이 삭제되었습니다: " + jsonMessage.get("taskId"));
            } else if ("addTask".equals(type)) {
                notifyClients("새로운 작업이 추가되었습니다: " + jsonMessage.get("task"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
        sessions.remove(session);  // 연결이 종료된 세션 제거
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    public static void notifyClients(String taskName) {
        JSONObject message = new JSONObject();
        message.put("type", "addTask");
        message.put("task", taskName);

        // 모든 클라이언트에게 JSON 메시지 전송
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message.toJSONString());  // JSON 형식으로 메시지 전송
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}