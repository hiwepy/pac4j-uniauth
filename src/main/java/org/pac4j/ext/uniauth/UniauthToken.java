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

import org.pac4j.core.ext.profile.Token;

/**
 * Uniauth access token wrapper.
 *
 * <p>This class is a thin, type-safe specialization of the framework-neutral
 * {@link Token} class. It encapsulates an opaque bearer token issued by the
 * Uniauth authentication service. Callers obtain the wrapped raw value via
 * the inherited {@link Token#getRawResponse()} method and individual token
 * parameters via {@link Token#getParameter(String)}.</p>
 *
 * <p>The protected constructor enforces that instances can only be created
 * inside this package &mdash; typically by
 * {@link UniauthTokenAuthenticator#getAccessToken(org.pac4j.core.credentials.TokenCredentials)}
 * after a credential has been parsed from the HTTP request.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see Token
 * @see UniauthTokenAuthenticator
 */
@SuppressWarnings("serial")
public class UniauthToken extends Token {

    /**
     * Package-private constructor used by
     * {@link UniauthTokenAuthenticator#getAccessToken(org.pac4j.core.credentials.TokenCredentials)}
     * when materialising a token object from a parsed HTTP parameter.
     *
     * @param rawResponse the raw bearer-token value as received from the
     *                    Uniauth endpoint, never {@code null}
     */
    protected UniauthToken(String rawResponse) {
        super(rawResponse);
    }

}
