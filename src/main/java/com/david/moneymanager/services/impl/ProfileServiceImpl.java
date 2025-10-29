package com.david.moneymanager.services.impl;

import com.david.moneymanager.dto.ProfileDTO;
import com.david.moneymanager.entities.ProfileEntity;
import com.david.moneymanager.repository.ProfileRepository;
import com.david.moneymanager.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        return toDTO(newProfile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
            .id(profileDTO.getId())
            .fullname(profileDTO.getFullname())
            .email(profileDTO.getEmail())
            .password(profileDTO.getPassword())
            .profileImageUrl(profileDTO.getProfileImageUrl())
            .createdAt(profileDTO.getCreatedAt())
            .updatedAt(profileDTO.getUpdatedAt())
            .build();
    }

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
