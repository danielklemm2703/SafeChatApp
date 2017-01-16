package phone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

import javax.websocket.Session;

import com.google.common.collect.Sets;

public class PhoneManager {

	private final HashMap<String, HashSet<Session>> _phones;
	private static final AtomicReference<PhoneManager> _singletonHolder = new AtomicReference<>();

	public static final PhoneManager instance() {
		if (_singletonHolder.get() == null) {
			_singletonHolder.set(new PhoneManager());
		}
		return _singletonHolder.get();
	}

	public void registerPhoneNumber(String phoneNumber, Session session) {
		if (!_phones.keySet().contains(phoneNumber)) {
			HashSet<Session> sessions = Sets.newHashSet(session);
			_phones.put(phoneNumber, sessions);
		} else {
			_phones.get(phoneNumber).add(session);
		}
		System.err.println(_phones.get(phoneNumber));
	}

	private PhoneManager() {
		_phones = new HashMap<String, HashSet<Session>>();
	}
}
