package com.monforte.coworking;

import com.monforte.coworking.domain.entities.Role;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.services.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
			Role role0 = userService.saveRole(new Role(null, "ROLE_ANONYMOUS"));
			Role role1 = userService.saveRole(new Role(null, "ROLE_USER"));
			Role role2 = userService.saveRole(new Role(null, "ROLE_PARTNER"));
			Role role3 = userService.saveRole(new Role(null, "ROLE_ADMIN"));

			userService.addUser(new User("Jorge", "Ruiz de la Torre", "jorge@macdiego.com", "+34 682 658 759", true, "crane", "root", true, "Informatico", true, "Descripcion: Soy informático",  Arrays.asList(role3), null, new ArrayList<>()));
			userService.addUser(new User("Alberto", "Monforte", "alberto@macdiego.com", "+34 600 411 006", true, "masclet", "root", true, "Industriales", false, "Descripcion: Soy industrial", Arrays.asList(role3), null, new ArrayList<>()));
			userService.addUser(new User("Canuto", "Ruiz de la Torre", "canuto@macdiego.com", "+34 612 321 333", false, "test", "root", false, "Telecomunicaciones", true, "Descripcion: Soy Telecomunicaciones", Arrays.asList(role0), null, new ArrayList<>()));
			userService.addUser(new User("Paula", "Ruiz de la Torre", "paula@macdiego.com", "+34 700 123 654", true, "test1", "root", true, "Socióloga", true, "Descripcion: Soy Socióloga", Arrays.asList(role2), null, new ArrayList<>()));
		};
	}
}
