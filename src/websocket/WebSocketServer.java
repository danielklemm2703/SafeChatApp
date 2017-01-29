package websocket;

import javaslang.control.Try;

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
import util.Parser;
import util.Unit;
import action.Action;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    @OnClose
    public void close(Session session) {
        SessionHandler.instance().removeSession(session.getId());
    }

    @OnMessage
    public void handleMessage(String json, Session session) {
        // verification
        if (!SessionHandler.instance().verifiedSession(session.getId())) {
            JsonObject error = JsonUtil.error("No verified Session");
            SessionHandler.instance().sendToSession(session.getId(), error);
            System.err.println("Could not verify Session: " + session.getId());
        }
        System.err.println("Verified Session: " + session.getId());

        // parsing
        System.err.println("Try to parse action");
        Try<? extends Action> action = Parser.parseAction(json, session.getId());
        if (action.isFailure()) {
            JsonObject error = JsonUtil.error("Could not parse action");
            SessionHandler.instance().sendToSession(session.getId(), error);
            System.err.println("Could not parse action: " + json);
        }

        // business logic execution
        Try<Unit> handle = WebSocketHandler.handle(action.get());
        if (handle.isFailure()) {
            JsonObject error = JsonUtil.error("Could not execute action");
            SessionHandler.instance().sendToSession(session.getId(), error);
            System.err.println("Could not execute action: " + action.get());
        }
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