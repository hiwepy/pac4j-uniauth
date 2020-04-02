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

import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.ext.profile.SignatureProfileDefinition;
import org.pac4j.core.profile.factory.ProfileFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * Access Signature profile definition.
 */
public class UniauthSignatureProfileDefinition extends SignatureProfileDefinition<UniauthSignatureProfile, UniauthSignature> {
	
	public UniauthSignatureProfileDefinition() {
		super();
	}

    public UniauthSignatureProfileDefinition( final ProfileFactory<UniauthSignatureProfile> profileFactory) {
        super(profileFactory);
    }
    
	@Override
	public UniauthSignatureProfile extractUserProfile(String payload, String signature) {
    	final UniauthSignatureProfile profileClass = this.newProfile();
        final UniauthSignatureProfile profile;
        try {
            profile = JSONObject.parseObject(payload, profileClass.getClass());
        } catch (final Exception e) {
            throw new TechnicalException(e);
        }
        logger.debug("profile: {}", profile);
    	return null;
	}
    
}
