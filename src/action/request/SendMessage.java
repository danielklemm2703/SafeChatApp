package action.request;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class SendMessage implements RequestAction {

    @Value.Parameter
    public abstract String senderNumber();

    @Value.Parameter
    public abstract String receiverNumber();

    @Value.Parameter
    public abstract String message();

    public static ImmutableSendMessage.Builder builder() {
        return ImmutableSendMessage.builder();
    }
}
