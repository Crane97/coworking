package com.monforte.coworking;

import com.monforte.coworking.domain.entities.Role;
import com.monforte.coworking.services.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CoworkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoworkingApplication.class, args);
	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("*"));
//		configuration.setAllowedHeaders(Arrays.asList("*"));
//		configuration.setMaxAge(Duration.ofDays(1));
//		configuration.setAllowCredentials(Boolean.TRUE);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//Va a correr este código inmediatamente después de leer el aplication.properties
	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null, "ROLE_ANONYMOUS"));
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_PARTNER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));

		};
	}
}
