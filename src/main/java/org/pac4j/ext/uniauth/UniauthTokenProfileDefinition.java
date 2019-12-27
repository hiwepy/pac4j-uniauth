/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.ext.profile.TokenProfileDefinition;

import com.alibaba.fastjson.JSONObject;

/**
 * Access Token profile definition.
 */
public class UniauthTokenProfileDefinition extends TokenProfileDefinition<UniauthTokenProfile, UniauthToken> {
	
	protected final String profileUrl;
	
	public UniauthTokenProfileDefinition(String profileUrl) {
		super();
		this.profileUrl = profileUrl;
	}

    public UniauthTokenProfileDefinition(String profileUrl, final Function<Object[], UniauthTokenProfile> profileFactory) {
        super(profileFactory);
        this.profileUrl = profileUrl;
    }
    
    
    /**
     * Retrieve the url of the profile of the authenticated user for the provider.
     *
     * @param accessToken only used when constructing dynamic urls from data in the token
     * @return the url of the user profile given by the provider
     */
    @Override
    public String getProfileUrl(WebContext context, UniauthToken accessToken) {
    	return profileUrl;
    }

    /**
     * Extract the user profile from the response (JSON, XML...) of the profile url.
     *
     * @param body the response body
     * @return the returned profile
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
		
    	final UniauthTokenProfile profileClass = this.newProfile();
        final UniauthTokenProfile profile;
        try {
            profile = JSONObject.parseObject(json.getString("pinfo"), profileClass.getClass());
        } catch (final Exception e) {
            throw new TechnicalException(e);
        }
        logger.debug("profile: {}", profile);
    	return profile;
    }
    
}
