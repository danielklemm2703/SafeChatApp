package util;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequestParser {

    public static Try<? extends RequestAction> parseAction(String jsonString, String sessionId) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            JsonObject json = reader.readObject();
            String action = json.getString("action");
            Gson builder = new GsonBuilder().create();
            if (action.equals(RequestActions.REGISTER_PHONENUMBER._value)) {
                RegisterPhoneNumber registerPhoneNumber = builder.fromJson(jsonString, ImmutableRegisterPhoneNumber.class).withSessionId(sessionId);
                return Try.success(registerPhoneNumber);
            }
            if (action.equals(RequestActions.SEND_MESSAGE._value)) {
                SendMessage sendMessage = builder.fromJson(jsonString, ImmutableSendMessage.class).withSessionId(sessionId);
                return Try.success(sendMessage);
            }
            throw new UnsupportedOperationException("No action for " + action + " defined");
        } catch (Exception e) {
            return Try.failure(e);
        }
    }
}
