package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpCommunicationException;

/**
 * Tests for {@link UniauthTokenProfileDefinition}.
 */
class UniauthTokenProfileDefinitionTest {

    private static final String PROFILE_URL = "https://uniauth.example.com/profile";

    @Test
    void shouldCreateWithProfileUrlOnly() {
        UniauthTokenProfileDefinition def = new UniauthTokenProfileDefinition(PROFILE_URL);
        assertNotNull(def);
    }

    @Test
    void shouldCreateWithProfileUrlAndFactory() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());
        assertNotNull(def);
    }

    @Test
    void shouldReturnProfileUrl() {
        UniauthTokenProfileDefinition def = new UniauthTokenProfileDefinition(PROFILE_URL);
        WebContext context = mock(WebContext.class);
        UniauthToken token = new UniauthToken("tok");

        String url = def.getProfileUrl(context, token);

        assertEquals(PROFILE_URL, url);
    }

    @Test
    void shouldReturnSameUrlRegardlessOfToken() {
        UniauthTokenProfileDefinition def = new UniauthTokenProfileDefinition(PROFILE_URL);
        WebContext context = mock(WebContext.class);

        assertEquals(PROFILE_URL, def.getProfileUrl(context, new UniauthToken("a")));
        assertEquals(PROFILE_URL, def.getProfileUrl(context, new UniauthToken("b")));
    }

    @Test
    void shouldExtractProfileFromSuccessResponse() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());

        String body = "{\"status\":\"success\",\"pinfo\":{"
                + "\"userid\":\"tokuser\",\"pid\":\"PID-300\",\"xm\":\"Alice\","
                + "\"ptype\":\"staff\",\"csrq\":\"1985-03-20\",\"flag\":\"1\"}}";

        UniauthTokenProfile profile = def.extractUserProfile(body);

        assertNotNull(profile);
        assertEquals("tokuser", profile.getUserid());
        assertEquals("PID-300", profile.getPid());
        assertEquals("Alice", profile.getXm());
        assertEquals("staff", profile.getPtype());
        assertEquals("1985-03-20", profile.getCsrq());
        assertEquals("1", profile.getFlag());
    }

    @Test
    void shouldExtractProfileWithPartialPinfo() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());

        String body = "{\"status\":\"success\",\"pinfo\":{\"pid\":\"PID-PARTIAL\"}}";

        UniauthTokenProfile profile = def.extractUserProfile(body);

        assertNotNull(profile);
        assertEquals("PID-PARTIAL", profile.getPid());
        assertNull(profile.getUserid());
    }

    @Test
    void shouldThrowHttpCommunicationExceptionOnFailStatus() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());

        String body = "{\"status\":\"fail\",\"msg\":\"Certificate validation failed\"}";

        HttpCommunicationException ex = assertThrows(HttpCommunicationException.class,
                () -> def.extractUserProfile(body));

        assertTrue(ex.getMessage().contains("Certificate validation failed"));
    }

    @Test
    void shouldHandleFailStatusWithNullMessage() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());

        String body = "{\"status\":\"fail\"}";

        assertThrows(HttpCommunicationException.class,
                () -> def.extractUserProfile(body));
    }

    @Test
    void shouldHandleFailStatusCaseInsensitive() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());

        String body = "{\"status\":\"Fail\",\"msg\":\"error\"}";

        assertThrows(HttpCommunicationException.class,
                () -> def.extractUserProfile(body));
    }

    @Test
    void shouldExtractProfileWithEmptyPinfo() {
        UniauthTokenProfileDefinition def =
                new UniauthTokenProfileDefinition(PROFILE_URL, args -> new UniauthTokenProfile());

        String body = "{\"status\":\"success\",\"pinfo\":{}}";

        UniauthTokenProfile profile = def.extractUserProfile(body);

        assertNotNull(profile);
        assertNull(profile.getPid());
    }
}
