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

import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.ext.profile.SignatureProfileDefinition;
import org.pac4j.core.profile.factory.ProfileFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * Profile definition used to convert Uniauth signature payloads into
 * {@link UniauthSignatureProfile} instances.
 *
 * <p>The definition parses the JSON payload portion of a signature credential
 * using Fastjson and copies the recognised attributes ({@code userid},
 * {@code pid}, {@code xm}, {@code ptype}, {@code csrq}, {@code flag}) onto a
 * fresh {@link UniauthSignatureProfile}. Any deserialisation failure is
 * wrapped in a {@link TechnicalException} so callers see a single, consistent
 * error type regardless of the underlying Fastjson issue.</p>
 *
 * <p>Two constructors are provided: a no-argument constructor suitable for
 * reflective use, and one accepting a {@link ProfileFactory} for advanced
 * scenarios where profile instances are produced by a factory.</p>
 *
 * @author Loong Wan
 * @since 3.0.0
 * @see SignatureProfileDefinition
 * @see UniauthSignatureProfile
 */
public class UniauthSignatureProfileDefinition extends SignatureProfileDefinition<UniauthSignatureProfile, UniauthSignature> {

    /**
     * Default constructor.
     *
     * <p>Delegates to the no-arg super constructor. Profile instances are
     * produced by the framework-default factory, which simply allocates a
     * new {@link UniauthSignatureProfile} on demand.</p>
     */
    public UniauthSignatureProfileDefinition() {
        super();
    }

    /**
     * Constructs a profile definition that uses the supplied factory to
     * materialise profile instances.
     *
     * @param profileFactory the factory used to produce
     *                        {@link UniauthSignatureProfile} instances,
     *                        never {@code null}
     */
    public UniauthSignatureProfileDefinition(final ProfileFactory profileFactory) {
        super(profileFactory);
    }

    /**
     * Extracts a Uniauth signature profile from the supplied payload /
     * signature pair.
     *
     * <p>The {@code signature} argument is not used directly by this
     * implementation &mdash; it is accepted only because the parent class
     * exposes it as part of the signature-flow contract. The {@code payload}
     * is parsed as JSON and the resulting object is interpreted as a
     * {@link UniauthSignatureProfile}.</p>
     *
     * @param payload   the JSON payload that was extracted from the
     *                  signature credential, never {@code null}
     * @param signature the accompanying signature value (unused here)
     * @return a {@link UniauthSignatureProfile} populated from the payload,
     *         never {@code null}
     * @throws TechnicalException if the payload cannot be deserialised as a
     *                            {@link UniauthSignatureProfile}
     */
    @Override
    public UniauthSignatureProfile extractUserProfile(String payload, String signature) {
        final UniauthSignatureProfile profileClass = (UniauthSignatureProfile) this.newProfile();
        final UniauthSignatureProfile profile;
        try {
            @SuppressWarnings("unchecked")
            final UniauthSignatureProfile parsed = (UniauthSignatureProfile) JSONObject.parseObject(payload, profileClass.getClass());
            profile = parsed;
        } catch (final Exception e) {
            throw new TechnicalException(e);
        }
        logger.debug("profile: {}", profile);
        return profile;
    }

}
