package com.paypal.userms.util;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private static final String SECRET = "secret123secret123secret123secret123";

	private Key getSiginKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	public String extractEmail(String token) {
		return Jwts.parser().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token, String username) {
		try {
			extractEmail(token); // If parsing succeeds, token is valid
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String extractUsername(String token) { // assuming email is username
		return Jwts.parser().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public String generateToken(Map<String, Object> claims, String email) {
		return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(getSiginKey(), SignatureAlgorithm.HS256).compact();
	}

	// role based access control
	public String extractRole(String token) {
		return Jwts.parser().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody().get("role")
				.toString();
	}
}
