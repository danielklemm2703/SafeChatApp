package action.request;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class UnknownRequestAction implements RequestAction {

    public static ImmutableUnknownRequestAction.Builder builder() {
        return ImmutableUnknownRequestAction.builder();
    }
}
