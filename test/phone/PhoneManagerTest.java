package phone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import javaslang.collection.HashSet;

import org.junit.After;
import org.junit.Test;

public class PhoneManagerTest {

    @After
    public void after() {
        PhoneManager.reset();
    }

    @Test
    public void testInstance() {
        assertThat(PhoneManager.instance()).isEqualTo(PhoneManager.instance());
    }

    @Test
    public void testRegisterPhoneNumber_multipleDifferentSessions() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session";

        // to test
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "1");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "2");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "3");
        HashSet<String> registeredSessionsToNumber = phoneManager.registerPhoneNumber(phoneNumber, sessionId + "4");

        // verify
        assertThat(registeredSessionsToNumber).hasSize(4);
        assertThat(registeredSessionsToNumber).isEqualTo(phoneManager.sessionIdsOf(phoneNumber));
    }

    @Test
    public void testRegisterPhoneNumber_multipleSameSession() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session";

        // to test
        phoneManager.registerPhoneNumber(phoneNumber, sessionId);
        phoneManager.registerPhoneNumber(phoneNumber, sessionId);
        phoneManager.registerPhoneNumber(phoneNumber, sessionId);
        HashSet<String> registeredSessionsToNumber = phoneManager.registerPhoneNumber(phoneNumber, sessionId);

        // verify
        assertThat(registeredSessionsToNumber).hasSize(1);
        assertThat(registeredSessionsToNumber).isEqualTo(phoneManager.sessionIdsOf(phoneNumber));
    }

    @Test
    public void testRegisterPhoneNumber_phoneNumberIsEmpty() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "";
        String sessionId = "session";

        // to test & verify
        try {
            phoneManager.registerPhoneNumber(phoneNumber, sessionId);
            fail("empty phone number should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'phoneNumber' must not be null or empty");
        }
    }

    @Test
    public void testRegisterPhoneNumber_phoneNumberIsNull() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String sessionId = "session";

        // to test & verify
        try {
            phoneManager.registerPhoneNumber(null, sessionId);
            fail("empty phone number should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'phoneNumber' must not be null or empty");
        }
    }

    @Test
    public void testRegisterPhoneNumber_sessionIdIsEmpty() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "";

        // to test & verify
        try {
            phoneManager.registerPhoneNumber(phoneNumber, sessionId);
            fail("empty sessionId should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'sessionId' must not be null or empty");
        }
    }

    @Test
    public void testRegisterPhoneNumber_sessionIdIsNull() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";

        // to test & verify
        try {
            phoneManager.registerPhoneNumber(phoneNumber, null);
            fail("empty sessionId should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'sessionId' must not be null or empty");
        }
    }

    @Test
    public void testRegisterPhoneNumber_singleSession() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session1";

        // to test
        HashSet<String> registeredSessionsToNumber = phoneManager.registerPhoneNumber(phoneNumber, sessionId);

        // verify
        assertThat(registeredSessionsToNumber).hasSize(1);
        assertThat(registeredSessionsToNumber).isEqualTo(phoneManager.sessionIdsOf(phoneNumber));
    }

    @Test
    public void testRemoveSession_notExistingSessionDoesNotAffectRegisteredNumbers() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session1";
        phoneManager.registerPhoneNumber(phoneNumber, sessionId);
        // pre-verify

        // to test
        phoneManager.removeSessionId("notExistingSessionId");

        // post-verify
        assertThat(phoneManager.sessionIdsOf(phoneNumber)).containsExactly(sessionId);
    }

    @Test
    public void testRemoveSession_numberStillHasSessionsRegistered() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session";
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "1");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "2");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "3");

        // pre-verify
        assertThat(phoneManager.sessionIdsOf(phoneNumber)).hasSize(3);

        // to test
        phoneManager.removeSessionId(sessionId + "2");

        // post-verify
        assertThat(phoneManager.sessionIdsOf(phoneNumber)).containsExactly(sessionId + "1", sessionId + "3");
    }

    @Test
    public void testRemoveSession_sessionIdIsEmpty() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String sessionId = "";

        // to test & verify
        try {
            phoneManager.removeSessionId(sessionId);
            fail("empty sessionId should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'sessionId' must not be null or empty");
        }
    }

    @Test
    public void testRemoveSession_sessionIdIsNull() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();

        // to test & verify
        try {
            phoneManager.removeSessionId(null);
            fail("empty sessionId should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'sessionId' must not be null or empty");
        }
    }

    @Test
    public void testRemoveSession_singleSessionForNumberAlsoRemovesNumber() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session1";
        phoneManager.registerPhoneNumber(phoneNumber, sessionId);

        // pre-verify
        assertThat(phoneManager.sessionIdsOf(phoneNumber)).hasSize(1);

        // to test
        phoneManager.removeSessionId(sessionId);

        // post-verify
        assertThat(phoneManager.sessionIdsOf(phoneNumber)).isEmpty();
    }

    @Test
    public void testSessionIdsOf_multipleRegisteredSessions() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session";
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "1");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "2");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "3");
        phoneManager.registerPhoneNumber(phoneNumber, sessionId + "4");

        // to test
        HashSet<String> sessionIdsOfNumber = phoneManager.sessionIdsOf(phoneNumber);

        // verify
        assertThat(sessionIdsOfNumber).containsExactly(sessionId + "1", sessionId + "2", sessionId + "3", sessionId + "4");
    }

    @Test
    public void testSessionIdsOf_notRegisteredPhoneNumber() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();

        // to test
        HashSet<String> sessionIdsOfNumber = phoneManager.sessionIdsOf("1234");

        // verify
        assertThat(sessionIdsOfNumber).isEmpty();
    }

    @Test
    public void testSessionIdsOf_sessionIdIsNull() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();

        // to test & verify
        try {
            phoneManager.sessionIdsOf(null);
            fail("empty phone number should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'phoneNumber' must not be null or empty");
        }
    }

    @Test
    public void testSessionIdsOf_singleRegisteredSession() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "1234";
        String sessionId = "session";
        phoneManager.registerPhoneNumber(phoneNumber, sessionId);

        // to test
        HashSet<String> sessionIdsOfNumber = phoneManager.sessionIdsOf("1234");

        // verify
        assertThat(sessionIdsOfNumber).containsExactly(sessionId);
    }

    @Test
    public void testSessionsIdsOf_phoneNumberIsEmpty() {
        // set up
        PhoneManager phoneManager = PhoneManager.instance();
        String phoneNumber = "";

        // to test & verify
        try {
            phoneManager.sessionIdsOf(phoneNumber);
            fail("empty phone number should not be allowed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e).hasMessage("Argument 'phoneNumber' must not be null or empty");
        }
    }
}
