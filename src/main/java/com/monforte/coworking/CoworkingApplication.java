package com.monforte.coworking;

import com.monforte.coworking.domain.entities.Company;
import com.monforte.coworking.domain.entities.Role;
import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.domain.entities.enums.RoomType;
import com.monforte.coworking.services.impl.CompanyService;
import com.monforte.coworking.services.impl.RoomService;
import com.monforte.coworking.services.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

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
	CommandLineRunner run(UserService userService, RoomService roomService, CompanyService companyService){
		return args -> {
			Role role0 = userService.saveRole(new Role(null, "ROLE_ANONYMOUS"));
			Role role1 = userService.saveRole(new Role(null, "ROLE_USER"));
			Role role2 = userService.saveRole(new Role(null, "ROLE_PARTNER"));
			Role role3 = userService.saveRole(new Role(null, "ROLE_ADMIN"));

			User user1 = userService.addUser(new User("Jorge", "Ruiz de la Torre", "jorge@macdiego.com", "+34 682 658 759", true, "crane", "root", true, "Informatico", true, "Descripcion: Soy informático", "IMAGE", null, null, null, Arrays.asList(role3), new ArrayList<>()));
			userService.addUser(new User("Alberto", "Monforte", "alberto@macdiego.com", "+34 600 411 006", true, "masclet", "root", true, "Industriales", false, "Descripcion: Soy industrial", "IMAGE", "sub", null, null, Arrays.asList(role3), new ArrayList<>()));
			userService.addUser(new User("Canuto", "Ruiz de la Torre", "canuto@macdiego.com", "+34 612 321 333", false, "test", "root", false, "Telecomunicaciones", true, "Descripcion: Soy Telecomunicaciones", "IMAGE", null, null, null, Arrays.asList(role0), new ArrayList<>()));
			userService.addUser(new User("Paula", "Ruiz de la Torre", "paula@macdiego.com", "+34 700 123 654", true, "test1", "root", true, "Socióloga", true, "Descripcion: Soy Socióloga", "IMAGE", null, null, null, Arrays.asList(role2), new ArrayList<>()));
			User user2 = userService.addUser(new User("Javier", "Romeu", "javier@macdiego.com", "+34 700 123 654", true, "test2", "root", true, "Químico", true, "Descripcion: Soy Ingeniero Químico", "IMAGE", null, null, null, Arrays.asList(role2), new ArrayList<>()));
			userService.addUser(new User("Pilar", "González", "pilar@macdiego.com", "+34 700 123 654", true, "test3", "root", true, "Profesora", true, "Descripcion: Soy Profesora de infantil", "IMAGE", null, null, null, Arrays.asList(role2), new ArrayList<>()));

			roomService.addRoom(new Room("Sala Ada Lovelace", 8, RoomType.REUNION, "https://www.equipamientointegraldeoficinas.com/wp-content/uploads/sala-de-reunion.jpg", new ArrayList<>()));
			roomService.addRoom(new Room("Sala Richard Stallman", 4, RoomType.FIXED, "https://www.torreombu.com/blog/wp-content/uploads/2021/07/shutterstock_1028952010-1.jpg", new ArrayList<>()));
			roomService.addRoom(new Room("Sala Alan Turing", 15, RoomType.FLEXIBLE, "https://atrapatuled.es/modules/amazzingblog/views/img/uploads/posts/30/xl/2-609baa4c0972e.jpg", new ArrayList<>()));

			companyService.addCompany(new Company("Innovators","1-10", "Software", "https://www.inesem.es/revistadigital/gestion-empresarial/files/2015/07/tips-para-hacer-un-logo-1020x680.jpg", true, user1.getId(),user1.getName() + " " + user1.getSurname()));
			companyService.addCompany(new Company("Coworkers","10-50", "Start-up, administrativo", "https://w7.pngwing.com/pngs/140/543/png-transparent-logo-company-business-business-blue-angle-company.png", false, user2.getId(),user2.getName() + " " + user2.getSurname()));
		};
	}
}
