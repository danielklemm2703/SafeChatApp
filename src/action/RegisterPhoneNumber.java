package action;

import static verify.Verify.verify;
import javaslang.control.Try;

import javax.json.JsonObject;

public final class RegisterPhoneNumber extends Action {

    public static final String ACTION_NAME = "registerPhoneNumber";
    private String _phoneNumber;

    public RegisterPhoneNumber(String sessionId) {
        super(sessionId);
    }

    public String phoneNumber() {
        return _phoneNumber;
    }

    @Override
    public Try<RegisterPhoneNumber> fromJson(JsonObject json) {
        try {
            verify(ACTION_NAME.equals(json.getString("action")));
            _phoneNumber = json.getString("phoneNumber");
            return Try.success(this);
        } catch (Exception e) {
            return Try.failure(e);
        }
    }
}
