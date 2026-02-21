package com.sunny.voting_backend.filter;

import com.sunny.voting_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Step 1: Grab the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Step 2 & 3: Sanity Check and Extraction
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Chops off "Bearer "
            username = jwtUtil.extractUsername(token); // Hands it to the translator
        }

        // Step 4: The Handshake (If we have a username but no existing authenticated session)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user details (including their roles)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Ask JwtUtil to do the math and validate the token
            if (jwtUtil.validateToken(token, userDetails)) {

                // Create the authentication token that Spring Security understands
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Attach the HTTP request details to this auth token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Final step: Shove it into the Security Context (The Bouncer opens the door)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // CRITICAL: Pass the request down the chain so it can actually hit your controller
        filterChain.doFilter(request, response);
    }
}
