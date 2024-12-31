package ro.unibuc.fmi.karate_auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.unibuc.fmi.karate_auth_service.services.JwtTokenService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(

            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String tokenPrefix = "Bearer ";
        final String userEmail;

        log.debug("Processing request to URI: {}", request.getRequestURI());

        if (authorizationHeader == null || !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(tokenPrefix.length());
        log.debug("Extracted token from header: {}", token);

        userEmail = jwtTokenService.extractUserEmail(token);
        if (userEmail != null) {
            log.debug("Extracted user email from token: {}", userEmail);
        } else {
            log.warn("Failed to extract user email from token.");
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("No authentication found in SecurityContext. Validating token...");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtTokenService.isTokenValid(token, userDetails)) {
                log.info("Token is valid. Setting up authentication for user: {}", userEmail);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                log.warn("Token validation failed for user: {}", userEmail);
            }
        } else {
            log.debug("User email is null or authentication already exists in SecurityContext.");
        }

        log.debug("Continuing filter chain...");
        filterChain.doFilter(request, response);
    }
}