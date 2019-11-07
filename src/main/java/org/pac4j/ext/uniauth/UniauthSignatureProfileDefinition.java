/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.ext.profile.SignatureProfileDefinition;

import com.alibaba.fastjson.JSONObject;

/**
 * Access Signature profile definition.
 */
public class UniauthSignatureProfileDefinition extends SignatureProfileDefinition<UniauthSignatureProfile, UniauthSignature> {
	
	public UniauthSignatureProfileDefinition() {
		super();
	}

    public UniauthSignatureProfileDefinition( final Function<Object[], UniauthSignatureProfile> profileFactory) {
        super(profileFactory);
    }
    
	/**
	 * TODO
	 * @author 		：<a href="https://github.com/vindell">wandl</a>
	 * @param payload
	 * @param signature
	 * @return
	 */
	
	@Override
	public UniauthSignatureProfile extractUserProfile(String payload, String signature) {
		JSONObject json = JSONObject.parseObject(payload);
		/*
		{
		    "msg": "系统证书校验失败，非法请求请,联系认证中心获得你的syskey!",
		    "status": "fail"
		}*/
		if(StringUtils.equalsIgnoreCase(json.getString("status"), "fail")) {
			throw new HttpCommunicationException(json.getString("msg"));
		}
		
    	final UniauthSignatureProfile profileClass = this.newProfile();
        final UniauthSignatureProfile profile;
        try {
            profile = JSONObject.parseObject(json.getString("pinfo"), profileClass.getClass());
        } catch (final Exception e) {
            throw new TechnicalException(e);
        }
        logger.debug("profile: {}", profile);
    	return null;
	}
    
}
