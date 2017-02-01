package action.response;

public enum ResponseActions {

    FAILURE("error"),
    REGISTERED_PHONENUMBER("registeredPhoneNumber");

    public String _value;

    ResponseActions(String action) {
        _value = action;
    }
}
