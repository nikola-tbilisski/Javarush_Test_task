package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.DataNotFoundException;
import com.game.exceptions.InvalidRequestException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    private final static int NAME_MAX_LENGTH = 12;
    private final static int TITLE_MAX_LENGTH = 30;
    private final static int EXPERIENCE_MIN_VALUE = 0;
    private final static int EXPERIENCE_MAX_VALUE = 10000000;
    private final static long DATE_MIN_VALUE = 2000L;
    private final static long DATE_MAX_VALUE = 3000L;


    private final PlayerRepository playerRepository;


    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Integer currentLevelCalc(int exp) {
        return (((int) Math.sqrt(2500 + 200 * exp)) - 50) / 100;
    }

    public Integer experienceToGetNextLevelCalc(int level, int exp) {
        return 50 * (level + 1) * (level + 2) - exp;
    }

    @Override
    public Page<Player> getAllPlayers(Specification<Player> playerSpecification, Pageable pageable) {
        return playerRepository.findAll(playerSpecification, pageable);
    }

    @Override
    public Long getPlayersCount(Specification<Player> playerSpecification) {
        return playerRepository.count(playerSpecification);
    }

    @Override
    public Player createPlayer(Player player) {
        validateName(player.getName());
        validateTitle(player.getTitle());
        validateRace(player.getRace());
        validateProfession(player.getProfession());
        validateExperience(player.getExperience());
        validateBirthday(player.getBirthday());

        if (player.isBanned() == null) player.setBanned(true);

        player.setLevel(currentLevelCalc(player.getExperience()));
        player.setUntilNextLevel(experienceToGetNextLevelCalc(player.getLevel(), player.getExperience()));

        return playerRepository.saveAndFlush(player);
    }

    @Override
    public Player getPlayerByID(Long id) {
        validateId(id);
        return playerRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("No data for player with ID:" + id));
    }

    @Override
    public Player updatePlayer(Player player, Long id) {
        Player updatePlayer = checkForNullsAndSet(player, id);

        updatePlayer.setLevel(currentLevelCalc(updatePlayer.getExperience()));
        updatePlayer.setUntilNextLevel(experienceToGetNextLevelCalc(updatePlayer.getLevel(), updatePlayer.getExperience()));

        return playerRepository.save(updatePlayer);
    }

    @Override
    public Map<String, Boolean> deletePlayer(Long id) {
        playerRepository.delete(getPlayerByID(id));

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);

        return response;
    }

    @Override
    public Player checkForNullsAndSet(Player player, Long id) {
        Player myPlayer = getPlayerByID(id);

        if (player.getName() != null) {
            validateName(player.getName());
            myPlayer.setName(player.getName());
        }

        if (player.getTitle() != null) {
            validateTitle(player.getTitle());
            myPlayer.setTitle(player.getTitle());
        }

        if (player.getRace() != null) {
            validateRace(player.getRace());
            myPlayer.setRace(player.getRace());
        }

        if (player.getProfession() != null) {
            validateProfession(player.getProfession());
            myPlayer.setProfession(player.getProfession());
        }

        if (player.getBirthday() != null) {
            validateBirthday(player.getBirthday());
            myPlayer.setBirthday(player.getBirthday());
        }

        if (player.isBanned() != null) {
            myPlayer.setBanned(player.isBanned());
        }

        if (player.getExperience() != null) {
            validateExperience(player.getExperience());
            myPlayer.setExperience(player.getExperience());
        }

        return myPlayer;
    }

    @Override
    public Specification<Player> filterByName(String name) {
        return (root, query, cb) -> name == null
                ? null
                : cb.like(root.get("name"), "%" + name + "%");
    }

    @Override
    public Specification<Player> filterByTitle(String title) {
        return (root, query, cb) -> title == null
                ? null
                : cb.like(root.get("title"), "%" + title + "%");
    }

    @Override
    public Specification<Player> filterByRace(Race race) {
        return (root, query, cb) -> race == null
                ? null
                : cb.equal(root.get("race"), race);
    }

    @Override
    public Specification<Player> filterByProfession(Profession profession) {
        return (root, query, cb) -> profession == null
                ? null
                : cb.equal(root.get("profession"), profession);
    }

    @Override
    public Specification<Player> filterByExperience(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(root.get("experience"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("experience"), min);
            return cb.between(root.get("experience"), min, max);
        };
    }

    @Override
    public Specification<Player> filterByLevel(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(root.get("level"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("level"), min);
            return cb.between(root.get("level"), min, max);
        };
    }

    @Override
    public Specification<Player> filterByBirthday(Long after, Long before) {
        return (root, query, cb) -> {
            if (after == null && before == null) return null;
            if (after == null) return cb.lessThanOrEqualTo(root.get("birthday"), new Date(before));
            if (before == null) return cb.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
            return cb.between(root.get("birthday"), new Date(after), new Date(before));
        };
    }

    @Override
    public Specification<Player> filterByBanned(Boolean isBanned) {
        return (root, query, cb) -> {
            if (isBanned == null) return null;
            if (isBanned) return cb.isTrue(root.get("banned"));
            return cb.isFalse(root.get("banned"));
        };
    }

    @Override
    public void validateId(Long id) {
        if (!id.toString().matches("^[0-9]+$") || id <= 0)
            throw new InvalidRequestException("Invalid ID");
    }

    @Override
    public void validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > NAME_MAX_LENGTH)
            throw new InvalidRequestException("Invalid name");
    }

    @Override
    public void validateTitle(String title) {
        if (title == null || title.isEmpty() || title.length() > TITLE_MAX_LENGTH)
            throw new InvalidRequestException("Invalid title");
    }

    @Override
    public void validateRace(Race race) {
        if (race == null || !race.toString().matches("^[A-Z]+$"))
            throw new InvalidRequestException("Invalid race");
    }

    @Override
    public void validateProfession(Profession profession) {
        if (profession == null || !profession.toString().matches("^[A-Z]+$"))
            throw new InvalidRequestException("Invalid profession");
    }

    @Override
    public void validateBirthday(Date birthday) {
        if (birthday == null || birthday.getTime() < 0)
            throw new InvalidRequestException("Invalid birthday");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthday.getTime());

        if (calendar.get(Calendar.YEAR) < DATE_MIN_VALUE || calendar.get(Calendar.YEAR) > DATE_MAX_VALUE)
            throw new InvalidRequestException("Birthday is out of bounds");
    }

    @Override
    public void validateExperience(Integer experience) {
        if (!experience.toString().matches("^[0-9]+$")
                || experience < EXPERIENCE_MIN_VALUE
                || experience > EXPERIENCE_MAX_VALUE)
            throw new InvalidRequestException("Invalid Experience");
    }
}
