package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Map;

public interface PlayerService {
    Page<Player> getAllPlayers(Specification<Player> playerSpecification, Pageable pageable);
    Long getPlayersCount(Specification<Player> playerSpecification);
    Player createPlayer(Player player);
    Player getPlayerByID(Long id);
    Player updatePlayer(Player player, Long id);
    Map<String, Boolean> deletePlayer(Long id);

    Specification<Player> filterByName(String name);
    Specification<Player> filterByTitle(String title);
    Specification<Player> filterByRace(Race race);
    Specification<Player> filterByProfession(Profession profession);
    Specification<Player> filterByExperience(Integer min, Integer max);
    Specification<Player> filterByLevel(Integer min, Integer max);
    Specification<Player> filterByBirthday(Long after, Long before);
    Specification<Player> filterByBanned(Boolean isBanned);

    void validateId(Long id);
    void validateName(String name);
    void validateTitle(String title);
    void validateRace(Race race);
    void validateExperience(Integer experience);
    void validateProfession(Profession profession);
    void validateBirthday(Date birthday);
    Player checkForNullsAndSet(Player player, Long id);
}
