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
        comunmente para autenticar usuarios en BD. Además, es el responsable
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
                // Se definen las reglas de acceso a las rutas
                .authorizeHttpRequests(auth -> auth
                        //rutas públicas
                        .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll()

                        //rutas de administración (CRUD Categorías, Preguntas y Usuarios)
                        .requestMatchers("/categorias", "/categorias/**").hasAnyAuthority("ADMIN", "COLABORADOR")
                        .requestMatchers("/preguntas", "/preguntas/**").hasAnyAuthority("ADMIN","COLABORADOR")
                        .requestMatchers("/usuarios", "/usuarios/**").hasAuthority("ADMIN")

                        //rutas de juego (trivia)
                        .requestMatchers("/jugar", "/jugar/**").hasAnyAuthority("JUGADOR", "ADMIN", "COLABORADOR")

                        //ranking (accesible para todos los usuarios autenticados)
                        .requestMatchers("/ranking").authenticated()

                        //todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                // Configura el manejo del inicio de sesión en la aplicación.
                .formLogin(form -> form
                        .loginPage("/login") /*ENNN "WebMvcConfigurer" vinculo el endpoint a un HTML*/
                        .defaultSuccessUrl("/ranking", true) //redirigir al ranking después del login
                        .permitAll() //permite el acceso al cierre de sesión para todos.
                )
                // Configura el manejo del cierre de sesión en la aplicación.
                .logout(logout -> logout
                        .logoutUrl("/logout")  //define la URL para realizar el cierre de sesión
                        .logoutSuccessUrl("/login?logout")  //redirige a login con parámetro de logout
                        .invalidateHttpSession(true)  //invalida la sesión
                        .deleteCookies("JSESSIONID")  //elimina cookies de sesión
                        .permitAll() //permite el acceso al cierre de sesión para todos.
                )
                // Configura el manejo de excepciones relacionadas con el acceso denegado
                .exceptionHandling(e -> e
                        .accessDeniedPage("/403") /*ENNN "WebMvcConfigurer" vinculo el endpoint a un HTML*/
                );

        return httpSecurity.build();
    }

}
