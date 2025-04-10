package com.coresaken.memApp.auth.config;

import com.coresaken.memApp.auth.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length()<20){
            jwt = authHeader.substring(7);
        }else{
            jwt = getTokenFromCookie(request);

            if(jwt == null){
                filterChain.doFilter(request, response);
                return;
            }
        }

        try{
            userEmail = jwtService.extractUsername(jwt);
        }catch (MalformedJwtException | ExpiredJwtException e){
            filterChain.doFilter(request, response);
            return;
        }

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails;
            try{
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            }catch (UsernameNotFoundException e){
                Cookie cookie = new Cookie("jwt_token", null);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                filterChain.doFilter(request, response);
                return;
            }

            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else{
                Optional<Cookie> currentJwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt_token")).findFirst();

                if(currentJwtCookie.isPresent()){
                    Cookie deleteCookie = new Cookie("jwt_token", "");
                    deleteCookie.setPath(currentJwtCookie.get().getPath());
                    deleteCookie.setMaxAge(0);
                    response.addCookie(deleteCookie);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
