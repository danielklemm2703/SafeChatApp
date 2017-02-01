package websocket;

import javaslang.collection.HashSet;
import action.response.Failure;
import action.response.ResponseAction;

public class Response {

    private final HashSet<String> _sessions;
    private final ResponseAction _action;

    private Response(ResponseAction responseAction, HashSet<String> sessionsToNotify) {
        _sessions = sessionsToNotify;
        _action = responseAction;
    }

    public ResponseAction responseAction() {
        return _action;
    }

    public HashSet<String> sessionsToNotify() {
        return _sessions;
    }

    public static Response of(ResponseAction responseAction, HashSet<String> sessionsToNotify) {
        return new Response(responseAction, sessionsToNotify);
    }

    public static Response failure(String errorMessage, HashSet<String> sessionsToNotify) {
        Failure error = Failure.builder()
                .message(errorMessage)
                .build();
        return new Response(error, sessionsToNotify);
    }
}
