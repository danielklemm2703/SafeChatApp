package websocket;

import javaslang.collection.HashSet;
import javaslang.control.Try;

import javax.resource.spi.IllegalStateException;

import phone.PhoneManager;
import action.request.RegisterPhoneNumber;
import action.request.RequestAction;
import action.request.SendMessage;
import action.response.RegisteredPhoneNumber;
import action.response.ResponseAction;

public final class WebSocketHandler {

    public static Try<Response> handleRequest(RequestAction action) {
        if (action instanceof RegisterPhoneNumber) {
            return registerPhoneNumber((RegisterPhoneNumber) action);
        }
        if (action instanceof SendMessage) {
            return sendMessage((SendMessage) action);
        }
        return Try.failure(new UnsupportedOperationException("Could not find action to execute: " + action.action()));
    }

    private static Try<Response> sendMessage(SendMessage sendMessageAction) {
        System.err.println("Try to send message to number");
        // TODO progress and send message
        return Try.failure(new IllegalStateException("Not implemented yet"));
    }

    private static Try<Response> registerPhoneNumber(RegisterPhoneNumber registerPhoneAction) {
        System.err.println("Try to register phone number");
        HashSet<String> registeredSessions = PhoneManager.instance().registerPhoneNumber(registerPhoneAction.phoneNumber(), registerPhoneAction.sessionId().get());
        System.err.println("Registered phone number: " + registerPhoneAction.phoneNumber());
        System.err.println("It belongs currently to " + registeredSessions.size() + " session(s)");

        // send response
        ResponseAction responseAction = RegisteredPhoneNumber.builder().phoneNumber(registerPhoneAction.phoneNumber()).build();
        return Try.success(Response.of(responseAction, registeredSessions));
    }
}
