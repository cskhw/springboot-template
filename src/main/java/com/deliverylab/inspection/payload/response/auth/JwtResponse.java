package com.deliverylab.inspection.payload.response.auth;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
	private String type = "Bearer";
	private String accessToken;
	// private String refreshToken;
	private Long id;
	private String username;
	private List<String> roles;

	// public JwtResponse(Long id, String accessToken, String refreshToken, String
	// username, List<String> roles) {
	public JwtResponse(Long id, String username, String accessToken, List<String> roles) {
		this.id = id;
		this.username = username;
		this.accessToken = accessToken;
		// this.refreshToken = refreshToken;
		this.roles = roles;
	}

}
