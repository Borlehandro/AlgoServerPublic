package com.sibdever.algo_data.tables;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserToQuestId implements Serializable {

    private long userId;
    private long questId;

    public UserToQuestId() {}

    public UserToQuestId(long userId, long questId) {
        this.questId = questId;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getQuestId() {
        return questId;
    }

    public void setQuestId(long questId) {
        this.questId = questId;
    }
}
