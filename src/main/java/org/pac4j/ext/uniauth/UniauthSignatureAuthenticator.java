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

import org.pac4j.core.ext.credentials.SignatureCredentials;
import org.pac4j.core.ext.credentials.authenticator.SignatureAuthenticator;

/**
 * Authenticator that validates Uniauth signature credentials.
 *
 * <p>The authenticator plugs into the pac4j <em>direct client</em> machinery
 * &mdash; it is invoked when an HTTP request carries a signature parameter
 * (see {@link org.pac4j.core.ext.credentials.extractor.SignatureParameterExtractor}).
 * It builds an {@link UniauthSignatureProfile} from the parsed
 * {@link SignatureCredentials} by delegating to an
 * {@link UniauthSignatureProfileDefinition}.</p>
 *
 * <p>The authenticator is fully wired by overriding {@link #internalInit(boolean)};
 * no external configuration is required for typical deployments because all
 * defaults are inferred from the Uniauth endpoint contract.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see SignatureAuthenticator
 * @see UniauthSignatureProfileDefinition
 * @see UniauthSignature
 */
public class UniauthSignatureAuthenticator extends SignatureAuthenticator<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> {

    /**
     * Initialises the authenticator and registers the
     * {@link UniauthSignatureProfileDefinition} that converts raw JSON
     * payloads into {@link UniauthSignatureProfile} instances.
     *
     * @param forceReinit when {@code true}, forces re-initialisation even
     *                    if the component was already initialised &mdash;
     *                    used internally by pac4j's component lifecycle
     */
    @Override
    protected void internalInit(boolean forceReinit) {
        defaultProfileDefinition(new UniauthSignatureProfileDefinition(x -> new UniauthSignatureProfile()));
        super.internalInit(forceReinit);
    }

}
