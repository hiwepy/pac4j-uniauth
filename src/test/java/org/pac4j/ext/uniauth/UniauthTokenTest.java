package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UniauthToken}.
 */
class UniauthTokenTest {

    @Test
    void shouldStoreRawResponse() {
        UniauthToken token = new UniauthToken("bearer-xyz");
        assertEquals("bearer-xyz", token.getRawResponse());
    }

    @Test
    void shouldExtractParameterFromQueryString() {
        UniauthToken token = new UniauthToken("access_token=abc&scope=read");
        assertEquals("abc", token.getParameter("access_token"));
        assertEquals("read", token.getParameter("scope"));
    }

    @Test
    void shouldReturnNullForMissingParameter() {
        UniauthToken token = new UniauthToken("access_token=abc");
        assertNull(token.getParameter("nonexistent"));
    }
}
