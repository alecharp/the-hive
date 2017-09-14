package com.cloudbees.hive.config;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception when someone outside of the authorized domain tries to connect.
 *
 * @author Adrien Lecharpentier
 */
class AccountRejectedException extends AuthenticationException {
    AccountRejectedException(String msg) {
        super(msg);
    }
}
