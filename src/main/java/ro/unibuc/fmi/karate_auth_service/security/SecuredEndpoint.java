package ro.unibuc.fmi.karate_auth_service.security;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "bearerAuth")
public @interface SecuredEndpoint {
    Role[] roles() default {Role.USER};
}
