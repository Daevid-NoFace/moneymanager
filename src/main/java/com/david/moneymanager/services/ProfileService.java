package com.david.moneymanager.services;

import com.david.moneymanager.dto.ProfileDTO;

public interface ProfileService {

    ProfileDTO registerProfile(ProfileDTO profileDTO);

    boolean activateProfile(String activationToken);
}
