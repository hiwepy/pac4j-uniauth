/*
 * Copyright (c) 2018, Loong Wan (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.pac4j.ext.uniauth;

import org.pac4j.core.ext.client.SignatureClient;
import org.pac4j.core.ext.credentials.SignatureCredentials;
import org.pac4j.core.ext.credentials.authenticator.SignatureAuthenticator;
import org.pac4j.core.profile.creator.ProfileCreator;

/**
 * pac4j direct client that authenticates HTTP requests carrying a Uniauth
 * signature parameter.
 *
 * <p>The client extracts a signature value from the incoming HTTP request
 * (using the parameter name supplied at construction), delegates credential
 * validation to an {@link UniauthSignatureAuthenticator}, and produces an
 * {@link UniauthSignatureProfile} once validation succeeds. Three
 * constructors are provided so callers can opt-in to a custom parameter name,
 * a custom authenticator, and/or a custom {@link ProfileCreator}.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see SignatureClient
 * @see UniauthSignatureAuthenticator
 * @see UniauthSignatureProfile
 */
public class UniauthSignatureClient extends SignatureClient<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> {

    /**
     * Default constructor.
     *
     * <p>Creates a client that uses the framework-default signature parameter
     * name and authenticator. Equivalent to invoking the parameterised
     * constructors with {@code null} arguments, in which case pac4j applies
     * its built-in defaults at {@link #internalInit(boolean)} time.</p>
     */
    public UniauthSignatureClient() {
    }

    /**
     * Creates a client with a custom signature parameter name and
     * authenticator.
     *
     * @param signatureParameterName the HTTP request parameter that carries
     *                                the Uniauth signature value, never
     *                                {@code null}
     * @param tokenAuthenticator      the authenticator used to validate the
     *                                parsed signature credentials, never
     *                                {@code null}
     */
    public UniauthSignatureClient(String signatureParameterName,
            SignatureAuthenticator<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> tokenAuthenticator) {
        super( signatureParameterName, tokenAuthenticator);
    }

    /**
     * Creates a client with a custom signature parameter name, authenticator
     * and profile creator.
     *
     * @param signatureParameterName the HTTP request parameter that carries
     *                                the Uniauth signature value, never
     *                                {@code null}
     * @param tokenAuthenticator      the authenticator used to validate the
     *                                parsed signature credentials, never
     *                                {@code null}
     * @param profileCreator          the strategy used to convert a validated
     *                                profile into the pac4j
     *                                {@code UserProfile} returned to
     *                                downstream callers, never {@code null}
     */
    public UniauthSignatureClient(String signatureParameterName,
            SignatureAuthenticator<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> tokenAuthenticator,
            ProfileCreator profileCreator) {
        super( signatureParameterName, tokenAuthenticator, profileCreator);
    }

}
