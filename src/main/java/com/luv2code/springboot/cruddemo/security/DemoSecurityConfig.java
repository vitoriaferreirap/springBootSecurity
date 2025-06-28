package com.luv2code.springboot.cruddemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // spring reconhece que essa classe é uma configuração
public class DemoSecurityConfig {

    /*
     * Usando autenticação JDBC com nossa base de dados
     * Leitura dos usuario e as funções diretamente do banco de dados.
     */

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        /*
         * O UserDetailsManager é uma interface que fornece métodos para gerenciar
         * usuários e suas funções.
         * O JdbcUserDetailsManager é uma implementação dessa interface que usa JDBC
         * para acessar os dados ele permite configurar consultas SQL personalizadas
         * para buscar usuários e suas funções
         */

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Configura o esquema de autenticação JDBC
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT user_id, pw, active FROM members WHERE user_id = ?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT user_id, role FROM roles WHERE user_id = ?");

        return jdbcUserDetailsManager;
    }

    // Restingir o acesso com base em funçoes
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE") // acesso permitido apenas para
                                                                                       // usuario com role EMPLOYEE
                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN"));

        // USE HTTP BASIC AUTHENTICATION
        http.httpBasic(Customizer.withDefaults());

        // desabilita a proteçao CSRF
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

}
