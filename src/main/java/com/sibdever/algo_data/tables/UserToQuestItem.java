package com.sibdever.algo_data.tables;

import com.sibdever.algo_data.quest.Quest;
import com.sibdever.algo_data.user.User;

import javax.persistence.*;

@Entity
@Table(name = "user_quest")
public class UserToQuestItem {

    @EmbeddedId
    private UserToQuestId id;

    @MapsId("userId")
    @JoinColumn(name="user_id")
    @ManyToOne
    private User user;

    @MapsId("questId")
    @JoinColumn(name = "quest_id")
    @ManyToOne
    private Quest quest;

    private long lastPointId = 0;

    private boolean passed = false;

    private boolean inProgress = false;

    public User getUser() {
        return user;
    }

    public Quest getQuest() {
        return quest;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public UserToQuestId getId() {
        return id;
    }

    public void setId(UserToQuestId id) {
        this.id = id;
    }

    public long getLastPointId() {
        return lastPointId;
    }

    public void setLastPointId(long lastPointId) {
        this.lastPointId = lastPointId;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
