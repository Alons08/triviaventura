package com.tcna.primeraweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer{

    /* Aquí se ponen las rutas (VISTAS) que se muestran a los usuarios
       que quieren acceder a un permiso que su rol no tiene permitido */

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
                                //(ruta en el navegador).setViewName(archivo html)
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/login").setViewName("login");
    }

}


