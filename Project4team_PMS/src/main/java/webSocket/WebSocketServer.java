package webSocket;

import java.io.IOException;
import java.util.*;
import javax.websocket.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import dao.TaskDAO; // TaskDAO 가져오기

@javax.websocket.server.ServerEndpoint("/ws")
public class WebSocketServer {

    private static Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("새로운 연결: " + session.getId());
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("받은 메시지: " + message);
        
        try {
            JSONObject jsonMessage = (JSONObject) new org.json.simple.parser.JSONParser().parse(message);
            String type = (String) jsonMessage.get("type");
            String dashboardId = (String) jsonMessage.get("dashboardId");

            if ("update_request".equals(type)) {
                sendUpdatedTasks(dashboardId); // 모든 클라이언트에게 최신 작업 목록 전송
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("연결 종료: " + session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // ✅ 모든 클라이언트에게 최신 작업 목록 전송
    public static void sendUpdatedTasks(String dashboardId) {
        JSONArray taskList = new TaskDAO().getDashboardTasks(dashboardId); // 최신 작업 목록 가져오기

        JSONObject message = new JSONObject();
        message.put("type", "update");
        message.put("tasks", taskList);
        message.put("dashboardId", dashboardId);

        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message.toJSONString()); // JSON으로 작업 목록 전송
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
