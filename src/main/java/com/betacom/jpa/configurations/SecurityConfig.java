package com.betacom.jpa.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.betacom.jpa.security.ApiAccessDeniedHandler;
import com.betacom.jpa.security.ApiAuthEntryPoint;
import com.betacom.jpa.security.JwtAuthFilter;
import com.betacom.jpa.security.UtenteDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final UtenteDetailsService utenteDetailsService;
	private final ApiAuthEntryPoint apiAuthEntryPoint;
	private final ApiAccessDeniedHandler apiAccessDeniedHandler;

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")
					.allowedOrigins("http://localhost:4200")
					.allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT")
					.allowedHeaders("*")
					.allowCredentials(true);
			}
		};
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
		config.setAllowedMethods(java.util.List.of("GET", "POST", "PATCH", "DELETE", "PUT"));
		config.setAllowedHeaders(java.util.List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(utenteDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(e -> e
					.authenticationEntryPoint(apiAuthEntryPoint)
					.accessDeniedHandler(apiAccessDeniedHandler)
					)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/rest/auth/**").permitAll()
					.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
					.requestMatchers(HttpMethod.GET, "/rest/categoria/**", "/rest/prodotto/**", "/rest/varianteProdotto/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/rest/recensione/list").permitAll()
					.anyRequest().authenticated()
					)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
