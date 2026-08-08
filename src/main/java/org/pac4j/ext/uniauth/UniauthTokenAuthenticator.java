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

import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.ext.credentials.authenticator.TokenAuthenticator;
import org.pac4j.core.util.CommonHelper;

/**
 * Authenticator that validates Uniauth token credentials.
 *
 * <p>The authenticator plugs into the pac4j <em>direct client</em> machinery
 * &mdash; it is invoked when an HTTP request carries a bearer-token
 * parameter (see
 * {@link org.pac4j.core.ext.credentials.extractor.TokenParameterExtractor}).
 * It calls the Uniauth profile endpoint with the supplied token and builds
 * an {@link UniauthTokenProfile} from the JSON response by delegating to an
 * {@link UniauthTokenProfileDefinition}.</p>
 *
 * <p>The profile endpoint URL is captured at construction time; the
 * authenticator refuses to initialise when the URL is {@code null}.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see TokenAuthenticator
 * @see UniauthTokenProfileDefinition
 * @see UniauthToken
 */
public class UniauthTokenAuthenticator extends TokenAuthenticator<UniauthTokenProfile, UniauthToken> {

    /**
     * The Uniauth endpoint that returns the authenticated user's profile
     * when supplied with a valid access token. Populated at construction
     * time and never mutated afterwards.
     */
    private String profileUrl;

    /**
     * Creates a new authenticator pointing at the given profile URL.
     *
     * @param profileUrl the Uniauth endpoint that returns the user profile
     *                   JSON document, never {@code null}
     * @throws IllegalArgumentException if {@code profileUrl} is {@code null}
     *                                  (enforced by
     *                                  {@link CommonHelper#assertNotNull(String, Object)})
     */
    public UniauthTokenAuthenticator(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    /**
     * Initialises the authenticator, validates the configured
     * {@link #profileUrl} and registers the
     * {@link UniauthTokenProfileDefinition} that converts raw JSON
     * payloads into {@link UniauthTokenProfile} instances.
     *
     * @param forceReinit when {@code true}, forces re-initialisation even
     *                    if the component was already initialised &mdash;
     *                    used internally by pac4j's component lifecycle
     * @throws IllegalArgumentException if {@link #profileUrl} is {@code null}
     */
    @Override
    protected void internalInit(boolean forceReinit) {
        CommonHelper.assertNotNull("profileUrl", profileUrl);
        defaultProfileDefinition(new UniauthTokenProfileDefinition(profileUrl, x -> new UniauthTokenProfile()));
        super.internalInit(forceReinit);
    }

    /**
     * Returns the configured Uniauth profile endpoint URL.
     *
     * @return the profileUrl value passed to the constructor, may be
     *         {@code null} if the authenticator has not yet been
     *         initialised
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * Wraps the supplied credentials into a {@link UniauthToken}.
     *
     * <p>Implemented as a one-liner so callers (and subclasses) can chain
     * token extraction without exposing the underlying
     * {@link TokenCredentials} type.</p>
     *
     * @param credentials the credentials extracted from the HTTP request,
     *                    never {@code null}
     * @return a {@link UniauthToken} wrapping the credentials' raw token,
     *         never {@code null}
     */
    @Override
    protected UniauthToken getAccessToken(TokenCredentials credentials) {
        return new UniauthToken(credentials.getToken());
    }

}
