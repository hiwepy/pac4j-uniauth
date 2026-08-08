package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UniauthSignatureProfile}.
 */
class UniauthSignatureProfileTest {

    @Test
    void shouldReturnNullIdWhenPidNotSet() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getId());
    }

    @Test
    void shouldReturnPidAsId() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        profile.setPid("PID-001");
        assertEquals("PID-001", profile.getId());
    }

    @Test
    void shouldGetAndSetUserid() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getUserid());
        profile.setUserid("user01");
        assertEquals("user01", profile.getUserid());
    }

    @Test
    void shouldGetAndSetPid() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getPid());
        profile.setPid("PID-002");
        assertEquals("PID-002", profile.getPid());
    }

    @Test
    void shouldGetAndSetXm() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getXm());
        profile.setXm("John Doe");
        assertEquals("John Doe", profile.getXm());
    }

    @Test
    void shouldGetAndSetPtype() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getPtype());
        profile.setPtype("student");
        assertEquals("student", profile.getPtype());
    }

    @Test
    void shouldGetAndSetCsrq() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getCsrq());
        profile.setCsrq("2000-01-01");
        assertEquals("2000-01-01", profile.getCsrq());
    }

    @Test
    void shouldGetAndSetFlag() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        assertNull(profile.getFlag());
        profile.setFlag("1");
        assertEquals("1", profile.getFlag());
    }

    @Test
    void shouldAcceptNullValues() {
        UniauthSignatureProfile profile = new UniauthSignatureProfile();
        profile.setUserid(null);
        profile.setPid(null);
        profile.setXm(null);
        profile.setPtype(null);
        profile.setCsrq(null);
        profile.setFlag(null);
        assertNull(profile.getUserid());
        assertNull(profile.getPid());
        assertNull(profile.getXm());
        assertNull(profile.getPtype());
        assertNull(profile.getCsrq());
        assertNull(profile.getFlag());
    }
}
