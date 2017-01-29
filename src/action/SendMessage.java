package action;

import static verify.Verify.verify;
import javaslang.control.Try;

import javax.json.JsonObject;

public final class SendMessage extends Action {

    public static final String ACTION_NAME = "sendMessageToNumber";
    private String _senderNumber;
    private String _receiverNumber;
    private String _message;

    public SendMessage(String sessionId) {
        super(sessionId);
    }

    public String senderNumber() {
        return _senderNumber;
    }

    public String receiverNumber() {
        return _receiverNumber;
    }

    public String message() {
        return _message;
    }

    @Override
    public Try<SendMessage> fromJson(JsonObject json) {
        try {
            verify(ACTION_NAME.equals(json.getString("action")));
            _receiverNumber = json.getString("receiver");
            _senderNumber = json.getString("sender");
            _message = json.getString("message");
            return Try.success(this);
        } catch (Exception e) {
            return Try.failure(e);
        }
    }
}
