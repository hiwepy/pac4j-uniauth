package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;

/**
 * Tests for {@link UniauthSignatureProfileDefinition}.
 */
class UniauthSignatureProfileDefinitionTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        UniauthSignatureProfileDefinition def = new UniauthSignatureProfileDefinition();
        assertNotNull(def);
    }

    @Test
    void shouldCreateWithProfileFactory() {
        UniauthSignatureProfileDefinition def =
                new UniauthSignatureProfileDefinition(args -> new UniauthSignatureProfile());
        assertNotNull(def);
    }

    @Test
    void shouldExtractProfileFromValidJson() {
        UniauthSignatureProfileDefinition def =
                new UniauthSignatureProfileDefinition(args -> new UniauthSignatureProfile());

        String payload = "{\"userid\":\"user01\",\"pid\":\"PID-001\",\"xm\":\"John Doe\","
                + "\"ptype\":\"student\",\"csrq\":\"2000-01-01\",\"flag\":\"1\"}";

        UniauthSignatureProfile profile = def.extractUserProfile(payload, "sig-value");

        assertNotNull(profile);
        assertEquals("user01", profile.getUserid());
        assertEquals("PID-001", profile.getPid());
        assertEquals("John Doe", profile.getXm());
        assertEquals("student", profile.getPtype());
        assertEquals("2000-01-01", profile.getCsrq());
        assertEquals("1", profile.getFlag());
        assertEquals("PID-001", profile.getId());
    }

    @Test
    void shouldExtractProfileWithPartialJson() {
        UniauthSignatureProfileDefinition def =
                new UniauthSignatureProfileDefinition(args -> new UniauthSignatureProfile());

        String payload = "{\"pid\":\"PID-ONLY\"}";

        UniauthSignatureProfile profile = def.extractUserProfile(payload, "sig");

        assertNotNull(profile);
        assertEquals("PID-ONLY", profile.getPid());
        assertNull(profile.getUserid());
    }

    @Test
    void shouldExtractProfileWithEmptyJson() {
        UniauthSignatureProfileDefinition def =
                new UniauthSignatureProfileDefinition(args -> new UniauthSignatureProfile());

        String payload = "{}";

        UniauthSignatureProfile profile = def.extractUserProfile(payload, "sig");

        assertNotNull(profile);
        assertNull(profile.getPid());
    }

    @Test
    void shouldThrowTechnicalExceptionForInvalidJson() {
        UniauthSignatureProfileDefinition def =
                new UniauthSignatureProfileDefinition(args -> new UniauthSignatureProfile());

        assertThrows(TechnicalException.class,
                () -> def.extractUserProfile("not-valid-json{{{", "sig"));
    }

    @Test
    void shouldIgnoreSignatureParameter() {
        UniauthSignatureProfileDefinition def =
                new UniauthSignatureProfileDefinition(args -> new UniauthSignatureProfile());

        String payload = "{\"pid\":\"PID-SIG-IGNORE\"}";

        // signature parameter is unused; both calls should produce the same result
        UniauthSignatureProfile p1 = def.extractUserProfile(payload, "sig-a");
        UniauthSignatureProfile p2 = def.extractUserProfile(payload, "sig-b");

        assertEquals(p1.getPid(), p2.getPid());
    }
}
