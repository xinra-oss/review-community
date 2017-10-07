package com.xinra.reviewcommunity.rest.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  private @Autowired RestAuthenticationEntryPoint authenticationEntryPoint;
  private @Autowired RestAuthenticationSuccessHandler authenticationSuccessHandler;
  private @Autowired RestAuthenticationFailureHandler authenticationFailureHandler;
  private @Autowired RestLogoutSuccessHandler logoutSuccessHandler;
  
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
  
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
  }
  
}
