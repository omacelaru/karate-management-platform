package ro.unibuc.fmi.karate_management_platform.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.services.JwtTokenService;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

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

        try {
            String token = authorizationHeader.substring(tokenPrefix.length());
            log.debug("Extracting token from header: {}", token);

            userEmail = jwtTokenService.extractUserEmail(token);
            if (userEmail != null) {
                log.debug("Extracted user email from token: {}", userEmail);
            } else {
                log.warn("Failed to extract user email from token.");
                handleJwtError(response, "Invalid token");
                return;
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
                    handleJwtError(response, "Invalid token");
                    return;
                }
            } else {
                log.debug("User email is null or authentication already exists in SecurityContext.");
            }

            log.debug("Continuing filter chain...");
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            handleJwtError(response, "Token has expired");
        } catch (JwtException e) {
            log.warn("JWT token is invalid: {}", e.getMessage());
            handleJwtError(response, "Invalid token");
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
            handleJwtError(response, "Error processing token");
        }
    }

    private void handleJwtError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message,
                null,
                LocalDateTime.now().toString()
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}