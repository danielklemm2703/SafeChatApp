package phone;

import java.util.concurrent.atomic.AtomicReference;

import javaslang.collection.HashSet;
import javaslang.control.Option;

public final class PhoneManager {

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
    private PhoneSessionMap _registeredPhones;

    private PhoneManager() {
        _registeredPhones = PhoneSessionMap.empty();
    }

    /**
     * registers a number to a sessionId, returns all sessionIds that belong to a given number
     * 
     * @param phoneNumber
     * @param sessionId
     * @return a set of registered sessionIds to a single number
     */
    public HashSet<String> registerPhoneNumber(String phoneNumber, String sessionId) {
        _registeredPhones = _registeredPhones.add(phoneNumber, sessionId);
        return _registeredPhones.getSessions(phoneNumber)
                .orElse(Option.of(HashSet.<String> empty()))
                .get();
    }

    /**
     * removes a sessionId from the list of registered sessions of a phone
     * 
     * @param sessionId
     */
    public void removeSessionId(String sessionId) {
        _registeredPhones = _registeredPhones.remove(sessionId);
    }

    /**
     * returns all registered sessionIds belonging to a given phone number or an empty hash set if
     * either the phone number is not registered or has no sessionId
     * 
     * @param phoneNumber
     * @return all registered sessionIds belonging to a given phone number
     */
    public HashSet<String> sessionIdsOf(String phoneNumber) {
        return _registeredPhones.getSessions(phoneNumber).getOrElse(HashSet.<String> empty());
    }
}
