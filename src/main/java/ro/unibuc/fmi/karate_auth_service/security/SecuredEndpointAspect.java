package ro.unibuc.fmi.karate_auth_service.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

@Aspect
@Component
public class SecuredEndpointAspect {
    @Around("@annotation(securedEndpoint)")
    public Object enforceRoles(ProceedingJoinPoint joinPoint, SecuredEndpoint securedEndpoint) throws Throwable {
        Role[] requiredRoles = securedEndpoint.roles();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !hasRequiredRoles(authentication, requiredRoles)) {
            throw new AccessDeniedException("You do not have the required roles!");
        }

        return joinPoint.proceed();
    }

    private boolean hasRequiredRoles(Authentication authentication, Role[] requiredRoles) {
        for (Role role : requiredRoles) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role.name()))) {
                return true;
            }
        }
        return false;
    }
}
