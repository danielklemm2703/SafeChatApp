package websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import session.SessionHandler;
import util.JsonUtil;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    @OnClose
    public void close(Session session) {
        SessionHandler.instance().removeSession(session);
    }

    @OnMessage
    public void handleMessage(String json, Session session) {
        // verification
        if (!SessionHandler.instance().verifiedSession(session)) {
            JsonObject error = JsonUtil.error("No verified Session");
            SessionHandler.instance().sendToSession(session, error);
            System.err.println("Could not verify Session: " + session.getId());
        }
        System.err.println("Verified Session: " + session.getId());

        // business logic execution
        System.err.println("Try to find suitable action");
        WebSocketHandler.handle(json, session);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnOpen
    public void open(Session session) {
        SessionHandler.instance().addSession(session);
    }
}