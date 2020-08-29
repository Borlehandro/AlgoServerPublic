package com.sibdever.algo_data.quest;

import com.sibdever.algo_data.tables.PointToQuestItem;
import com.sibdever.algo_data.tables.UserToQuestItem;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "quests_list")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long questId;

    @Column(nullable = false)
    private String bigDescName;

    @Column(nullable = false)
    private String picName;

    @Column(nullable = false)
    private String enName;

    @Column(nullable = false)
    private String ruName;

    @Column(nullable = false)
    private String zhName;

    @Column(nullable = false)
    private String shortDescRu;

    @Column(nullable = false)
    private String shortDescEn;

    @Column(nullable = false)
    private String shortDescZh;

    @Column(nullable = false)
    private int pointsCount;

    @Column(nullable = false)
    private float length;

    @Column(nullable = false)
    private int bonuses;

    @Column(name = "finish_message_ru")
    private String finishMessageTextRu;

    @Column(name = "finish_message_en")
    private String finishMessageTextEn;

    @Column(name = "finish_message_zh")
    private String finishMessageTextZh;

    @OneToMany(mappedBy = "quest")
    private Set<PointToQuestItem> items;

    @OneToMany(mappedBy = "quest")
    private Set<UserToQuestItem> userToQuestItems;

    public static class FinishMessage {
        private final String finishMessage;
        private final int bonuses;

        public FinishMessage(String finishMessage, int bonuses) {
            this.finishMessage = finishMessage;
            this.bonuses = bonuses;
        }

        public String getFinishMessage() {
            return finishMessage;
        }

        public int getBonuses() {
            return bonuses;
        }
    }

    public FinishMessage getFinishMessage(String language) {
        String text = null;
        switch (language) {
            case "ru":
                text = finishMessageTextRu;
                break;
            case "en":
                text = finishMessageTextEn;
                break;
            case "zh":
                text = finishMessageTextZh;
                break;
        }
        return new FinishMessage(text, bonuses);
    }

    public long getQuestId() {
        return questId;
    }

    public String getBigDescName() {
        return bigDescName;
    }

    public String getPicName() {
        return picName;
    }

    public String getEnName() {
        return enName;
    }

    public String getRuName() {
        return ruName;
    }

    public String getZhName() {
        return zhName;
    }

    public String getShortDescRu() {
        return shortDescRu;
    }

    public String getShortDescEn() {
        return shortDescEn;
    }

    public String getShortDescZh() {
        return shortDescZh;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public float getLength() {
        return length;
    }

    public int getBonuses() {
        return bonuses;
    }

    public Set<PointToQuestItem> infoPoints() {
        return items;
    }

    public Set<UserToQuestItem> infoUsers() {
        return userToQuestItems;
    }

    public String getFinishMessageTextRu() {
        return finishMessageTextRu;
    }

    public void setFinishMessageTextRu(String finishMessageTextRu) {
        this.finishMessageTextRu = finishMessageTextRu;
    }

    public String getFinishMessageTextEn() {
        return finishMessageTextEn;
    }

    public void setFinishMessageTextEn(String finishMessageTextEn) {
        this.finishMessageTextEn = finishMessageTextEn;
    }

    public String getFinishMessageTextZh() {
        return finishMessageTextZh;
    }

    public void setFinishMessageTextZh(String finishMessageTextZh) {
        this.finishMessageTextZh = finishMessageTextZh;
    }
}