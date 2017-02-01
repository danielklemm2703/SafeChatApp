package action.response;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class RegisteredPhoneNumber implements ResponseAction {

    @Value.Default
    public String action() {
        return ResponseActions.REGISTERED_PHONENUMBER._value;
    }

    @Value.Parameter
    public abstract String phoneNumber();

    public static ImmutableRegisteredPhoneNumber.Builder builder() {
        return ImmutableRegisteredPhoneNumber.builder();
    }
}
