package com.david.moneymanager.services;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        ProfileEntity profileEntity = profileRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email" + email));
        
        return User.builder()
            .username(profileEntity.getEmail())
            .password(profileEntity.getPassword())
            .authorities(Collections.emptyList())
            .build();
    }
}
