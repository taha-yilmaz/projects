package com.tea.fmScout_api.controller;

import com.tea.fmScout_api.dto.FootballPlayerDto;
import com.tea.fmScout_api.dto.request.AddFootballPlayerRequest;
import com.tea.fmScout_api.model.FootballPlayer;
import com.tea.fmScout_api.model.League;
import com.tea.fmScout_api.model.Specification.*;
import org.springframework.data.jpa.domain.Specification;
import com.tea.fmScout_api.service.FootballPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/football-players")
@Validated
public class FootballPlayerController {

    private final FootballPlayerService footballPlayerService;

    public FootballPlayerController(FootballPlayerService footballPlayerService) {
        this.footballPlayerService = footballPlayerService;
    }
    @GetMapping("/test")
    public ResponseEntity<String> getFootballPlayerById() {
        return ResponseEntity.ok("test");
    }

    @GetMapping("/{id}")
    public ResponseEntity<FootballPlayerDto> getFootballPlayerById(@PathVariable Long id) {
        FootballPlayerDto playerDto = footballPlayerService.getFootballPlayerById(id);
        return ResponseEntity.ok(playerDto);
    }

    @GetMapping
    public ResponseEntity<List<FootballPlayerDto>> getAllFootballPlayers() {
        List<FootballPlayerDto> players = footballPlayerService.getAllFootballPlayers("playerId", "asc");
        return ResponseEntity.ok(players);
    }

    @PostMapping
    public ResponseEntity<FootballPlayerDto> createFootballPlayer(@Valid @RequestBody AddFootballPlayerRequest createRequest) {
        FootballPlayerDto newPlayer = footballPlayerService.createFootballPlayer(createRequest);
        return ResponseEntity.ok(newPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FootballPlayerDto> updateFootballPlayer(@PathVariable Long id,
                                                                  @Valid @RequestBody AddFootballPlayerRequest updateRequest) {
        FootballPlayerDto updatedPlayer = footballPlayerService.updateFootballPlayer(id, updateRequest);
        return ResponseEntity.ok(updatedPlayer);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<FootballPlayerDto>> getAllSortedPlayers(
            @RequestParam(name = "sortBy", defaultValue = "playerId") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder
    ){
        List<FootballPlayerDto> sortedPlayers = footballPlayerService.getAllFootballPlayers(sortBy, sortOrder);
        return ResponseEntity.ok(sortedPlayers);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FootballPlayerDto>> filterPlayers(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String countryName,
            @RequestParam(required = false) String clubName,
            @RequestParam(required = false) String leagueName,
            @RequestParam(required = false) List<Integer> positionIds,
            @RequestParam(required = false) Integer minMarketValue,
            @RequestParam(required = false) Integer maxMarketValue,
            @RequestParam(required = false) Integer minCa,
            @RequestParam(required = false) Integer maxCa,
            @RequestParam(required = false) Integer minPa,
            @RequestParam(required = false) Integer maxPa) {

        List<Specification<FootballPlayer>> specs = new ArrayList<>();

        if (minAge != null || maxAge != null) {
            minAge = minAge == null ? 16 : minAge;
            maxAge = maxAge == null ? 45 : maxAge;
            specs.add(new AgeSpecification(minAge, maxAge));
        }
        if (positionIds != null && !positionIds.isEmpty()) {
            specs.add(new PositionSpecification(positionIds));
        }
        if (countryName != null) {
            specs.add(new CountrySpecification(countryName));
        }
        if (clubName != null) {
            specs.add(new ClubSpecification(clubName));
        }
        if (leagueName != null) {
            specs.add(new LeagueSpecification(leagueName));
        }
        if (minMarketValue != null || maxMarketValue != null) {
            minMarketValue = minMarketValue == null ? 0 : minMarketValue;
            maxMarketValue = maxMarketValue == null ? 400_000_000 : maxMarketValue;
            specs.add(new MarketValueSpecification(minMarketValue, maxMarketValue));
        }
        if (minCa != null || maxCa != null) {
            minCa = minCa == null ? 0 : minCa;
            maxCa = maxCa == null ? 200 : maxCa;
            specs.add(new CaSpecification(minCa, maxCa));
        }
        if (minPa != null || maxPa != null) {
            minPa = minPa == null ? 0 : minPa;
            maxPa = maxPa == null ? 200 : maxPa;
            specs.add(new PaSpecification(minPa, maxPa));
        }

        List<FootballPlayerDto> filteredPlayers = footballPlayerService.filterPlayers(specs);
        return ResponseEntity.ok(filteredPlayers);
    }

    // Wonderkids
    @GetMapping("/wonderkids")
    public ResponseEntity<List<FootballPlayerDto>> getWonderkids() {
        return ResponseEntity.ok(footballPlayerService.getWonderkids());
    }

    // Free Transfers
    @GetMapping("/free-transfers")
    public ResponseEntity<List<FootballPlayerDto>> getFreeTransfers() {
        return ResponseEntity.ok(footballPlayerService.getFreeTransfers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        footballPlayerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

