package websocket;

import javaslang.collection.HashSet;
import javaslang.control.Try;

import javax.json.JsonObject;

import phone.PhoneManager;
import session.SessionHandler;
import util.JsonUtil;
import util.Unit;
import action.Action;
import action.RegisterPhoneNumber;
import action.SendMessage;

public final class WebSocketHandler {

    public static Try<Unit> handle(Action action) {
        if (action instanceof RegisterPhoneNumber) {
            RegisterPhoneNumber registerPhoneAction = (RegisterPhoneNumber) action;
            return registerPhoneNumber(registerPhoneAction);
        }
        if (action instanceof SendMessage) {
            SendMessage sendMessageAction = (SendMessage) action;
            return sendMessage(sendMessageAction);
        }
        return Try.failure(new UnsupportedOperationException("Could not find action to execute"));
    }

    private static Try<Unit> sendMessage(SendMessage sendMessageAction) {
        System.err.println("Try to send message to number");
        // TODO progress and send message
        return Try.success(Unit.VALUE);
    }

    private static Try<Unit> registerPhoneNumber(RegisterPhoneNumber registerPhoneAction) {
        System.err.println("Try to register phone number");
        HashSet<String> registeredSessions = PhoneManager.instance().registerPhoneNumber(registerPhoneAction.phoneNumber(), registerPhoneAction.sessionId());
        System.err.println("Registered phone number: " + registerPhoneAction.phoneNumber());
        System.err.println("It belongs currently to " + registeredSessions.size() + " session(s)");

        // send response
        JsonObject registeredPhoneNumber = JsonUtil.registeredPhoneNumber(registerPhoneAction.phoneNumber());
        return SessionHandler.instance().sendToSessions(registeredSessions, registeredPhoneNumber);
    }
}
