package action.response;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Failure implements ResponseAction {

    @Value.Default
    public String action() {
        return ResponseActions.FAILURE._value;
    }

    @Value.Parameter
    public abstract String message();

    public static ImmutableFailure.Builder builder() {
        return ImmutableFailure.builder();
    }
}
