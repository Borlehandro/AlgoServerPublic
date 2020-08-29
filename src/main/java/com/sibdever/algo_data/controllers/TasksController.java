package com.sibdever.algo_data.controllers;

import com.sibdever.algo_data.QuestStatus;
import com.sibdever.algo_data.tables.PointToQuestRepo;
import com.sibdever.algo_data.tables.UserToQuestId;
import com.sibdever.algo_data.tables.UserToQuestItem;
import com.sibdever.algo_data.tables.UserToQuestItemRepo;
import com.sibdever.algo_data.point.PointsRepo;
import com.sibdever.algo_data.quest.QuestsRepo;
import com.sibdever.algo_data.task.Task;
import com.sibdever.algo_data.task.TasksRepo;
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
@RequestMapping("/task")
public class TasksController {

    private final TasksRepo repo;
    private final UsersRepo usersRepo;
    private final QuestsRepo questsRepo;
    private final PointsRepo pointsRepo;
    private final UserToQuestItemRepo questToUserRepo;
    private final PointToQuestRepo pointToQuestRepo;

    public TasksController(TasksRepo repo, UsersRepo usersRepo, QuestsRepo questsRepo, PointsRepo pointsRepo, UserToQuestItemRepo questToUserRepo, PointToQuestRepo pointToQuestRepo) {
        this.repo = repo;
        this.usersRepo = usersRepo;
        this.questsRepo = questsRepo;
        this.pointsRepo = pointsRepo;
        this.questToUserRepo = questToUserRepo;
        this.pointToQuestRepo = pointToQuestRepo;
    }

    @PostMapping("/info")
    public @ResponseBody
    Optional<Task> getTaskInfo(@RequestParam Long taskId,
                               @RequestParam String ticket) {

        // Todo check task is task for current point
        User user = usersRepo.findByTicket(ticket);
        if(user!=null) {
            Optional<UserToQuestItem> opt = questToUserRepo.findById(new UserToQuestId(user.getUserId(), user.getCurrentQuestId()));
            if(!opt.isPresent()) {
                System.err.println("Incorrect user-quest");
                return Optional.empty();
            }
            if(pointsRepo.findById(opt.get().getLastPointId()).get().getTaskId() == taskId) {
                return repo.findById(taskId);
            }
            else return Optional.empty();
        } else
            return Optional.empty();
    }

    // Check and continue progress if success
    @PostMapping("/check")
    public @ResponseBody
    QuestStatus checkAnswer(@RequestParam int variant,
                            @RequestParam long taskId,
                            @RequestParam String ticket) {

        Optional<Task> res = repo.findById(taskId);

        User user = usersRepo.findByTicket(ticket);

        long questId = user.getCurrentQuestId();

        if(questId == -1)
            return new QuestStatus(QuestStatus.StatusType.ERROR, null);

        UserToQuestId id = new UserToQuestId(user.getUserId(), questId);

        UserToQuestItem item = questToUserRepo.findById(id).get();

        long lastPointId = item.getLastPointId();

        if((res.isPresent()
                && pointsRepo.findById(lastPointId).get().infoTask().getTaskId() == taskId
                && res.get().isRight(variant))) {

            // Update progress and return new ShortPoint
            return ProgressWorker.continueQuest(ticket, questId,
                    usersRepo, questsRepo, questToUserRepo, pointToQuestRepo, pointsRepo);
        } else // Return NOT_CHANGED + current point
            return new QuestStatus(QuestStatus.StatusType.NOT_CHANGED, pointsRepo.findById(item.getLastPointId()).get());
    }
}