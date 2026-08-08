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

import org.pac4j.core.ext.profile.SignatureProfile;

/**
 * Profile populated from a Uniauth signature-credential response.
 *
 * <p>Each instance mirrors the attributes exposed by the Uniauth single
 * sign-on backend &mdash; notably the campus-wide {@code pid} (unified
 * person identifier), the per-system {@code userid}, the display name
 * {@code xm}, the person type ({@code ptype}), the date of birth
 * ({@csrq}) and the credential initialisation flag ({@code flag}).
 * The {@link #getId()} method returns the {@code pid}, which is the
 * canonical identifier for Uniauth subjects.</p>
 *
 * <p>The class is a plain data carrier: setters simply update the
 * corresponding field, and {@code toString()} is inherited from
 * {@link org.pac4j.core.profile.CommonProfile}.</p>
 *
 * @author Loong Wan
 * @since 3.0.0
 * @see SignatureProfile
 * @see UniauthSignature
 */
public class UniauthSignatureProfile extends SignatureProfile {

    /**
     * Per-system user id (login name) &mdash; unique within a single Uniauth
     * realm but not across realms.
     */
    private String userid;

    /**
     * Unified person identifier shared across the Uniauth federation.
     * Used as the canonical profile id (see {@link #getId()}).
     */
    private String pid;

    /**
     * Display name of the authenticated person (Pinyin: <em>x&iacute;ngm&iacute;ng</em>).
     */
    private String xm;

    /**
     * Person type discriminator; one of {@code "student"} or {@code "staff"}.
     */
    private String ptype;

    /**
     * Date of birth in the Uniauth canonical format
     * ({@code yyyy-MM-dd}, Pinyin: <em>ch&#601;nsh&#601;ng r&igrave;</em>).
     */
    private String csrq;

    /**
     * Password initialisation flag &mdash;
     * {@code "0"} when the credential has never been initialised,
     * {@code "1"} after the user has set a password.
     */
    private String flag;

    /**
     * Returns the canonical profile identifier.
     *
     * @return the value of {@link #pid}, or {@code null} when not populated
     */
    @Override
    public String getId() {
        return pid;
    }

    /**
     * Returns the per-system user id.
     *
     * @return the userid field, may be {@code null}
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Sets the per-system user id.
     *
     * @param userid the new user id, may be {@code null}
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * Returns the unified person identifier.
     *
     * @return the pid field, may be {@code null}
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the unified person identifier. This also becomes the value
     * returned by {@link #getId()}.
     *
     * @param pid the new pid, may be {@code null}
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * Returns the display name.
     *
     * @return the xm field, may be {@code null}
     */
    public String getXm() {
        return xm;
    }

    /**
     * Sets the display name.
     *
     * @param xm the new display name, may be {@code null}
     */
    public void setXm(String xm) {
        this.xm = xm;
    }

    /**
     * Returns the person type.
     *
     * @return the ptype field ({@code "student"} / {@code "staff"}), may be
     *         {@code null}
     */
    public String getPtype() {
        return ptype;
    }

    /**
     * Sets the person type.
     *
     * @param ptype the new person type, may be {@code null}
     */
    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    /**
     * Returns the date of birth string.
     *
     * @return the csrq field, may be {@code null}
     */
    public String getCsrq() {
        return csrq;
    }

    /**
     * Sets the date of birth string.
     *
     * @param csrq the new date of birth, may be {@code null}
     */
    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    /**
     * Returns the password-initialisation flag.
     *
     * @return the flag field, typically {@code "0"} or {@code "1"}, may be
     *         {@code null}
     */
    public String getFlag() {
        return flag;
    }

    /**
     * Sets the password-initialisation flag.
     *
     * @param flag the new flag value, may be {@code null}
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

}
