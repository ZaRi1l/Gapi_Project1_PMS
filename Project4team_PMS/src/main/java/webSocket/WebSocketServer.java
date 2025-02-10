package webSocket;

import java.io.IOException;
import java.util.*;
import javax.websocket.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import dao.TaskDAO; // TaskDAO 가져오기

@javax.websocket.server.ServerEndpoint("/ws")
public class WebSocketServer {
    private static Map<Session, String> sessionDashboardMap = new HashMap<>();
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
            String dashboardId = String.valueOf(jsonMessage.get("dashboardId")); // 🔥 오류 방지: Long → String 변환

            // 🔹 클라이언트가 새로운 대시보드 ID를 요청하면 업데이트
            if ("update_dashboard".equals(type)) {
                sessionDashboardMap.put(session, dashboardId);
                sendUpdatedTasks(session, dashboardId);
            }
            // 🔹 대시보드에 변경사항이 있을 때 모든 관련 세션에 전송
            else if ("update_request".equals(type)) {
                sendUpdatedTasksToAll(dashboardId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("연결 종료: " + session.getId());
        sessions.remove(session);
        sessionDashboardMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // 🔹 특정 세션에만 업데이트 전송
    private void sendUpdatedTasks(Session session, String dashboardId) {
        JSONArray taskList = new TaskDAO().getDashboardTasks(dashboardId);

        JSONObject message = new JSONObject();
        message.put("type", "update");
        message.put("tasks", taskList);
        message.put("dashboardId", dashboardId);

        try {
            session.getBasicRemote().sendText(message.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 특정 대시보드의 모든 구독자에게 업데이트 전송
    private void sendUpdatedTasksToAll(String dashboardId) {
        JSONArray taskList = new TaskDAO().getDashboardTasks(dashboardId);

        JSONObject message = new JSONObject();
        message.put("type", "update");
        message.put("tasks", taskList);
        message.put("dashboardId", dashboardId);

        for (Session session : sessions) {
            if (dashboardId.equals(sessionDashboardMap.get(session))) {
                try {
                    session.getBasicRemote().sendText(message.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
