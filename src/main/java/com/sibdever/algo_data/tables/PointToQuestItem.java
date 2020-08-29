package com.sibdever.algo_data.tables;

import com.sibdever.algo_data.point.Point;
import com.sibdever.algo_data.quest.Quest;

import javax.persistence.*;

@Entity
@Table(name = "quest_points")
public class PointToQuestItem {

    @EmbeddedId
    private PointToQuestId id;

    @MapsId("questId")
    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

//    @MapsId(value = "pointNumber")
//    private int pointNumber;

//    public Quest getQuest() {
//        return quest;
//    }

    public Point getPoint() {
        return point;
    }

    public int getPointNumber() {
        return id.getPointNumber();
    }

    public PointToQuestId getId() {
        return id;
    }

    public void setId(PointToQuestId id) {
        this.id = id;
    }
}
