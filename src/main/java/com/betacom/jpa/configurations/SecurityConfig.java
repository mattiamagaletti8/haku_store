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

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Il punto dove tutta la sicurezza viene assemblata in una SecurityFilterChain unica
@RequiredArgsConstructor
// Abilita @PreAuthorize sui metodi dei controller (senza questa annotazione,
// @PreAuthorize("hasRole('ADMIN')") sparso per il codice non avrebbe alcun effetto)
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final UtenteDetailsService utenteDetailsService;
	private final ApiAuthEntryPoint apiAuthEntryPoint;
	private final ApiAccessDeniedHandler apiAccessDeniedHandler;

	// Configurazione CORS "vecchio stile" via WebMvcConfigurer: permette le chiamate
	// dal frontend Angular in sviluppo (localhost:4200)
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

	// Stessa configurazione CORS, ma nella forma richiesta dalla SecurityFilterChain sottostante
	// (le due configurazioni coesistono perche' servono a due livelli diversi dello stack Spring)
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

	// Bean usato per hashare le password in UtenteImpl.create/update e per verificarle in AuthImpl.login
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	// Collega UtenteDetailsService (come sapere cercare un utente) e PasswordEncoder (come verificarne
	// la password): il pezzo che permette a Spring di sapere COME autenticare un utente
	@Bean
	DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(utenteDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// CSRF disabilitato: non serve per un'API stateless consumata da un frontend separato
			// (il rischio CSRF esiste per le sessioni basate su cookie, non per i JWT nell'header)
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			// STATELESS: nessuna sessione server-side, coerente con un'autenticazione JWT
			// (ogni richiesta si autentica da sola tramite il token, il server non ricorda nulla tra una richiesta e l'altra)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// I due handler custom di Sarah per 401 (non autenticato) e 403 (autenticato ma ruolo sbagliato)
			.exceptionHandling(e -> e
					.authenticationEntryPoint(apiAuthEntryPoint)
					.accessDeniedHandler(apiAccessDeniedHandler)
					)
			// Regole di accesso, valutate in ordine: auth/swagger sempre pubblici, poi le GET pubbliche
			// del catalogo/recensioni (modulo di Mattia e Sarah), tutto il resto richiede autenticazione
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/rest/auth/**").permitAll()
					.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
					.requestMatchers(HttpMethod.GET, "/rest/categoria/**", "/rest/prodotto/**", "/rest/varianteProdotto/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/rest/recensione/list").permitAll()
					.anyRequest().authenticated()
					)
			// JwtAuthFilter inserito PRIMA del filtro standard di Spring: cosi' il SecurityContext
			// e' gia' popolato quando la richiesta arriva ai controller
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
