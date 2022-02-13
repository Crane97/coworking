package com.monforte.coworking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.monforte.coworking.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // TODO: next episode
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*" ).permitAll()
                .antMatchers("/api/**").hasRole(ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails jorgeUser = User.builder()
                .username("jorge")
                .password(passwordEncoder.encode("root"))
                .roles(PARTNER.name()) //ROLE_PARTNER
                .build();

        UserDetails albertoUser = User.builder()
                .username("alberto")
                .password(passwordEncoder.encode("root"))
                .roles(ADMIN.name()) //ROLE_ADMIN
                .build();

        return new InMemoryUserDetailsManager(
                jorgeUser,
                albertoUser
        );
    }
}
