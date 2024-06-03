package com.example.todolist.security.jwt.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.fusionauth.jwt.InvalidJWTException;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

@Component
@PropertySource(value = {"classpath:jwt.properties"})
public class JWTUtils {

	@Value("${jwt.token.secret.key}")
	private String secretKey;
	@Value("${jwt.token.expiration}")
	private Long expiration;
	@Value("${jwt.token.issuer}")
	private String issuer;
	
	private ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
	
	public String generateToken(String username, List<String> roles) {
		Assert.hasText(username, "Username is required.");
		Assert.notEmpty(roles, "Role list cannot be empty.");
        roles.forEach(role -> Assert.hasText(role, "Role is required."));
		return createToken(username, roles);
	}

	private String createToken(String username, List<String> roles) {
		Signer signer = HMACSigner.newSHA256Signer(secretKey);
		JWT jwt = new JWT().setIssuer("dukbong").setIssuedAt(now).setSubject(username).setExpiration(now.plusSeconds(expiration)).addClaim("username", username).addClaim("roles", roles);
		return JWT.getEncoder().encode(jwt, signer);
	}
	
	public JWT validateToken(String token) {
		Verifier verifier = HMACVerifier.newVerifier(secretKey);
	    JWT jwt = null;
	    
	    try {
	        jwt = JWT.getDecoder().decode(token, verifier);
	    } catch (InvalidJWTException e) {
	        throw new InvalidJWTException("Invalid token: " + e.getMessage());
	    }
	    
		if(jwt.expiration.isBefore(now)) {
			throw new InvalidJWTException("The token has expired.");
		}
		
		if(!jwt.issuer.equals(issuer)) {
			throw new IllegalArgumentException("The issuer is wrong.");
		}
		
		return jwt;
	}
}
