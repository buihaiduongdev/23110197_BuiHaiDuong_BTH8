package com.example.bth07.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.bth07.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(antMatcher("/"), antMatcher("/login"), antMatcher("/logout"), antMatcher("/home"),
						antMatcher("/css/**"), antMatcher("/js/**"), antMatcher("/images/**"),
						antMatcher("/webjars/**"), antMatcher("/h2-console/**"))
				.permitAll().requestMatchers(antMatcher("/category/**")).authenticated().anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.loginPage("/login").loginProcessingUrl("/login")
						.defaultSuccessUrl("/home", true).failureUrl("/login?error=true"))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login"))
				.csrf(csrf -> csrf.ignoringRequestMatchers(antMatcher("/h2-console/**")))
				.headers(headers -> headers.frameOptions().sameOrigin());
		return http.build();
	}
}
