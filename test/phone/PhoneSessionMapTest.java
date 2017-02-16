package phone;

import static org.assertj.core.api.Assertions.assertThat;
import javaslang.collection.HashSet;
import javaslang.control.Option;

import org.junit.Test;

public class PhoneSessionMapTest {

    @Test
    public void testEmpty() {
        PhoneSessionMap emptyMap = PhoneSessionMap.empty();
        assertThat(emptyMap.phoneNumbers()).isEmpty();
    }

    @Test
    public void testSessionIds_noSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";

        // to test
        Option<HashSet<String>> sessionIds = map.sessionIds(phoneNumber);

        // verify
        assertThat(sessionIds.isEmpty()).isTrue();
    }

    @Test
    public void testSessionIds_singelSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber, sessionId);

        // to test
        HashSet<String> sessionIds = map.sessionIds(phoneNumber).get();

        // verify
        assertThat(sessionIds).containsExactly(sessionId);
    }

    @Test
    public void testSessionIds_multipleDifferentSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber, sessionId + "1");
        map = map.add(phoneNumber, sessionId + "2");
        map = map.add(phoneNumber, sessionId + "3");
        map = map.add(phoneNumber, sessionId + "4");

        // to test
        HashSet<String> sessionIds = map.sessionIds(phoneNumber).get();

        // verify
        assertThat(sessionIds).containsExactly(sessionId + "1", sessionId + "2", sessionId + "3", sessionId + "4");
    }

    @Test
    public void testSessionIds_multipleSameSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber, sessionId);
        map = map.add(phoneNumber, sessionId);
        map = map.add(phoneNumber, sessionId);

        // to test
        HashSet<String> sessionIds = map.sessionIds(phoneNumber).get();

        // verify
        assertThat(sessionIds).containsExactly(sessionId);
    }

    @Test
    public void testPhoneNumbers_noPhoneNumbers() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();

        // verify
        assertThat(map.phoneNumbers()).isEmpty();
    }

    @Test
    public void testPhoneNumbers_singlePhoneNumbers() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber, sessionId);

        // verify
        assertThat(map.phoneNumbers()).containsExactly(phoneNumber);
    }

    @Test
    public void testPhoneNumbers_multiplePhoneNumbers() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber + "-1", sessionId + "1");
        map = map.add(phoneNumber + "-2", sessionId + "2");
        map = map.add(phoneNumber + "-3", sessionId + "3");

        // verify
        assertThat(map.phoneNumbers()).containsExactly(phoneNumber + "-1", phoneNumber + "-2", phoneNumber + "-3");
    }

    @Test
    public void testPhoneNumbers_multiplePhoneNumbers_withMultipleSessions() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber + "-1", sessionId + "1");
        map = map.add(phoneNumber + "-1", sessionId + "2");
        map = map.add(phoneNumber + "-2", sessionId + "3");
        map = map.add(phoneNumber + "-2", sessionId + "4");
        map = map.add(phoneNumber + "-3", sessionId + "5");
        map = map.add(phoneNumber + "-3", sessionId + "6");

        // verify
        assertThat(map.phoneNumbers()).containsExactly(phoneNumber + "-1", phoneNumber + "-2", phoneNumber + "-3");
    }

    @Test
    public void testPhoneNumbers_multiplePhoneNumbers_withSameSessions() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber + "-1", sessionId + "1");
        map = map.add(phoneNumber + "-2", sessionId + "1");
        map = map.add(phoneNumber + "-3", sessionId + "1");

        // verify
        assertThat(map.phoneNumbers()).containsExactly(phoneNumber + "-3");
    }

    @Test
    public void testAdd_singlePhoneNumber_withSingleSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";

        // to test
        map = map.add(phoneNumber, sessionId);

        // verify
        assertThat(map.sessionIds(phoneNumber).get()).containsExactly(sessionId);
    }

    @Test
    public void testAdd_sameSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";

        // to test
        map = map.add(phoneNumber, sessionId);
        map = map.add(phoneNumber, sessionId);

        // verify
        assertThat(map.sessionIds(phoneNumber).get()).containsExactly(sessionId);
    }

    @Test
    public void testAdd_multiplePhoneNumbers_withSingleSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";

        // to test
        map = map.add(phoneNumber + "-1", sessionId + "-1");
        map = map.add(phoneNumber + "-2", sessionId + "-2");
        map = map.add(phoneNumber + "-3", sessionId + "-3");
        map = map.add(phoneNumber + "-4", sessionId + "-4");

        // verify
        assertThat(map.sessionIds(phoneNumber + "-1").get()).containsExactly(sessionId + "-1");
        assertThat(map.sessionIds(phoneNumber + "-2").get()).containsExactly(sessionId + "-2");
        assertThat(map.sessionIds(phoneNumber + "-3").get()).containsExactly(sessionId + "-3");
        assertThat(map.sessionIds(phoneNumber + "-4").get()).containsExactly(sessionId + "-4");
    }

    @Test
    public void testAdd_multiplePhoneNumbers_withMultipleSessions() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";

        // to test
        map = map.add(phoneNumber + "-1", sessionId + "-1");
        map = map.add(phoneNumber + "-1", sessionId + "-2");
        map = map.add(phoneNumber + "-2", sessionId + "-3");
        map = map.add(phoneNumber + "-2", sessionId + "-4");
        map = map.add(phoneNumber + "-3", sessionId + "-5");
        map = map.add(phoneNumber + "-3", sessionId + "-6");
        map = map.add(phoneNumber + "-4", sessionId + "-7");
        map = map.add(phoneNumber + "-4", sessionId + "-8");

        // verify
        assertThat(map.sessionIds(phoneNumber + "-1").get()).containsExactly(sessionId + "-1", sessionId + "-2");
        assertThat(map.sessionIds(phoneNumber + "-2").get()).containsExactly(sessionId + "-3", sessionId + "-4");
        assertThat(map.sessionIds(phoneNumber + "-3").get()).containsExactly(sessionId + "-5", sessionId + "-6");
        assertThat(map.sessionIds(phoneNumber + "-4").get()).containsExactly(sessionId + "-7", sessionId + "-8");
    }

    @Test
    public void testAdd_multiplePhoneNumbers_withSameSessions_overwritesOldPhoneNumber() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";

        // to test
        map = map.add(phoneNumber + "-1", sessionId + "-1");
        map = map.add(phoneNumber + "-2", sessionId + "-1");
        map = map.add(phoneNumber + "-3", sessionId + "-1");
        map = map.add(phoneNumber + "-4", sessionId + "-1");

        // verify
        assertThat(map.sessionIds(phoneNumber + "-1").isEmpty()).isTrue();
        assertThat(map.sessionIds(phoneNumber + "-2").isEmpty()).isTrue();
        assertThat(map.sessionIds(phoneNumber + "-3").isEmpty()).isTrue();
        assertThat(map.sessionIds(phoneNumber + "-4").get()).containsExactly(sessionId + "-1");
    }

    @Test
    public void testRemove_noSession() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();

        // to test
        PhoneSessionMap removedSessionMap = map.remove("notExistingSession");

        // verify
        assertThat(removedSessionMap).isEqualTo(map);
    }

    @Test
    public void testRemove_singleSessionRemovesPhoneNumber() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber, sessionId);

        // to test
        map = map.remove(sessionId);

        // verify
        assertThat(map.phoneNumbers()).isEmpty();
        assertThat(map.sessionIds(phoneNumber).isEmpty()).isTrue();
    }

    @Test
    public void testRemove_multipleSessions() {
        // set up
        PhoneSessionMap map = PhoneSessionMap.empty();
        String phoneNumber = "1234";
        String sessionId = "sessionId";
        map = map.add(phoneNumber, sessionId + "-1");
        map = map.add(phoneNumber, sessionId + "-2");

        // to test
        map = map.remove(sessionId + "-1");

        // verify
        assertThat(map.phoneNumbers()).containsExactly(phoneNumber);
        assertThat(map.sessionIds(phoneNumber).get()).containsExactly(sessionId + "-2");
    }
}
