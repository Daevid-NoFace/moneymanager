package com.david.moneymanager.repository;

import com.david.moneymanager.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    // select * from tbl_profile where email = ?1
    Optional<ProfileEntity> findByEmail(String email);

    // select * from tbl_profile where activation_token = ?1
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
