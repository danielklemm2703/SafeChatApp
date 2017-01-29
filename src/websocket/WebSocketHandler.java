package websocket;

import java.io.StringReader;

import javaslang.collection.HashSet;
import javaslang.control.Try;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.Session;

import phone.PhoneManager;
import session.SessionHandler;
import util.JsonUtil;
import util.Unit;

public final class WebSocketHandler {

    public static void handle(String json, Session session) {
        // TODO better Json validation
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject jsonMessage = reader.readObject();

            if ("registerPhoneNumber".equals(jsonMessage.getString("action"))) {
                // business operation
                System.err.println("Try to register phone number");
                String phoneNumber = jsonMessage.getString("phoneNumber");
                HashSet<Session> registeredSessions = PhoneManager.instance().registerPhoneNumber(phoneNumber, session);
                System.err.println("Registered phone number: " + phoneNumber);
                System.err.println("It belongs currently to " + registeredSessions.size() + " session(s)");

                // send response
                JsonObject registeredPhoneNumber = JsonUtil.registeredPhoneNumber(phoneNumber);
                // TODO try to recover
                Try<Unit> sendToSessions = SessionHandler.instance().sendToSessions(registeredSessions, registeredPhoneNumber);
            }

            if ("sendMessageToNumber".equals(jsonMessage.getString("action"))) {
                // business operation
                System.err.println("Try to send message to number");
            }
        }
    }
}
