package action;

import javaslang.control.Try;

import javax.json.JsonObject;

public abstract class Action {

    private final String _sessionId;

    public Action(String sessionId) {
        _sessionId = sessionId;
    }

    public String sessionId() {
        return _sessionId;
    }

    public abstract Try<? extends Action> fromJson(JsonObject json);
}
