package com.deliverylab.inspection.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.deliverylab.inspection.security.services.UserDetailsServiceImpl;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  // 요청 보내기 전 필터체인들 설정함(미들웨어)
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      // jwt 토큰이 유효하면 인증토큰 세션 갱신함
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        // 시큐리티 콘텍스트에 authentication set해주고 jwt 생성
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        String newJwt = jwtUtils.generateTokenFromAuthentication(authentication);

        // 서버로 요청 시 사용할 수 있는 헤더
        response.setHeader("Access-Control-Allow-Headers",
            "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        // 브라우저에서 보이는 헤더
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        // 인증토큰 넣어줌
        response.setHeader("Authorization", "Bearer " + newJwt);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e);
    }
    filterChain.doFilter(request, response);
  }

  // 헤더에서 jwt 토큰 가져옴
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}
