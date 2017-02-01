package action.request;

import java.util.Optional;

import org.immutables.value.Value;

public interface RequestAction {

    public Optional<String> sessionId();

    @Value.Parameter
    public String action();
}
