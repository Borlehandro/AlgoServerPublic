package com.sibdever.algo_data.workers;

import com.sibdever.algo_data.QuestStatus;
import com.sibdever.algo_data.point.Point;
import com.sibdever.algo_data.point.PointsRepo;
import com.sibdever.algo_data.tables.PointToQuestRepo;
import com.sibdever.algo_data.tables.UserToQuestId;
import com.sibdever.algo_data.tables.UserToQuestItem;
import com.sibdever.algo_data.tables.UserToQuestItemRepo;
import com.sibdever.algo_data.quest.Quest;
import com.sibdever.algo_data.quest.QuestsRepo;
import com.sibdever.algo_data.user.User;
import com.sibdever.algo_data.user.UsersRepo;

import java.util.Optional;

// I feel awful when I read it
public class ProgressWorker {

    public static QuestStatus getQuestStatus(String ticket,
                                             Long questId,
                                             UsersRepo usersRepo,
                                             UserToQuestItemRepo questToUserRepo,
                                             PointsRepo pointsRepo) {

        User user = usersRepo.findByTicket(ticket);

        if (user != null) {
            Optional<UserToQuestItem> opt = questToUserRepo.findById(new UserToQuestId(user.getUserId(), questId));
            // Nothing in the table
            if (!opt.isPresent())
                return new QuestStatus(QuestStatus.StatusType.NOT_STARTED, new Point());
                // Table row exist
            else {
                UserToQuestItem item = opt.get();
                if (item.isPassed()) {
                    return new QuestStatus(item.isInProgress() ? QuestStatus.StatusType.IN_PROGRESS_AGAIN : QuestStatus.StatusType.FINISHED,
                            pointsRepo.findById(item.getLastPointId()).get());
                } else {
                    return new QuestStatus(QuestStatus.StatusType.IN_PROGRESS_NOT_FINISHED, pointsRepo.findById(item.getLastPointId()).get());
                }
            }
        } else
            return new QuestStatus(QuestStatus.StatusType.ERROR, new Point());
    }

    public static QuestStatus continueQuest(String ticket,
                                            Long questId,
                                            UsersRepo usersRepo,
                                            QuestsRepo questsRepo,
                                            UserToQuestItemRepo questToUserRepo,
                                            PointToQuestRepo pointToQuestRepo,
                                            PointsRepo pointsRepo) {

        // Todo remove on release
        System.err.println("Try to continue " + ticket);

        User user = usersRepo.findByTicket(ticket);

        if (user != null) {

            // Id for searching
            UserToQuestId id = new UserToQuestId();
            id.setUserId(user.getUserId());
            id.setQuestId(questId);

            UserToQuestItem item = questToUserRepo.findById(id).get();

            long lastPointId = item.getLastPointId();

            System.err.println("Old id: " + lastPointId);

            int lastPointNumber = pointToQuestRepo.getNumberOfPointInQueue(questId, lastPointId) + 1;

            Quest quest = questsRepo.findById(questId).get();

            if (lastPointNumber == quest.getPointsCount()) {
                // Finishing
                return setQuestFinished(user, quest, questToUserRepo, usersRepo, pointsRepo);
            } else {
                // Update last point
                long newId = pointToQuestRepo.getIdByPointsInQueue(questId, lastPointNumber);
                System.err.println("New id: " + newId);
                item.setLastPointId(newId);
                questToUserRepo.save(item);
                return new QuestStatus(item.isPassed() ? QuestStatus.StatusType.IN_PROGRESS_AGAIN : QuestStatus.StatusType.IN_PROGRESS_NOT_FINISHED,
                        pointsRepo.findById(item.getLastPointId()).get());
            }
        } else
            return new QuestStatus(QuestStatus.StatusType.ERROR, null);
    }

    private static QuestStatus setQuestFinished(User user, Quest quest,
                                                UserToQuestItemRepo questToUserRepo,
                                                UsersRepo usersRepo,
                                                PointsRepo pointsRepo) {

        UserToQuestId id = new UserToQuestId();
        id.setUserId(user.getUserId());
        id.setQuestId(quest.getQuestId());
        Optional<UserToQuestItem> optional = questToUserRepo.findById(id);

        if (!optional.isPresent()) {
            return new QuestStatus(QuestStatus.StatusType.ERROR, null);
        }

        UserToQuestItem item = optional.get();

        user.setCurrentQuestId(-1);
        usersRepo.save(user);

        if (!item.isPassed()) {

            item.setPassed(true);
            item.setInProgress(false);
            questToUserRepo.save(item);

            user.addBonuses(quest.getBonuses());
            user.addPassedPoints(quest.getPointsCount());
            user.addCompletedKilometers(quest.getLength());
            user.addPassedQuests(1);
            usersRepo.save(user);
            return new QuestStatus(QuestStatus.StatusType.FINISHED_FIRST_TIME, pointsRepo.findById(item.getLastPointId()).get());
        } else
            return new QuestStatus(QuestStatus.StatusType.FINISHED_AGAIN, pointsRepo.findById(item.getLastPointId()).get());
    }
}