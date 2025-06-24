package com.postora.postora_backend.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	
	private JwtEncoder jwtEncoder;
	
	public JwtService(JwtEncoder jwtEncoder) {
		super();
		this.jwtEncoder = jwtEncoder;
	}

	public String createToken(Authentication authentication) {
		
		var claims = JwtClaimsSet.builder()
								 .issuer("self")
								 .issuedAt(Instant.now())
								 .expiresAt(Instant.now().plusSeconds(60*30))
								 .subject(authentication.getName())
								 .claim("scope", createScope(authentication))
								 .build();
		
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
	}

	private String createScope(Authentication authentication) {
		return authentication.getAuthorities()
							 .stream()
							 .map(a -> a.getAuthority()) //returns a stream of GrantedAuthority objects
							 .collect(Collectors.joining(" "));
	}
	
}
