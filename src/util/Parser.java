package util;

import java.io.StringReader;

import javaslang.control.Try;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import action.Action;
import action.RegisterPhoneNumber;
import action.SendMessage;

public class Parser {

    public static Try<? extends Action> parseAction(String jsonString, String sessionId) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            JsonObject json = reader.readObject();
            String action = json.getString("action");
            if (action.equals(RegisterPhoneNumber.ACTION_NAME)) {
                return new RegisterPhoneNumber(sessionId).fromJson(json);
            }
            if (action.equals(SendMessage.ACTION_NAME)) {
                return new SendMessage(sessionId).fromJson(json);
            }
            throw new UnsupportedOperationException("No action for " + action + " defined");
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

}
