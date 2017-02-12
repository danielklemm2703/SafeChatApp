package websocket;

import java.util.function.Consumer;

import javaslang.collection.HashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import phone.PhoneManager;
import session.SessionHandler;
import util.RequestParser;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    private SessionHandler _sessionHandler = SessionHandler.instance();

    private static final Consumer<Throwable> sendError(Session session, SessionHandler sessionHandler) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable t) {
                Response response = Response.failure(t.getMessage(), HashSet.of(session.getId()));
                sessionHandler.sendResponse(response);
                System.err.println("error sending response, reason: " + t.getMessage());
                t.getCause().printStackTrace();
            }
        };
    }

    @OnClose
    public void close(Session session) {
        // TODO this should trigger an update in the phoneManager to avoid session not found
        // failures
        _sessionHandler.removeSession(session.getId());
    }

    @OnMessage
    public void handleMessage(String json, Session session) {
        _sessionHandler.verifiedSession(session.getId())
                .flatMap(t -> RequestParser.parseAction(json, session.getId()))
                .flatMap(action -> WebSocketHandler.handleRequest(action, PhoneManager.instance()))
                .flatMap(response -> _sessionHandler.sendResponse(response))
                .onSuccess(t -> System.err.println("successfully sent response"))
                .onFailure(sendError(session, _sessionHandler));
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnOpen
    public void open(Session session) {
        _sessionHandler.addSession(session);
    }
}