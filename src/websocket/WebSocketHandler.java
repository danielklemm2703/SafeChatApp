package websocket;

import javaslang.collection.HashSet;
import javaslang.control.Try;
import phone.PhoneManager;
import action.request.RegisterPhoneNumber;
import action.request.RequestAction;
import action.request.SendMessage;
import action.response.RegisteredPhoneNumber;
import action.response.ResponseAction;
import action.response.SentMessage;

public final class WebSocketHandler {

    /**
     * Executes the business logic according to the given action
     * 
     * @param action
     * @param phoneManager
     * @return response of the execution
     */
    public static Try<Response> handleRequest(RequestAction action, PhoneManager phoneManager) {
        if (action instanceof RegisterPhoneNumber) {
            return registerPhoneNumber((RegisterPhoneNumber) action, phoneManager);
        }
        if (action instanceof SendMessage) {
            return sendMessage((SendMessage) action, phoneManager);
        }
        return Try.failure(new UnsupportedOperationException("Could not find action to execute: " + action.action()));
    }

    private static Try<Response> sendMessage(SendMessage sendMessageAction, PhoneManager phoneManager) {
        String message = sendMessageAction.message();
        System.err.println("Try to send message '" + message + "' to number");
        String senderNumber = sendMessageAction.senderNumber();
        String receiverNumber = sendMessageAction.receiverNumber();
        HashSet<String> senderSessionIds = phoneManager.sessionIdsOf(senderNumber);
        HashSet<String> receiverSessionIds = phoneManager.sessionIdsOf(receiverNumber);
        System.err.println("found " + senderSessionIds.size() + " sessions for sender number " + senderNumber);
        System.err.println("found " + receiverSessionIds.size() + " sessions for receiver number " + receiverNumber);
        HashSet<String> sessionIdsToNotify = senderSessionIds.addAll(receiverSessionIds);

        // TODO build message cache here

        // send response
        ResponseAction responseAction = SentMessage.builder()
                .message(message)
                .senderNumber(senderNumber)
                .receiverNumber(receiverNumber)
                .build();
        return Try.success(Response.of(responseAction, sessionIdsToNotify));
    }

    private static Try<Response> registerPhoneNumber(RegisterPhoneNumber registerPhoneAction, PhoneManager phoneManager) {
        System.err.println("Try to register phone number");
        HashSet<String> registeredSessionIds = phoneManager.registerPhoneNumber(registerPhoneAction.phoneNumber(), registerPhoneAction.sessionId().get());
        System.err.println("Registered phone number: " + registerPhoneAction.phoneNumber());
        System.err.println("It belongs currently to " + registeredSessionIds.size() + " session(s)");

        // send response
        ResponseAction responseAction = RegisteredPhoneNumber.builder()
                .phoneNumber(registerPhoneAction.phoneNumber())
                .build();
        return Try.success(Response.of(responseAction, registeredSessionIds));
    }
}
