package com.lpbici.security;

import com.lpbici.service.RolService;
import enums.RolNombre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.lpbici.service.RolService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RolService rolService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean hasRolVisit = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RolNombre.ROL_VISIT.name()));
        if (hasRolVisit) {
            response.sendRedirect("/producto/categorias");
        } else {
            response.sendRedirect("/producto/menuproducto");
        }
    }
}
