package action.request;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class RegisterPhoneNumber implements RequestAction {

    @Value.Parameter
    public abstract String phoneNumber();

    public static ImmutableRegisterPhoneNumber.Builder builder() {
        return ImmutableRegisterPhoneNumber.builder();
    }
}
