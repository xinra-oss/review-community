package com.xinra.reviewcommunity.rest.conf;

import com.xinra.reviewcommunity.entity.PasswordLogin;
import com.xinra.reviewcommunity.repo.PasswordLoginRepository;
import com.xinra.reviewcommunity.service.AuthenticationProviderImpl;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  private @Autowired RestAuthenticationEntryPoint authenticationEntryPoint;
  private @Autowired RestAuthenticationSuccessHandler authenticationSuccessHandler;
  private @Autowired RestAuthenticationFailureHandler authenticationFailureHandler;
  private @Autowired RestLogoutSuccessHandler logoutSuccessHandler;
  private @Autowired DtoFactory dtoFactory;
  private @Autowired PasswordLoginRepository<PasswordLogin> passwordLoginRepo;
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(new AuthenticationProviderImpl(dtoFactory, passwordLoginRepo));
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
        .antMatchers("/").permitAll()
      .and()
        .formLogin()
        .loginProcessingUrl("/session")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
      .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/session", HttpMethod.DELETE.name()))
        .logoutSuccessHandler(logoutSuccessHandler)
      .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint);
    
  }
  
}
