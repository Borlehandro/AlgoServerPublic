package com.sibdever.algo_data.user;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long userId;

    @Column(name = "user_name", unique = true, nullable = false)
    private String name;

    private String password;

    @Column(unique = true, nullable = false)
    private String ticket;

    private int bonuses = 0;

    @Column(nullable = false)
    private long currentQuestId;

    @Column(name = "quests_completed", nullable = false)
    private int questsPassed = 0;

    @Column(name = "points_completed", nullable = false)
    private int pointsPassed = 0;

    @Column(name = "kilometers_completed", nullable = false)
    private float kilometersCompleted = 0f;

    @Basic
    @Column(name = "registration_time")
    private java.sql.Timestamp registrationTime;

    public static class UserInfo {
        private String name;
        private int bonuses;
        private int questsPassed;
        private int pointsPassed;
        private double kilometersCompleted;

        public UserInfo(String name, int bonuses, int questsPassed, int pointsPassed, double kilometersCompleted) {
            this.name = name;
            this.bonuses = bonuses;
            this.questsPassed = questsPassed;
            this.pointsPassed = pointsPassed;
            this.kilometersCompleted = kilometersCompleted;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBonuses() {
            return bonuses;
        }

        public void setBonuses(int bonuses) {
            this.bonuses = bonuses;
        }

        public int getQuestsPassed() {
            return questsPassed;
        }

        public void setQuestsPassed(int questsPassed) {
            this.questsPassed = questsPassed;
        }

        public int getPointsPassed() {
            return pointsPassed;
        }

        public void setPointsPassed(int pointsPassed) {
            this.pointsPassed = pointsPassed;
        }

        public double getKilometersCompleted() {
            return kilometersCompleted;
        }

        public void setKilometersCompleted(int kilometersCompleted) {
            this.kilometersCompleted = kilometersCompleted;
        }
    }

    public UserInfo getInfo() {
        return new UserInfo(name, bonuses, questsPassed, pointsPassed, kilometersCompleted);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getTicket() {
        return ticket;
    }

    public int getBonuses() {
        return bonuses;
    }

    public int getQuestsPassed() {
        return questsPassed;
    }

    public int getPointsPassed() {
        return pointsPassed;
    }

    public float getKilometersCompleted() {
        return kilometersCompleted;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void generateTicket() {
        try {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
            this.ticket = Base64.getUrlEncoder().encodeToString(salt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void setRegistrationTime(java.sql.Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void addBonuses(int newBonuses) {
        this.bonuses += newBonuses;
    }

    public void addPassedQuests(int newQuests) {
        this.questsPassed += newQuests;
    }

    public void addPassedPoints(int newPoints) {
        this.pointsPassed += newPoints;
    }

    public void addCompletedKilometers(float newKilometers) {
        this.kilometersCompleted += newKilometers;
    }

    public java.sql.Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public long getCurrentQuestId() {
        return currentQuestId;
    }

    public void setCurrentQuestId(long currentQuestId) {
        this.currentQuestId = currentQuestId;
    }
}