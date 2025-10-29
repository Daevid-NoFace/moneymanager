package com.david.moneymanager.services;

import java.util.Map;

import com.david.moneymanager.dto.AuthDTO;
import com.david.moneymanager.dto.ProfileDTO;
import com.david.moneymanager.entities.ProfileEntity;

public interface ProfileService {

    ProfileDTO registerProfile(ProfileDTO profileDTO);

    boolean activateProfile(String activationToken);

    boolean isAccountActive(String email);

    ProfileEntity getCurrentProfile();

    ProfileDTO getPublicProfile(String email);

    ProfileEntity toEntity(ProfileDTO profileDTO);

    ProfileDTO toDTO(ProfileEntity profileEntity);

    Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO);

}
