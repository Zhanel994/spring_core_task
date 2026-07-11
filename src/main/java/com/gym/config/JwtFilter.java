package com.gym.config;

import com.gym.services.JwtService;
import com.gym.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//spring security filter for JWT
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService; //to work with JWT
    private final UserService userService; //to work with UserDetails

    public JwtFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); //reads Authorization header (for example: Authorization: Bearer "token")

        //checks if it has token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); //to remove letters "Bearer "

        String username = jwtService.extractUsername(token);

        //checks if the user is authorized
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //gets user details from db
            UserDetails userDetails = userService.loadUserByUsername(username);

            //in order to let Spring Security to understand that the user is authorized
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            //authenticated
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        //passes the request on (to controller, service etc.)
        filterChain.doFilter(request, response);
    }
}
