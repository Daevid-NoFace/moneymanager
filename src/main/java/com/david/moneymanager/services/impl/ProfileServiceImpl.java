package com.david.moneymanager.services.impl;

import com.david.moneymanager.dto.AuthDTO;
import com.david.moneymanager.dto.ProfileDTO;
import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.repository.ProfileRepository;
import com.david.moneymanager.services.EmailService;
import com.david.moneymanager.services.ProfileService;
import com.david.moneymanager.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final EmailService emailService;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        
        // Send activation link
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your account";
        String text = "Please click the link below to activate your account: " + activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject, text);

        return toDTO(newProfile);
    }

    @Override
    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
            .map(profile -> {
                profile.setIsActive(true);
                profileRepository.save(profile);
                return true;
            })
            .orElse(false);
    }

    @Override
    public boolean isAccountActive(String email) {
        return profileRepository.findByEmail(email)
        .map(ProfileEntity::getIsActive)
        .orElse(false);
    }

    @Override
    public ProfileEntity getCurrentProfile() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return profileRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + authentication.getName()));
    }

    @Override
    public ProfileDTO getPublicProfile(String email) {
        
        ProfileEntity currentUser = null;
        
        if (email == null) {
            currentUser = getCurrentProfile();
        } else {
            currentUser = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
        }

        return toDTO(currentUser);
    }

    @Override
    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            
            // Generate JWT token
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of(
                "token", token,
                "user", getPublicProfile(authDTO.getEmail())
            );

        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
            .id(profileDTO.getId())
            .fullname(profileDTO.getFullname())
            .email(profileDTO.getEmail())
            .password(passwordEncoder.encode(profileDTO.getPassword()))
            .profileImageUrl(profileDTO.getProfileImageUrl())
            .createdAt(profileDTO.getCreatedAt())
            .updatedAt(profileDTO.getUpdatedAt())
            .build();
    }

    @Override
    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        return ProfileDTO.builder()
            .id(profileEntity.getId())
            .fullname(profileEntity.getFullname())
            .email(profileEntity.getEmail())
            .profileImageUrl(profileEntity.getProfileImageUrl())
            .createdAt(profileEntity.getCreatedAt())
            .updatedAt(profileEntity.getUpdatedAt())
            .build();
    }
}
