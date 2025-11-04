package com.david.moneymanager.repository;

import com.david.moneymanager.entities.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

    // Find by profile id order by date desc
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    // Find top 5 by profile id order by date desc
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    // Find total expense by profile id
    @Query("select sum(e.amount) from IncomeEntity e where e.profile.id = :profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);

    // Find by profile id and date between and name containing ignore case
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String name,
            Sort sort);

    // Find by profile id and date between
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}
