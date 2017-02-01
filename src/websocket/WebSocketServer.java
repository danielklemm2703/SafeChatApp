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

import session.SessionHandler;
import util.RequestParser;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {

    private static final Consumer<Throwable> sendError(Session session) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable t) {
                Response response = Response.failure(t.getMessage(), HashSet.of(session.getId()));
                SessionHandler.instance().sendResponse(response);
                System.err.println("error sending response, reason: " + t.getMessage());
                t.getCause().printStackTrace();
            }
        };
    }

    @OnClose
    public void close(Session session) {
        // TODO should this trigger an update in the phoneManager?
        SessionHandler.instance().removeSession(session.getId());
    }

    @OnMessage
    public void handleMessage(String json, Session session) {
        SessionHandler.instance().verifiedSession(session.getId())
                .flatMap(t -> RequestParser.parseAction(json, session.getId()))
                .flatMap(action -> WebSocketHandler.handleRequest(action))
                .flatMap(response -> SessionHandler.instance().sendResponse(response))
                .onSuccess(t -> System.err.println("successfully sent response"))
                .onFailure(sendError(session));
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