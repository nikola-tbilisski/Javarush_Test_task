package com.game.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String name;
    private String title;

    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;

    private Date birthday;
    private Boolean banned;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;


    public Player() {
    }

    public Player(Long id, String name, String title, Race race, Profession profession, int experience, int level,
                  int untilNextLevel, Date birthday, Boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(int untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return id.equals(player.id) &&
                Objects.equals(name, player.name) &&
                Objects.equals(title, player.title) &&
                race == player.race &&
                profession == player.profession &&
                Objects.equals(experience, player.experience) &&
                Objects.equals(level, player.level) &&
                Objects.equals(untilNextLevel, player.untilNextLevel) &&
                Objects.equals(birthday, player.birthday) &&
                Objects.equals(banned, player.banned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }
}
