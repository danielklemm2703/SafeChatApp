package session;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.control.Try;

import javax.json.JsonObject;
import javax.websocket.Session;

import util.Unit;

public class SessionHandler {

    private static final AtomicReference<SessionHandler> _singletonHolder = new AtomicReference<SessionHandler>();

    public static final SessionHandler instance() {
        if (_singletonHolder.get() == null) {
            _singletonHolder.set(new SessionHandler());
        }
        return _singletonHolder.get();
    }

    private HashMap<String, Session> _registeredSessions;

    private SessionHandler() {
        _registeredSessions = HashMap.<String, Session> empty();
    }

    public void addSession(Session session) {
        System.out.println("addSession: " + session.getId());
        _registeredSessions = _registeredSessions.put(session.getId(), session);
    }

    public void removeSession(Session session) {
        System.out.println("removeSession: " + session.getId());
        _registeredSessions = _registeredSessions.remove(session.getId());
    }

    public Try<Unit> sendToSession(Session session, JsonObject json) {
        try {
            session.getBasicRemote().sendText(json.toString());
            System.err.println("Successfully sent to session " + session.getId());
            return Try.success(Unit.VALUE);
        } catch (IOException ex) {
            return Try.failure(ex);
        }
    }

    public Try<Unit> sendToSessions(HashSet<Session> registeredSessions, JsonObject json) {
        System.err.println("Try sending to multiple sessions (#" + registeredSessions.size() + ")");
        HashSet<Try<Unit>> failures = registeredSessions
                .map(t -> sendToSession(t, json))
                .filter(t -> t.isFailure());
        if(failures.isEmpty()){
            return Try.success(Unit.VALUE);
        }
        return failures.head();
    }

    public boolean verifiedSession(Session session) {
        return _registeredSessions.get(session.getId()).isDefined();
    }
}