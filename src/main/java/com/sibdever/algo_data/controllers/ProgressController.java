package com.sibdever.algo_data.controllers;

import com.sibdever.algo_data.QuestStatus;
import com.sibdever.algo_data.point.Point;
import com.sibdever.algo_data.tables.PointToQuestRepo;
import com.sibdever.algo_data.tables.UserToQuestId;
import com.sibdever.algo_data.tables.UserToQuestItem;
import com.sibdever.algo_data.tables.UserToQuestItemRepo;
import com.sibdever.algo_data.point.PointsRepo;
import com.sibdever.algo_data.quest.QuestsRepo;
import com.sibdever.algo_data.user.User;
import com.sibdever.algo_data.user.UsersRepo;
import com.sibdever.algo_data.workers.ProgressWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/progress")
public class ProgressController {

    private final UsersRepo usersRepo;
    private final QuestsRepo questsRepo;
    private final PointsRepo pointsRepo;
    private final UserToQuestItemRepo userToQuestItemRepo;
    private final PointToQuestRepo pointToQuestRepo;

    public ProgressController(UsersRepo usersRepo, QuestsRepo questsRepo, UserToQuestItemRepo userToQuestItemRepo, PointToQuestRepo pointToQuestRepo, PointsRepo pointsRepo, PointsRepo pointsRepo1) {
        this.usersRepo = usersRepo;
        this.questsRepo = questsRepo;
        this.userToQuestItemRepo = userToQuestItemRepo;
        this.pointToQuestRepo = pointToQuestRepo;
        this.pointsRepo = pointsRepo1;
    }

    // Get info about user progress in particular quest
    @PostMapping("/info")
    public @ResponseBody
    QuestStatus getQuestInfo(@RequestParam String ticket,
                             @RequestParam Long questId) {

        return ProgressWorker.getQuestStatus(ticket, questId, usersRepo, userToQuestItemRepo, pointsRepo);
    }

    // Use with geolocation auth when user has reached the point
    // Todo: Test it
    @PostMapping("/next")
    public @ResponseBody
    Point continueQuest(@RequestParam String ticket) {
        User user = usersRepo.findByTicket(ticket);

        if (user == null)
            return null;

        // Just return point with id equals lastPointId
        Point result = pointsRepo.findById(userToQuestItemRepo.findById(
                new UserToQuestId(user.getUserId(), user.getCurrentQuestId()))
                .get().getLastPointId()).get();

        return result;
    }

    // Start the quest
    @PostMapping("/start")
    public @ResponseBody
    QuestStatus startQuest(@RequestParam String ticket,
                           @RequestParam long questId) {

        // Todo remove on release
        System.err.println("Try to continue " + ticket);

        User user = usersRepo.findByTicket(ticket);

        if (user != null) {

            UserToQuestId id = new UserToQuestId(user.getUserId(), questId);

            Optional<UserToQuestItem> optional = userToQuestItemRepo.findById(id);

            // Start quest first time.
            if (!optional.isPresent()) {
                UserToQuestItem item = new UserToQuestItem();
                item.setUser(user);
                item.setQuest(questsRepo.findById(questId).get());
                item.setPassed(false);
                item.setInProgress(true);
                item.setLastPointId((questsRepo.getListOfPoints(questId).iterator().next()).getPointId());
                item.setId(id);
                userToQuestItemRepo.save(item);
                user.setCurrentQuestId(questId);
                usersRepo.save(user);
                return new QuestStatus(QuestStatus.StatusType.NEW, pointsRepo.findById(item.getLastPointId()).get());
            } else if (optional.get().isPassed()) {
                UserToQuestItem item = optional.get();
                // Start quest again.
                if (!item.isInProgress()) {
                    item.setLastPointId((questsRepo.getListOfPoints(questId).iterator().next()).getPointId());
                    item.setInProgress(true);
                    userToQuestItemRepo.save(item);
                    user.setCurrentQuestId(questId);
                    usersRepo.save(user);
                }
                return new QuestStatus(QuestStatus.StatusType.IN_PROGRESS_AGAIN, pointsRepo.findById(item.getLastPointId()).get());
            } else {
                // Continue quest first time.
                return new QuestStatus(QuestStatus.StatusType.IN_PROGRESS_NOT_FINISHED, pointsRepo.findById(optional.get().getLastPointId()).get());
            }

        }
        return new QuestStatus(QuestStatus.StatusType.ERROR, null);
    }

}