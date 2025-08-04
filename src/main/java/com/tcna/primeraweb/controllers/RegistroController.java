package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.Role;
import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.RoleRepository;
import com.tcna.primeraweb.repositories.UserRepository;
import com.tcna.primeraweb.services.MyUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/registro")
@RequiredArgsConstructor
public class RegistroController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("user", new User());
        return "registro"; //archivo html
    }

    @PostMapping
    public String registrarUsuario(@Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
        // Verificar si el username ya existe
        if (userRepository.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "El nombre de usuario ya está en uso");
        }
        if (bindingResult.hasErrors()) {
            return "registro"; //archivo html
        }

        // Asignar el rol JUGADOR por defecto
        Role jugadorRole = roleRepository.findByNombre("JUGADOR")
                .orElseThrow(() -> new RuntimeException("Rol JUGADOR no encontrado"));
        user.setRoles(Collections.singleton(jugadorRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        
        // Autenticar automáticamente al usuario recién registrado
        try {
            // Crear UserDetails del usuario recién guardado
            MyUserDetails userDetails = new MyUserDetails(savedUser);
            
            // Crear token de autenticación
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetails(request));
            
            // Establecer la autenticación en el contexto de seguridad
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authToken);
            SecurityContextHolder.setContext(securityContext);
            
            // Guardar el contexto de seguridad en la sesión HTTP
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            
            // Redirigir directamente al ranking tras autenticación exitosa
            return "redirect:/ranking";
        } catch (Exception e) {
            // Si falla la autenticación automática, redirigir al login
            return "redirect:/login?registroExitoso";
        }
    }

}