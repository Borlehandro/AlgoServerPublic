package com.sibdever.algo_data.quest;

import com.sibdever.algo_data.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedHashSet;

public interface QuestsRepo extends JpaRepository<Quest, Long> {

    @Query("select item.point from PointToQuestItem item where item.quest.questId=?1 order by item.id.pointNumber")
    LinkedHashSet<Point> getListOfPoints(long questId);

}
