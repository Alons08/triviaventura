package com.tcna.primeraweb.security;

import com.tcna.primeraweb.services.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //esta anotación registra Beans en el contenedor de Spring Boot
@EnableWebSecurity //habilitar la seguridad en la app y poder usar la BD sin problemas
public class WebSecurityConfig {

    //Para tener al usuario en la fabrica de Spring
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    //Para encriptar la contraseña del ususario
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*Haremos una implementacion del AuthenticationProvider que se utiliza
        comunmente para autenticar usuarios en BD. Además, es el respondable
         de verificar las credenciales del usuario y autenticar el usuario*/
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //Luego de lo de arriba tenemos que registrarlo en el manejador de autenticacion
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authMB = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        //cuando se realiza una solicitud el administrador de autenticacion usara este provider
        authMB.authenticationProvider(authenticationProvider());
        return authMB.build();
    }

    //CREAMOS un FILTRO (define las reglas de autorizacion para las solicitudes HTTP)
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/", "/login", "/registro", "/registro/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // Rutas de administración (CRUD Categorías y Preguntas)
                        .requestMatchers("/categorias", "/categorias/**").hasAuthority("ADMIN")
                        .requestMatchers("/preguntas", "/preguntas/**").hasAuthority("ADMIN")

                        // Rutas de juego/trivia
                        .requestMatchers("/jugar", "/jugar/**").hasAnyAuthority("USER", "ADMIN")

                        // Ranking (accesible para todos los usuarios autenticados)
                        .requestMatchers("/ranking").authenticated()

                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/ranking", true) // Redirigir al ranking después del login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Asegúrate de que coincida con el enlace en tu navbar
                        .logoutSuccessUrl("/login?logout")  // Redirige a login con parámetro de logout
                        .invalidateHttpSession(true)  // Invalida la sesión
                        .deleteCookies("JSESSIONID")  // Elimina cookies de sesión
                        .permitAll()
                )
                .exceptionHandling(e -> e
                        .accessDeniedPage("/403")
                );

        return httpSecurity.build();
    }

}
