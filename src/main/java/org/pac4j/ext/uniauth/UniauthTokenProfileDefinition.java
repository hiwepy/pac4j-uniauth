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

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.ext.profile.TokenProfileDefinition;
import org.pac4j.core.profile.factory.ProfileFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * Profile definition used to convert Uniauth REST responses into
 * {@link UniauthTokenProfile} instances.
 *
 * <p>The definition interprets the JSON envelope returned by the Uniauth
 * profile endpoint:</p>
 * <ul>
 *     <li>{@code status = "fail"} &mdash; the {@code msg} field is wrapped in
 *         a {@link HttpCommunicationException} and the auth flow fails fast.</li>
 *     <li>{@code status = "success"} &mdash; the {@code pinfo} field is parsed
 *         as a {@link UniauthTokenProfile}. Any deserialisation failure is
 *         wrapped in a {@link TechnicalException}.</li>
 * </ul>
 *
 * <p>The {@link #profileUrl} is captured at construction time and exposed via
 * {@link #getProfileUrl(WebContext, UniauthToken)}; the {@code accessToken}
 * argument is not used because the URL is static for the lifetime of this
 * definition.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see TokenProfileDefinition
 * @see UniauthTokenProfile
 * @see UniauthToken
 */
public class UniauthTokenProfileDefinition extends TokenProfileDefinition<UniauthTokenProfile, UniauthToken> {

    /**
     * The Uniauth endpoint URL used to look up the profile of the
     * currently authenticated user. Populated at construction time and
     * never mutated afterwards.
     */
    protected final String profileUrl;

    /**
     * Creates a new definition that points at the supplied profile URL.
     *
     * @param profileUrl the Uniauth endpoint that returns the profile JSON
     *                   document, never {@code null}
     */
    public UniauthTokenProfileDefinition(String profileUrl) {
        super();
        this.profileUrl = profileUrl;
    }

    /**
     * Creates a new definition that points at the supplied profile URL and
     * uses the supplied factory to materialise profile instances.
     *
     * @param profileUrl    the Uniauth endpoint that returns the profile JSON
     *                      document, never {@code null}
     * @param profileFactory the factory used to produce
     *                        {@link UniauthTokenProfile} instances, never
     *                        {@code null}
     */
    public UniauthTokenProfileDefinition(String profileUrl, final ProfileFactory profileFactory) {
        super(profileFactory);
        this.profileUrl = profileUrl;
    }

    /**
     * Retrieves the static profile URL &mdash; the {@code accessToken} is
     * ignored because the Uniauth endpoint does not vary its URL based on
     * the supplied token.
     *
     * @param context     the current web context, never {@code null}
     * @param accessToken the Uniauth access token (ignored)
     * @return the profile URL passed to the constructor, never {@code null}
     */
    @Override
    public String getProfileUrl(WebContext context, UniauthToken accessToken) {
        return profileUrl;
    }

    /**
     * Extracts a Uniauth token profile from the JSON body returned by the
     * Uniauth profile endpoint.
     *
     * <p>If the body reports a failure (status {@code "fail"}), an
     * {@link HttpCommunicationException} carrying the supplied message is
     * thrown. Otherwise the {@code pinfo} sub-object is parsed into a
     * {@link UniauthTokenProfile}; any deserialisation error is wrapped in
     * a {@link TechnicalException}.</p>
     *
     * @param body the JSON body returned by the profile endpoint, never
     *             {@code null}
     * @return a {@link UniauthTokenProfile} populated from {@code pinfo},
     *         never {@code null}
     * @throws HttpCommunicationException if the body reports a failed status
     * @throws TechnicalException         if {@code pinfo} cannot be
     *                                    deserialised
     */
    @Override
    public UniauthTokenProfile extractUserProfile(String body) {

        JSONObject json = JSONObject.parseObject(body);
        /*
        {
            "msg": "系统证书校验失败，非法请求请,联系认证中心获得你的syskey!",
            "status": "fail"
        }*/
        if(StringUtils.equalsIgnoreCase(json.getString("status"), "fail")) {
            throw new HttpCommunicationException(json.getString("msg"));
        }

        final UniauthTokenProfile profileClass = (UniauthTokenProfile) this.newProfile();
        final UniauthTokenProfile profile;
        try {
            @SuppressWarnings("unchecked")
            final UniauthTokenProfile parsed = (UniauthTokenProfile) JSONObject.parseObject(json.getString("pinfo"), profileClass.getClass());
            profile = parsed;
        } catch (final Exception e) {
            throw new TechnicalException(e);
        }
        logger.debug("profile: {}", profile);
        return profile;
    }

}
