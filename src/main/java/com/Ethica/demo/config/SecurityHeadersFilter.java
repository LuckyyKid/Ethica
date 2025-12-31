package com.Ethica.demo.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        // Content Security Policy - autorised  external ressources
        String csp = "default-src 'self'; " +
                "script-src 'self' https://cdn.jsdelivr.net https://www.youtube.com https://s.ytimg.com; " +
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                "font-src 'self' https://fonts.gstatic.com; " +
                "img-src 'self' data: https: http:; " +
                "frame-src 'self' https://www.youtube.com https://www.youtube-nocookie.com; " +
                "connect-src 'self' https://api.marketaux.com;";

        res.setHeader("Content-Security-Policy", csp);
        // Changed de DENY to allow  YouTube live content
        res.setHeader("X-Frame-Options", "SAMEORIGIN");
        res.setHeader("X-Content-Type-Options", "nosniff");

        chain.doFilter(request, response);
    }
}