package action.request;


public enum RequestActions {
    
    REGISTER_PHONENUMBER("registerPhoneNumber"),
    SEND_MESSAGE("sendMessageToNumber"),
    UNKNOWN("unknown");

    public String _value;

    RequestActions(String action) {
        _value = action;
    }
}
