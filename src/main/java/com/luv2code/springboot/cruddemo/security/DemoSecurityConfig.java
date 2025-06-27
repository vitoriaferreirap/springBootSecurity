package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration // spring reconhece que essa classe é uma configuração
public class DemoSecurityConfig {

    // autenticaçao da memoria
    /*
     * informar ao Spring que o método anotado retorna um objeto que deve ser
     * gerenciado pelo contêiner de injeção de dependência do Spring — ou seja, ele
     * vira um bean.
     */

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        // cria um usuário com nome de usuário "john" e senha "test123"
        UserDetails john = User.builder()
                .username("john")
                .password("{noop}test123")
                .roles("EMPLOYEE") // atribui a role "EMPLOYEE"
                .build();

        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}test123")
                .roles("EMPLOYEE", "MANAGER") // atribui as roles "EMPLOYEE" e "ADMIN"
                .build();

        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}test123")
                .roles("EMPLOYEE", "ADMIN") // atribui as roles "EMPLOYEE" e "ADMIN"
                .build();

        return new InMemoryUserDetailsManager(john, mary, susan); // retorna o gerenciador de usuários com o usuário
                                                                  // criado
    }

}
