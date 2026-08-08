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

import org.pac4j.core.ext.profile.Signature;

/**
 * Uniauth signature token wrapper.
 *
 * <p>This class is a thin, type-safe specialization of the framework-neutral
 * {@link Signature} class. It encapsulates a signed payload returned by the
 * Uniauth authentication service &mdash; typically a base64-encoded token
 * containing both a JSON payload and an HMAC/cipher signature.</p>
 *
 * <p>The protected constructor enforces that instances can only be created
 * inside this package (typically by {@link UniauthSignatureAuthenticator}
 * after a credential has been parsed from the HTTP request). Callers obtain
 * the wrapped raw response via the inherited {@link Signature#getRawResponse()}
 * method and individual parameters via {@link Signature#getParameter(String)}.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 3.0.0
 * @see Signature
 * @see UniauthSignatureAuthenticator
 */
@SuppressWarnings("serial")
public class UniauthSignature extends Signature {

    /**
     * Package-private constructor used by {@link UniauthSignatureAuthenticator}
     * when materialising a signature object from a parsed HTTP parameter.
     *
     * @param rawResponse the raw signature value as received from the
     *                    Uniauth endpoint, never {@code null}
     */
    protected UniauthSignature(String rawResponse) {
        super(rawResponse);
    }

}
