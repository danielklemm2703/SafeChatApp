package util;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

public final class JsonUtil {

    public static JsonObject error(String errorMessage) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "error")
                .add("message", errorMessage)
                .build();
    }

    public static JsonObject registeredPhoneNumber(String phoneNumber) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add("action", "registeredPhoneNumber")
                .add("phoneNumber", phoneNumber)
                .build();
    }
}
