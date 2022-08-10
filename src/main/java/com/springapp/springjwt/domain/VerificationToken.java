package com.springapp.springjwt.domain;

import com.springapp.springjwt.constant.ActivationTokenConstant;

import javax.persistence.Entity;

@Entity
public class VerificationToken extends AbstractToken{
    private static final long serialVersionUID = ActivationTokenConstant.serialVersionUID;

    public VerificationToken() {
        super();
    }

    public VerificationToken(final String token) {
        super(token);
    }

    public VerificationToken(final String token, final User user) {
        super(token, user);
    }
}
