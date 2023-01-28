package com.deliverylab.inspection.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deliverylab.inspection.security.jwt.AuthEntryPointJwt;
import com.deliverylab.inspection.security.jwt.AuthTokenFilter;
import com.deliverylab.inspection.security.services.UserDetailsServiceImpl;

@Configuration
// @EnableWebSecurity
@EnableMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  // @Override
  // public void configure(AuthenticationManagerBuilder
  // authenticationManagerBuilder) throws Exception {
  // authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  // }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  // @Bean
  // @Override
  // public AuthenticationManager authenticationManagerBean() throws Exception {
  // return super.authenticationManagerBean();
  // }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // @Override
  // protected void configure(HttpSecurity http) throws Exception {
  // http.cors().and().csrf().disable()
  // .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
  // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
  // .authorizeRequests().antMatchers("/api/auth/**").permitAll()
  // .antMatchers("/api/test/**").permitAll()
  // .anyRequest().authenticated();
  //
  // http.addFilterBefore(authenticationJwtTokenFilter(),
  // UsernamePasswordAuthenticationFilter.class);
  // }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeHttpRequests().requestMatchers("/auth/**").permitAll()
        .requestMatchers("/test/**").permitAll()
        .requestMatchers("/log/**").permitAll()
        .requestMatchers("/kafka/log/**").permitAll()
        .anyRequest().authenticated();

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
