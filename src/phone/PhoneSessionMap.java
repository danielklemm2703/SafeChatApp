package phone;

import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.collection.Set;
import javaslang.control.Option;

/**
 * Created as delegate of {@link PhoneManager} to hold dedicated maps for registered sessionIds and
 * phone numbers. It should take away the complexity of handling two separate maps from
 * {@link PhoneManager}. <br>
 * <br>
 * Instantiated objects of this class are immutable.
 *
 * @Note since this class is package private and {@link PhoneManager} uses it as secured delegate,
 *       it is not necessary to have additional preconditions or argument checks.
 */
final class PhoneSessionMap {

    private final HashMap<String, HashSet<String>> _phoneMap;
    private final HashMap<String, String> _sessionMap;

    private PhoneSessionMap(HashMap<String, HashSet<String>> phoneMap, HashMap<String, String> sessionMap) {
        _phoneMap = phoneMap;
        _sessionMap = sessionMap;
    }

    public static PhoneSessionMap empty() {
        return new PhoneSessionMap(HashMap.<String, HashSet<String>> empty(), HashMap.<String, String> empty());
    }

    public Option<HashSet<String>> sessionIds(String phoneNumber) {
        return _phoneMap.get(phoneNumber);
    }

    public Set<String> phoneNumbers() {
        return _phoneMap.keySet();
    }

    /**
     * Since this is an immutable implementation, adding a value to the {@link PhoneSessionMap} will
     * create a new instance of it.
     * 
     * @param phoneNumber
     * @param sessionId
     * @return
     */
    public PhoneSessionMap add(String phoneNumber, String sessionId) {
        HashSet<String> sessionIds = HashSet.of(sessionId);
        if (_phoneMap.keySet().contains(phoneNumber)) {
            sessionIds = _phoneMap.get(phoneNumber).get().add(sessionId);
        }
        HashMap<String, HashSet<String>> newPhoneMap = _phoneMap.put(phoneNumber, sessionIds);
        // TODO what happens if this sessionID has already another number registered?
        HashMap<String, String> newSessionMap = _sessionMap.put(sessionId, phoneNumber);
        return new PhoneSessionMap(newPhoneMap, newSessionMap);
    }

    public PhoneSessionMap remove(String sessionId) {
        return _sessionMap.get(sessionId)
                .map(phoneNumber -> {
                    HashMap<String, HashSet<String>> newPhoneMap = _phoneMap.get(phoneNumber)
                            .map(sessionIds -> sessionIds.remove(sessionId))
                            .map(newSessionIds -> {
                                if (newSessionIds.isEmpty()) {
                                    return _phoneMap.remove(phoneNumber);
                                }
                                return _phoneMap.put(phoneNumber, newSessionIds);
                            })
                            .orElse(Option.some(_phoneMap))
                            .get();
                    return new PhoneSessionMap(newPhoneMap, _sessionMap.remove(sessionId));
                })
                .orElse(Option.some(this))
                .get();
    }
}
