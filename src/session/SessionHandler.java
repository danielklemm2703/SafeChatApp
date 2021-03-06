package session;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.control.Option;
import javaslang.control.Try;

import javax.websocket.Session;

import util.Unit;
import websocket.Response;
import action.response.ResponseAction;

import com.google.gson.GsonBuilder;

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

    /**
     * Adds a session to the list of known sessions
     * 
     * @param session
     */
    public void addSession(Session session) {
        System.out.println("addSession: " + session.getId());
        _registeredSessions = _registeredSessions.put(session.getId(), session);
    }

    /**
     * Removes a session by its id from the known sessions
     * 
     * @param sessionId
     */
    public void removeSession(String sessionId) {
        System.out.println("removeSession: " + sessionId);
        _registeredSessions = _registeredSessions.remove(sessionId);
    }

    /**
     * Sends a response object to all given sessions
     * 
     * @param response
     * @return Try.success if all sessions are available and could be reached, otherwise returns the
     *         first Try.failure that occurred
     */
    public Try<Unit> sendResponse(Response response) {
        System.err.println("Try sending to multiple sessions (#" + response.sessionsToNotify().size() + ")");
        HashSet<Try<Unit>> failures = response.sessionsToNotify()
                .map(t -> sendToSession(t, response.responseAction()))
                .filter(t -> t.isFailure());
        if (failures.isEmpty()) {
            return Try.success(Unit.VALUE);
        }
        return failures.head();
    }

    private Try<Unit> sendToSession(String sessionId, ResponseAction action) {
        try {
            Option<Session> registeredSession = _registeredSessions.get(sessionId);
            if (registeredSession.isDefined()) {
                String json = new GsonBuilder().create().toJson(action);
                registeredSession.get().getBasicRemote().sendText(json.toString());
                System.err.println("Successfully sent to session " + sessionId);
                return Try.success(Unit.VALUE);
            }
            return Try.failure(new IllegalStateException("Session " + sessionId + " not found"));
        } catch (IllegalStateException e1) {
            if (e1.getMessage().startsWith("The connection has been closed")) {
                System.err.println("Removed this outdated session");
                removeSession(sessionId);
                return Try.success(Unit.VALUE);
            }
            return Try.failure(e1);
        } catch (IOException e2) {
            removeSession(sessionId);
            return Try.failure(e2);
        }
    }

    /**
     * Checks if a certain session is known by the given sessionId
     * 
     * @param sessionId
     * @return Try.success, if the session is known, Try.failure else
     */
    public Try<Unit> verifiedSession(String sessionId) {
        return _registeredSessions.get(sessionId).isDefined() ?
                Try.success(Unit.VALUE) :
                Try.failure(new IllegalStateException("no verified Session"));
    }
}