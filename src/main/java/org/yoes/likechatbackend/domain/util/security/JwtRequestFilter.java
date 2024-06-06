package org.yoes.likechatbackend.domain.util.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yoes.likechatbackend.domain.util.security.token.JwtUtil;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Eliminar el prefijo "Bearer "
            // Validar el token JWT
            if (jwtUtil.validateToken(token)) {
                // Si el token es válido, permitir que la solicitud continúe
                filterChain.doFilter(request, response);
                return;
            } else {
                // Si el token no es válido, devolver un error de no autorizado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // Si no se proporciona ningún token en el encabezado de autorización, devolver un error de no autorizado
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
