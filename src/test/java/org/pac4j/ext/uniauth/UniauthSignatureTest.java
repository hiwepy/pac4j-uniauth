package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UniauthSignature}.
 */
class UniauthSignatureTest {

    @Test
    void shouldStoreRawResponse() {
        UniauthSignature sig = new UniauthSignature("abc123");
        assertEquals("abc123", sig.getRawResponse());
    }

    @Test
    void shouldExtractParameterFromQueryString() {
        UniauthSignature sig = new UniauthSignature("token=hello&user=john");
        assertEquals("hello", sig.getParameter("token"));
        assertEquals("john", sig.getParameter("user"));
    }

    @Test
    void shouldReturnNullForMissingParameter() {
        UniauthSignature sig = new UniauthSignature("token=hello");
        assertNull(sig.getParameter("missing"));
    }

    @Test
    void shouldBeSerializable() {
        UniauthSignature sig = new UniauthSignature("serializable-test");
        assertNotNull(sig);
        assertEquals("serializable-test", sig.getRawResponse());
    }
}
