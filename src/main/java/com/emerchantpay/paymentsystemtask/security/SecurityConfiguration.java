package com.emerchantpay.paymentsystemtask.security;


import com.emerchantpay.paymentsystemtask.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final String MERCHANT = "ROLE_MERCHANT";
	private static final String ADMIN = "ROLE_ADMIN";

	@Autowired
	UserAccountService userService;


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		httpSecurity.csrf(csrf -> csrf.disable()).
		authorizeHttpRequests(authz -> authz
				.requestMatchers("/payments").authenticated()
				.requestMatchers("/alerts/**").hasAuthority(ADMIN)
				.requestMatchers("/merchants/**").hasAuthority(ADMIN)
				.requestMatchers("/transactions/**").hasAuthority(MERCHANT))

				.authenticationManager(authenticationManager)
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(ss-> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		;

		return httpSecurity.build();
	}
}