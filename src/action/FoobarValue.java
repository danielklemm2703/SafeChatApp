package action;

import java.util.List;
import java.util.Set;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class FoobarValue {
    public abstract int foo();

    public abstract String bar();

    public abstract List<Integer> business();

    public abstract Set<Long> cruise();
}
