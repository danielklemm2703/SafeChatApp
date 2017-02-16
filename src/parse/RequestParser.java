package parse;

import java.io.StringReader;

import javaslang.control.Try;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import action.request.ImmutableRegisterPhoneNumber;
import action.request.ImmutableSendMessage;
import action.request.RegisterPhoneNumber;
import action.request.RequestAction;
import action.request.RequestActions;
import action.request.SendMessage;

import com.google.gson.GsonBuilder;

public class RequestParser {

    public static Try<? extends RequestAction> parseAction(String jsonString, String sessionId) {
        return parseActionType(jsonString)
                .flatMap(action -> findRequestAction(action, jsonString, sessionId));
    }

    private static Try<String> parseActionType(String jsonString) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            JsonObject json = reader.readObject();
            return Try.success(json.getString("action"));
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    private static Try<? extends RequestAction> findRequestAction(String action, String jsonString, String sessionId) {
        if (action.equals(RequestActions.REGISTER_PHONENUMBER._value)) {
            RegisterPhoneNumber registerPhoneNumber = new GsonBuilder()
                    .create()
                    .fromJson(jsonString, ImmutableRegisterPhoneNumber.class)
                    .withSessionId(sessionId);
            return Try.success(registerPhoneNumber);
        }
        if (action.equals(RequestActions.SEND_MESSAGE._value)) {
            SendMessage sendMessage = new GsonBuilder()
                    .create()
                    .fromJson(jsonString, ImmutableSendMessage.class)
                    .withSessionId(sessionId);
            return Try.success(sendMessage);
        }
        return Try.failure(new IllegalStateException("unknown action: " + action));
    }
}
