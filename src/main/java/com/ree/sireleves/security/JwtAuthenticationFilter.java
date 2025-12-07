// File: src/main/java/com/ree/sireleves/security/JwtAuthenticationFilter.java
package com.ree.sireleves.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import com.ree.sireleves.service.JwtService;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService){ this.jwtService = jwtService; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtService.parseToken(token).getBody();
                String uuid = claims.getSubject();
                String roles = claims.get("roles", String.class);
                var authorities = Arrays.stream(roles.split(","))
                        .filter(s->!s.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                var auth = new UsernamePasswordAuthenticationToken(uuid, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex){
                // invalid token -> clear context, but let Spring handle unauthorized later
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
