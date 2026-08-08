package org.pac4j.ext.uniauth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.pac4j.core.ext.credentials.SignatureCredentials;
import org.pac4j.core.ext.credentials.authenticator.SignatureAuthenticator;
import org.pac4j.core.profile.creator.ProfileCreator;

/**
 * Tests for {@link UniauthSignatureClient}.
 */
class UniauthSignatureClientTest {

    @Test
    void shouldCreateWithDefaultConstructor() {
        UniauthSignatureClient client = new UniauthSignatureClient();
        assertNotNull(client);
    }

    @Test
    void shouldCreateWithParameterNameAndAuthenticator() {
        @SuppressWarnings("unchecked")
        SignatureAuthenticator<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> auth =
                new UniauthSignatureAuthenticator();

        UniauthSignatureClient client = new UniauthSignatureClient("sigParam", auth);
        assertNotNull(client);
    }

    @Test
    void shouldCreateWithAllParameters() {
        @SuppressWarnings("unchecked")
        SignatureAuthenticator<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> auth =
                new UniauthSignatureAuthenticator();
        ProfileCreator creator = (ctx, creds) -> java.util.Optional.empty();

        UniauthSignatureClient client = new UniauthSignatureClient("sigParam", auth, creator);
        assertNotNull(client);
    }
}
