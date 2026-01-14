package com.app.examproject.commons.security;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.springframework.security.access.AccessDeniedException;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireRoleAspect {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RequireRoleAspect.class);

    private final SecurityExpressionService security;

    @Before("@annotation(require)")
    public void checkAccess(JoinPoint jp, Require require) {
        boolean allowed = security.check(require.value());

        if (!allowed) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
