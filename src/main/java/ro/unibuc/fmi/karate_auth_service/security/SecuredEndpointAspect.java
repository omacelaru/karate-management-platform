package ro.unibuc.fmi.karate_auth_service.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

@Slf4j
@Aspect
@Component
public class SecuredEndpointAspect {
    @Around("@annotation(securedEndpoint)")
    public Object enforceRoles(ProceedingJoinPoint joinPoint, SecuredEndpoint securedEndpoint) throws Throwable {
        Role[] requiredRoles = securedEndpoint.roles();
        log.debug("Intercepting method: {}. Required roles: {}", joinPoint.getSignature().getName(), requiredRoles);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.warn("No authentication found. Access denied.");
            throw new AuthenticationCredentialsNotFoundException("You must be authenticated to access this resource!");
        }

        log.debug("Authentication found for user: {}. Checking roles...", authentication.getName());
        if (!hasRequiredRoles(authentication, requiredRoles)) {
            log.warn("User '{}' does not have the required roles: {}. Access denied.", authentication.getName(), requiredRoles);
            throw new AccessDeniedException("You do not have the required roles!");
        }

        log.info("Access granted to method '{}' for user '{}'.", joinPoint.getSignature().getName(), authentication.getName());
        return joinPoint.proceed();
    }

    private boolean hasRequiredRoles(Authentication authentication, Role[] requiredRoles) {
        log.debug("Checking if user '{}' has required roles: {}", authentication.getName(), requiredRoles);
        for (Role role : requiredRoles) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role.name()))) {
                log.debug("User '{}' has the required role: {}", authentication.getName(), role.name());
                return true;
            }
        }
        log.debug("User '{}' does not have any of the required roles: {}", authentication.getName(), requiredRoles);
        return false;
    }
}
