package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.pac4j.core.credentials.TokenCredentials;

/**
 * Tests for {@link UniauthTokenAuthenticator}.
 */
class UniauthTokenAuthenticatorTest {

    private static final String PROFILE_URL = "https://uniauth.example.com/api/profile";

    @Test
    void shouldStoreProfileUrl() {
        UniauthTokenAuthenticator auth = new UniauthTokenAuthenticator(PROFILE_URL);
        assertEquals(PROFILE_URL, auth.getProfileUrl());
    }

    @Test
    void shouldInitialiseSuccessfully() throws Exception {
        UniauthTokenAuthenticator auth = new UniauthTokenAuthenticator(PROFILE_URL);

        Method initMethod = auth.getClass().getSuperclass().getDeclaredMethod("internalInit", boolean.class);
        initMethod.setAccessible(true);
        initMethod.invoke(auth, false);

        // no exception means success
        assertEquals(PROFILE_URL, auth.getProfileUrl());
    }

    @Test
    void shouldCreateAccessToken() throws Exception {
        UniauthTokenAuthenticator auth = new UniauthTokenAuthenticator(PROFILE_URL);

        Method getAccessTokenMethod = UniauthTokenAuthenticator.class.getDeclaredMethod("getAccessToken", TokenCredentials.class);
        getAccessTokenMethod.setAccessible(true);

        TokenCredentials creds = new TokenCredentials("my-token");
        UniauthToken token = (UniauthToken) getAccessTokenMethod.invoke(auth, creds);

        assertNotNull(token);
        assertEquals("my-token", token.getRawResponse());
    }

    @Test
    void shouldReturnDifferentTokensForDifferentCredentials() throws Exception {
        UniauthTokenAuthenticator auth = new UniauthTokenAuthenticator(PROFILE_URL);

        Method getAccessTokenMethod = UniauthTokenAuthenticator.class.getDeclaredMethod("getAccessToken", TokenCredentials.class);
        getAccessTokenMethod.setAccessible(true);

        UniauthToken t1 = (UniauthToken) getAccessTokenMethod.invoke(auth, new TokenCredentials("tok-a"));
        UniauthToken t2 = (UniauthToken) getAccessTokenMethod.invoke(auth, new TokenCredentials("tok-b"));

        assertEquals("tok-a", t1.getRawResponse());
        assertEquals("tok-b", t2.getRawResponse());
    }

    @Test
    void shouldExposeProfileUrlBeforeInit() {
        UniauthTokenAuthenticator auth = new UniauthTokenAuthenticator(PROFILE_URL);
        assertEquals(PROFILE_URL, auth.getProfileUrl());
    }
}
