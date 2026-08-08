package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.ext.credentials.SignatureCredentials;
import org.pac4j.core.profile.factory.ProfileManagerFactory;

/**
 * Tests for {@link UniauthSignatureAuthenticator}.
 */
class UniauthSignatureAuthenticatorTest {

    private CallContext createCallContext() {
        WebContext webContext = mock(WebContext.class);
        SessionStore sessionStore = mock(SessionStore.class);
        return new CallContext(webContext, sessionStore, ProfileManagerFactory.DEFAULT);
    }

    @Test
    void shouldInitialiseWithoutErrors() {
        UniauthSignatureAuthenticator auth = new UniauthSignatureAuthenticator();
        auth.init();
        // no exception means success
    }

    @Test
    void shouldValidateSignatureCredentials() {
        UniauthSignatureAuthenticator auth = new UniauthSignatureAuthenticator();
        auth.init();

        String payload = "{\"userid\":\"u1\",\"pid\":\"P1\",\"xm\":\"Test\","
                + "\"ptype\":\"student\",\"csrq\":\"2000-01-01\",\"flag\":\"0\"}";

        SignatureCredentials creds = new SignatureCredentials(payload, "sig-val");

        Optional<Credentials> result = auth.validate(createCallContext(), creds);

        assertTrue(result.isPresent());
        Credentials returned = result.get();
        assertSame(creds, returned);
    }

    @Test
    void shouldThrowOnNullCredentials() {
        UniauthSignatureAuthenticator auth = new UniauthSignatureAuthenticator();
        auth.init();

        assertThrows(CredentialsException.class,
                () -> auth.validate(createCallContext(), null));
    }

    @Test
    void shouldThrowOnBlankPayload() {
        UniauthSignatureAuthenticator auth = new UniauthSignatureAuthenticator();
        auth.init();

        SignatureCredentials creds = new SignatureCredentials("", "sig");

        assertThrows(CredentialsException.class,
                () -> auth.validate(createCallContext(), creds));
    }
}
