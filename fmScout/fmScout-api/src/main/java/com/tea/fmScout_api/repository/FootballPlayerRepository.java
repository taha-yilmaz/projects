package com.tea.fmScout_api.repository;

import com.tea.fmScout_api.model.FootballPlayer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FootballPlayerRepository extends JpaRepository<FootballPlayer, Long>, JpaSpecificationExecutor<FootballPlayer> {


    @Query("SELECT p FROM FootballPlayer  p WHERE p.playerId = ?1")
    Optional<FootballPlayer> findPlayerById(Long playerId);

    boolean existsFootballPlayerByName(String playerName);



    List<FootballPlayer> findAll(Sort sort);

    @Query("SELECT p FROM FootballPlayer p WHERE p.club.id = :clubId")
    List<FootballPlayer> findFootballPlayersByClub_Id(Long clubId);

    @Query("SELECT p FROM FootballPlayer p WHERE p.country.countryId = :countryId")
    List<FootballPlayer> findFootballPlayerByCountry_CountryId(Long countryId);

}
