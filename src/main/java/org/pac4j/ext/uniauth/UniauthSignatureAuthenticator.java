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

import org.pac4j.core.ext.credentials.SignatureCredentials;
import org.pac4j.core.ext.credentials.authenticator.SignatureAuthenticator;

/**
 * TODO
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class UniauthSignatureAuthenticator extends SignatureAuthenticator<SignatureCredentials, UniauthSignatureProfile, UniauthSignature> {
	
	@Override
    protected void internalInit() {
        defaultProfileDefinition(new UniauthSignatureProfileDefinition(x -> new UniauthSignatureProfile()));
        super.internalInit();
    }
    
}
