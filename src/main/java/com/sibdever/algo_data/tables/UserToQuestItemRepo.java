package com.sibdever.algo_data.tables;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserToQuestItemRepo extends JpaRepository<UserToQuestItem, UserToQuestId> {

//    @Query("select item from UserToQuestItem item " +
//            "where item.user.userId=?1 and item.quest.questId=?2")
//    UserToQuestItem findByUserQuestId(long userId, long questId);

}
