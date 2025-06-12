package com.tcna.primeraweb.services.impl;

import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.UserRepository;
import com.tcna.primeraweb.services.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = repository.getUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new MyUserDetails(user);
    }

}
