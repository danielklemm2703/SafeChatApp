package phone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import javaslang.control.Option;

import javax.websocket.Session;

import com.google.common.collect.Sets;

public class PhoneManager {

    private final HashMap<String, HashSet<Session>> _phoneMap;
    private static final AtomicReference<PhoneManager> _singletonHolder = new AtomicReference<PhoneManager>();

    public static final PhoneManager instance() {
        if (_singletonHolder.get() == null) {
            _singletonHolder.set(new PhoneManager());
        }
        return _singletonHolder.get();
    }

    public void registerPhoneNumber(String phoneNumber, Session session) {
        if (!_phoneMap.keySet().contains(phoneNumber)) {
            HashSet<Session> sessions = Sets.newHashSet(session);
            _phoneMap.put(phoneNumber, sessions);
        } else {
            _phoneMap.get(phoneNumber).add(session);
        }
        Option<Integer> of = Option.of(4);
        System.err.println("Registered for number: " + phoneNumber);
        System.err.println("Number of sessions: " + _phoneMap.get(phoneNumber).size());
    }

    private PhoneManager() {
        _phoneMap = new HashMap<String, HashSet<Session>>();
    }
}
