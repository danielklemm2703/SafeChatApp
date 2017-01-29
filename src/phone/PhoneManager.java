package phone;

import java.util.concurrent.atomic.AtomicReference;

import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.control.Option;

public class PhoneManager {

    private static final AtomicReference<PhoneManager> _singletonHolder = new AtomicReference<PhoneManager>();

    public static final PhoneManager instance() {
        if (_singletonHolder.get() == null) {
            _singletonHolder.set(new PhoneManager());
        }
        return _singletonHolder.get();
    }

    /**
     * maps phone number to session id's
     */
    private HashMap<String, HashSet<String>> _phoneMap;

    private PhoneManager() {
        _phoneMap = HashMap.<String, HashSet<String>> empty();
    }

    /**
     * registers a number to a sessionId, returns all sessionIds that belong to a given number
     * 
     * @param phoneNumber
     * @param sessionId
     * @return a set of registered sessionIds to a single number
     */
    public HashSet<String> registerPhoneNumber(String phoneNumber, String sessionId) {
        // TODO what happens if the incoming session already has a phone number registered?
        HashSet<String> sessionIds = HashSet.of(sessionId);
        if (_phoneMap.keySet().contains(phoneNumber)) {
            sessionIds = _phoneMap.get(phoneNumber).get().add(sessionId);
        }
        _phoneMap = _phoneMap.put(phoneNumber, sessionIds);
        return _phoneMap.get(phoneNumber)
                .orElse(Option.of(HashSet.<String> empty()))
                .get();
    }
}
