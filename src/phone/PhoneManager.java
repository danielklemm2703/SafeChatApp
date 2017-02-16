package phone;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.atomic.AtomicReference;

import javaslang.collection.HashSet;
import javaslang.control.Option;

import org.assertj.core.util.VisibleForTesting;

public final class PhoneManager {

    private static final AtomicReference<PhoneManager> _singletonHolder = new AtomicReference<PhoneManager>();

    public static final PhoneManager instance() {
        if (_singletonHolder.get() == null) {
            _singletonHolder.set(new PhoneManager());
        }
        return _singletonHolder.get();
    }

    /**
     * This method is used for better testing.
     * Do not use it in production code. 
     * @return resetted PhoneManager instance
     */
    @VisibleForTesting
    static final PhoneManager reset() {
        _singletonHolder.set(new PhoneManager());
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
        checkArgument(phoneNumber != null && !phoneNumber.isEmpty(), "Argument 'phoneNumber' must not be null or empty");
        checkArgument(sessionId != null && !sessionId.isEmpty(), "Argument 'sessionId' must not be null or empty");
        _registeredPhones = _registeredPhones.add(phoneNumber, sessionId);
        return _registeredPhones.sessionIds(phoneNumber)
                .orElse(Option.of(HashSet.<String> empty()))
                .get();
    }

    /**
     * removes a sessionId from the list of registered sessions of a phone
     * 
     * @param sessionId
     */
    public void removeSessionId(String sessionId) {
        checkArgument(sessionId != null && !sessionId.isEmpty(), "Argument 'sessionId' must not be null or empty");
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
        checkArgument(phoneNumber != null && !phoneNumber.isEmpty(), "Argument 'phoneNumber' must not be null or empty");
        return _registeredPhones.sessionIds(phoneNumber).getOrElse(HashSet.<String> empty());
    }
}
