package com.deliverylab.inspection.controllers;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverylab.inspection.exception.RoleNotFoundException;
import com.deliverylab.inspection.models.Role;
import com.deliverylab.inspection.models.User;
import com.deliverylab.inspection.models.enums.ERole;
import com.deliverylab.inspection.payload.request.auth.SigninRequest;
import com.deliverylab.inspection.payload.request.auth.SignupRequest;
import com.deliverylab.inspection.payload.request.auth.ValidRequest;
import com.deliverylab.inspection.payload.response.MessageResponse;
import com.deliverylab.inspection.payload.response.auth.JwtResponse;
import com.deliverylab.inspection.repository.RoleRepository;
import com.deliverylab.inspection.repository.UserRepository;
import com.deliverylab.inspection.security.jwt.JwtUtils;
import com.deliverylab.inspection.security.services.RefreshTokenService;
import com.deliverylab.inspection.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)

@ControllerAdvice
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;

	// auth api 체크용
	@GetMapping("/check")
	public ResponseEntity<?> check() {
		return ResponseEntity.ok("auth check." + Instant.now());
	}

	// access-token 유효성 체크
	@PostMapping("/valid")
	public ResponseEntity<?> valid(@Valid @RequestBody ValidRequest req) {
		String accessToken = req.getAccessToken();
		if (jwtUtils.validateJwtToken(accessToken))
			return ResponseEntity.ok("Access token is valid!");
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	// access_token 새로 발급
	// @PostMapping("/refresh")
	// public ResponseEntity<?> refresh(@Valid @RequestBody TokenRefreshRequest
	// request) {
	// String requestRefreshToken = request.getRefreshToken();

	// return refreshTokenService.findByToken(requestRefreshToken)
	// .map(refreshTokenService::verifyExpiration)
	// .map(RefreshToken::getUser)
	// .map(user -> {
	// String token = jwtUtils.generateTokenFromUsername(user.getUsername());
	// return ResponseEntity.ok(new TokenRefreshResponse(token,
	// requestRefreshToken));
	// })
	// .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
	// "Refresh token is not in database!"));
	// }

	// 로그인
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest loginRequest) {

		// authentication으로 토큰 생성
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
						loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(
				userDetails.getId(), userDetails.getUsername(), jwt, roles));
	}

	// 회원가입
	@PostMapping("/signup")

	public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body("Error: Username is already taken!");
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "ROLE_ADMIN":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					case "ROLE_MODERATOR":
						Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
