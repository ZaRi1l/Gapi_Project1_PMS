package webSocket;

import java.io.IOException;
import java.util.*;
import javax.websocket.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import dao.*; // TaskDAO 가져오기

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
            String dashboardId = String.valueOf(jsonMessage.get("dashboardId"));
            String customerId = (String) jsonMessage.get("customerId");  // 탈퇴할 사용자 ID
            
            // 1. 대시보드 변경 요청 처리
            if ("update_dashboard".equals(type)) {
                sessionDashboardMap.put(session, dashboardId);  // 대시보드 ID 갱신
                sendUpdatedTasksToAll(dashboardId);
            }
		
	      // 2 대시보드에 변경사항이 있을 때 모든 관련 세션에 전송
            else if ("update_request".equals(type)) {
                sendUpdatedTasksToAll(dashboardId);
            }

            // 3. 회원 탈퇴 처리
            else if ("delete_client".equals(type)) {
                sendUpdatedTasksToAll(dashboardId);  // 삭제된 작업 후 대시보드 작업 목록 갱신
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

    // 🔹 특정 대시보드의 모든 구독자에게 업데이트 전송
    private void sendUpdatedTasksToAll(String dashboardId) {
    	
    	System.out.println("갱신할 대시보드 : " + dashboardId);
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
