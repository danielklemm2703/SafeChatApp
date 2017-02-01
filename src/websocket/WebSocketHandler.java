package websocket;

import javaslang.collection.HashSet;
import javaslang.control.Try;
import phone.PhoneManager;
import session.SessionHandler;
import util.Unit;
import action.request.RegisterPhoneNumber;
import action.request.RequestAction;
import action.request.SendMessage;
import action.response.ImmutableRegisteredPhoneNumber;
import action.response.RegisteredPhoneNumber;

public final class WebSocketHandler {

    public static Try<Unit> handleRequest(RequestAction action) {
        if (action instanceof RegisterPhoneNumber) {
            return registerPhoneNumber((RegisterPhoneNumber) action);
        }
        if (action instanceof SendMessage) {
            return sendMessage((SendMessage) action);
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
        HashSet<String> registeredSessions = PhoneManager.instance().registerPhoneNumber(registerPhoneAction.phoneNumber(), registerPhoneAction.sessionId().get());
        System.err.println("Registered phone number: " + registerPhoneAction.phoneNumber());
        System.err.println("It belongs currently to " + registeredSessions.size() + " session(s)");

        // send response
        ImmutableRegisteredPhoneNumber responseAction = RegisteredPhoneNumber.builder().phoneNumber(registerPhoneAction.phoneNumber()).build();
        return SessionHandler.instance().sendToSessions(registeredSessions, responseAction);
    }
}
