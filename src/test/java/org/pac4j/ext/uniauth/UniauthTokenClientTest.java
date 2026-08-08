package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UniauthTokenClient}.
 */
class UniauthTokenClientTest {

    @Test
    void shouldInstantiate() {
        UniauthTokenClient client = new UniauthTokenClient();
        assertNotNull(client);
    }
}
