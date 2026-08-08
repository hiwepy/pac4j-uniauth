package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UniauthTokenProfile}.
 */
class UniauthTokenProfileTest {

    @Test
    void shouldReturnNullIdWhenPidNotSet() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getId());
    }

    @Test
    void shouldReturnPidAsId() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        profile.setPid("PID-100");
        assertEquals("PID-100", profile.getId());
    }

    @Test
    void shouldGetAndSetUserid() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getUserid());
        profile.setUserid("tokuser");
        assertEquals("tokuser", profile.getUserid());
    }

    @Test
    void shouldGetAndSetPid() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getPid());
        profile.setPid("PID-200");
        assertEquals("PID-200", profile.getPid());
    }

    @Test
    void shouldGetAndSetXm() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getXm());
        profile.setXm("Jane Smith");
        assertEquals("Jane Smith", profile.getXm());
    }

    @Test
    void shouldGetAndSetPtype() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getPtype());
        profile.setPtype("staff");
        assertEquals("staff", profile.getPtype());
    }

    @Test
    void shouldGetAndSetCsrq() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getCsrq());
        profile.setCsrq("1990-05-15");
        assertEquals("1990-05-15", profile.getCsrq());
    }

    @Test
    void shouldGetAndSetFlag() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
        assertNull(profile.getFlag());
        profile.setFlag("0");
        assertEquals("0", profile.getFlag());
    }

    @Test
    void shouldAcceptNullValues() {
        UniauthTokenProfile profile = new UniauthTokenProfile();
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
