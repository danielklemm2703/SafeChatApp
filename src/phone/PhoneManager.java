package phone;

import java.util.concurrent.atomic.AtomicReference;

import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.control.Option;

import javax.websocket.Session;

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
    private HashMap<String, HashSet<Session>> _phoneMap;

    private PhoneManager() {
        _phoneMap = HashMap.<String, HashSet<Session>> empty();
    }

    /**
     * registers a number to a session, returns all sessions that belong to a given number
     * 
     * @param phoneNumber
     * @param session
     * @return a set of registered sessions to a single number
     */
    public HashSet<Session> registerPhoneNumber(String phoneNumber, Session session) {
        // TODO what happens if the incoming session already has a phone number registered?
        if (!_phoneMap.keySet().contains(phoneNumber)) {
            HashSet<Session> sessions = HashSet.of(session);
            _phoneMap = _phoneMap.put(phoneNumber, sessions);
        } else {
            HashSet<Session> sessions = _phoneMap.get(phoneNumber).get().add(session);
            _phoneMap = _phoneMap.put(phoneNumber, sessions);
        }
        return _phoneMap.get(phoneNumber)
                .orElse(Option.of(HashSet.<Session> empty()))
                .get();
    }
}
