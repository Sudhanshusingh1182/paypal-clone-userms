package com.paypal.userms.util;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwtToken);
			} catch (Exception e) {
				log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// username = null not authenticated
			if (jwtUtil.validateToken(jwtToken, username)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
						null);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// role check
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			if (jwtToken == null || jwtToken.isBlank()) {
				filterChain.doFilter(request, response);
				return; // skip processing if token is empty
			}

			try {
				username = jwtUtil.extractUsername(jwtToken);
				// Only extract role if JWT is valid and present
				String role = jwtUtil.extractRole(jwtToken);

				// use role for authorities as needed
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
						List.of(new SimpleGrantedAuthority(role)));

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);

			} catch (Exception e) {
				log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			}
		}
		
		filterChain.doFilter(request, response);

	}

}
