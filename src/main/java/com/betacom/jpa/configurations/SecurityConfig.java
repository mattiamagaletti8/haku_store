package com.betacom.jpa.configurations;


import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// le nostre classi custom che gestiscono JWT ed errori di sicurezza
import com.betacom.jpa.security.ApiAccessDeniedHandler;
import com.betacom.jpa.security.ApiAuthEntryPoint;
import com.betacom.jpa.security.JwtAuthFilter;
import com.betacom.jpa.security.UtenteDetailsService;

import lombok.RequiredArgsConstructor; // genera in automatico il costruttore con i campi "final"

// Proprietario: Sara e Mattia
@RequiredArgsConstructor       // Lombok crea il costruttore che riceve i 4 campi final qui sotto
@EnableMethodSecurity          // abilita @PreAuthorize sui metodi dei controller (es. solo ADMIN)
@EnableWebSecurity             // attiva la sicurezza web di Spring su tutto il progetto
@Configuration                 
public class SecurityConfig {

	// filtro che legge e valida il token JWT ad ogni richiesta
	private final JwtAuthFilter jwtAuthFilter;
	// carica l'utente dal database durante il login
	private final UtenteDetailsService utenteDetailsService;
	// risponde quando manca il token o è invalido (errore 401)
	private final ApiAuthEntryPoint apiAuthEntryPoint;
	// risponde quando l'utente non ha i permessi giusti (errore 403)
	private final ApiAccessDeniedHandler apiAccessDeniedHandler;

	// cartella su disco delle immagini delle categorie, per servirle come file statici
	@Value("${upload.dir.categorie}")
	private String uploadDirCategorie;

	// cartella su disco del logo del sito, per servirlo come file statico
	@Value("${upload.dir.logo}")
	private String uploadDirLogo;

	// cartella su disco delle immagini dei prodotti, per servirle come file statici
	@Value("${upload.dir.prodotti}")
	private String uploadDirProdotti;

	// cartella su disco delle immagini delle varianti prodotto, per servirle come file statici
	@Value("${upload.dir.varianti}")
	private String uploadDirVarianti;

	@Bean
	WebMvcConfigurer corsConfigurer() {
		// configura il CORS: chi può chiamare il backend da un altro dominio/porta
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")                                   // vale per tutti gli endpoint
					.allowedOrigins("http://localhost:4200")             // solo il frontend Angular in locale
					.allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT") // metodi HTTP permessi
					.allowedHeaders("*")                                 // accetta qualsiasi header
					.allowCredentials(true);                             // permette di inviare cookie/credenziali
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				// tutto cio' che arriva su /immagini/categorie/** viene letto direttamente dalla cartella su disco
				registry.addResourceHandler("/immagini/categorie/**")
						.addResourceLocations("file:" + uploadDirCategorie + "/");
				// stesso meccanismo per il logo del sito, un file unico condiviso da tutti
				registry.addResourceHandler("/immagini/logo/**")
						.addResourceLocations("file:" + uploadDirLogo + "/");
				// e per le immagini dei prodotti
				registry.addResourceHandler("/immagini/prodotti/**")
						.addResourceLocations("file:" + uploadDirProdotti + "/");
				// e per le immagini delle varianti prodotto
				registry.addResourceHandler("/immagini/varianti/**")
						.addResourceLocations("file:" + uploadDirVarianti + "/");
			}
		};
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		// stessa configurazione CORS di sopra, ma nel formato richiesto da Spring Security
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
		config.setAllowedMethods(java.util.List.of("GET", "POST", "PATCH", "DELETE", "PUT"));
		config.setAllowedHeaders(java.util.List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // applica queste regole a tutti i path
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		// le password si salvano sempre cifrate con BCrypt, mai in chiaro
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		// oggetto che Spring usa internamente per gestire il login
		return config.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
		// dice a Spring: per autenticare un utente, usa UtenteDetailsService (per trovarlo)
		// e PasswordEncoder (per confrontare la password cifrata)
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(utenteDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// qui si decide, richiesta per richiesta, cosa è permesso e cosa no
		http
			.csrf(csrf -> csrf.disable())   // disabilita la protezione CSRF (non serve, l'API usa i token JWT)
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // applica le regole CORS definite sopra
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// STATELESS = niente sessioni salvate sul server, ogni richiesta si autentica da sola col token
			.exceptionHandling(e -> e
					.authenticationEntryPoint(apiAuthEntryPoint)     // token mancante/non valido -> 401
					.accessDeniedHandler(apiAccessDeniedHandler)     // permessi insufficienti -> 403
					)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/rest/auth/**").permitAll()   // login/registrazione: aperti a tutti
					.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // documentazione API: aperta
					.requestMatchers(HttpMethod.GET, "/rest/categoria/**", "/rest/prodotto/**", "/rest/varianteProdotto/**").permitAll()
					// consultare il catalogo (solo lettura) è pubblico, non serve login
					.requestMatchers(HttpMethod.GET, "/rest/recensione/list").permitAll()
					// anche leggere le recensioni è pubblico
					.requestMatchers(HttpMethod.GET, "/immagini/**").permitAll()
					// le immagini caricate (es. quelle delle categorie e il logo) sono visibili a chiunque
					.requestMatchers(HttpMethod.GET, "/rest/logo").permitAll()
					// chiunque puo' chiedere l'URL del logo attuale del sito
					.anyRequest().authenticated()   // tutto il resto richiede un login valido
					)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
			// il nostro filtro JWT controlla il token PRIMA del meccanismo di login classico di Spring

		return http.build(); // costruisce e restituisce la configurazione finale
	}
}