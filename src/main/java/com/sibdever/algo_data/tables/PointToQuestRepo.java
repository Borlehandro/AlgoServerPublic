package com.sibdever.algo_data.tables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointToQuestRepo extends JpaRepository<PointToQuestItem, PointToQuestId> {

    @Query("select item.id.pointNumber from PointToQuestItem item " +
            "where item.quest.questId = ?1 and item.point.pointId = ?2")
    int getNumberOfPointInQueue(long questId, long pointId);

    @Query("select item.point.pointId from PointToQuestItem item " +
            "where item.quest.questId=?1 and item.id.pointNumber=?2")
    long getIdByPointsInQueue(long questId, int pointNumber);

}
