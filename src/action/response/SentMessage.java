package action.response;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class SentMessage implements ResponseAction {

    @Value.Default
    public String action() {
        return ResponseActions.SENT_MESSAGE._value;
    }

    @Value.Parameter
    public abstract String senderNumber();

    @Value.Parameter
    public abstract String receiverNumber();

    @Value.Parameter
    public abstract String message();

    public static ImmutableSentMessage.Builder builder() {
        return ImmutableSentMessage.builder();
    }
}
