package com.sibdever.algo_data.tables;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PointToQuestId implements Serializable {

    private long questId;
    private int pointNumber;

    public long getQuestId() {
        return questId;
    }

    public void setQuestId(long questId) {
        this.questId = questId;
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
    }
}
