package action.response;

import org.immutables.value.Value;

public interface ResponseAction {

    @Value.Parameter
    public String action();
}
