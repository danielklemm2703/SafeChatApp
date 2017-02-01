package websocket;

import javaslang.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import session.SessionHandler;
import util.RequestParser;
import util.Unit;
import action.request.RequestAction;
import action.response.Failure;

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
            Failure error = Failure.builder().message("No verified Session").build();
            SessionHandler.instance().sendToSession(session.getId(), error);
            System.err.println("Could not verify Session: " + session.getId());
        }
        System.err.println("Verified Session: " + session.getId());

        // parsing
        System.err.println("Try to parse action");
        Try<? extends RequestAction> action = RequestParser.parseAction(json, session.getId());
        if (action.isFailure()) {
            Failure error = Failure.builder().message("Could not parse action").build();
            SessionHandler.instance().sendToSession(session.getId(), error);
            System.err.println("Could not parse action: " + json);
        }

        // business logic execution
        Try<Unit> handle = WebSocketHandler.handleRequest(action.get());
        if (handle.isFailure()) {
            Failure error = Failure.builder().message("Could not execute action").build();
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