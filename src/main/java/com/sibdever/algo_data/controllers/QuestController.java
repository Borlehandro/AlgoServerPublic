package com.sibdever.algo_data.controllers;

import com.sibdever.algo_data.QuestListItem;
import com.sibdever.algo_data.QuestStatus;
import com.sibdever.algo_data.point.PointsRepo;
import com.sibdever.algo_data.quest.Quest;
import com.sibdever.algo_data.quest.QuestsRepo;
import com.sibdever.algo_data.sevice.S3Service;
import com.sibdever.algo_data.tables.UserToQuestId;
import com.sibdever.algo_data.tables.UserToQuestItemRepo;
import com.sibdever.algo_data.user.User;
import com.sibdever.algo_data.user.UsersRepo;
import com.sibdever.algo_data.workers.ProgressWorker;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quest")
public class QuestController {

    private final QuestsRepo questsRepo;
    private final UsersRepo usersRepo;
    private final PointsRepo pointsRepo;
    private final UserToQuestItemRepo userToQuestItemRepo;
    private final S3Service s3Service;

    public QuestController(QuestsRepo questsRepo, UsersRepo usersRepo, PointsRepo pointsRepo, UserToQuestItemRepo userToQuestItemRepo, S3Service s3Service){
        this.questsRepo = questsRepo;
        this.usersRepo = usersRepo;
        this.pointsRepo = pointsRepo;
        this.userToQuestItemRepo = userToQuestItemRepo;
        this.s3Service = s3Service;
    }

    @PostMapping("/list")
    public @ResponseBody
    List<QuestListItem> getQuestsList(@RequestParam String ticket) {

        System.err.println("Try to get list " + ticket);

        List<Quest> questList = questsRepo.findAll();

        List<QuestListItem> result = new ArrayList<>();

        for (Quest item : questList) {

            QuestStatus status = ProgressWorker.getQuestStatus(ticket, item.getQuestId(), usersRepo, userToQuestItemRepo, pointsRepo);

            if(status.getStatus()== QuestStatus.StatusType.ERROR)
                return null;

            result.add(new QuestListItem(item, status));
        }

        return result;
    }

    @PostMapping("/description")
    public @ResponseBody
    ResponseEntity<ByteArrayResource> getQuestDescription(@RequestParam Long id,
                                                          @RequestParam String language) {
        System.err.println("Try to get description");
        Optional<Quest> opt = questsRepo.findById(id);

        if(opt.isPresent()) {

            String path = "resources/desc/" + opt.get().getBigDescName() + "_" + language + ".txt";
            byte[] data = s3Service.downloadFile(path);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } else
            return null;
    }

    @PostMapping("/picture")
    public @ResponseBody
    ResponseEntity<ByteArrayResource> getQuestPicture(@RequestParam Long id) {

        System.err.println("Try to get picture");
        Optional<Quest> opt = questsRepo.findById(id);

        if(opt.isPresent()) {

            String path = "resources/pic/" + opt.get().getPicName();
            byte[] data = s3Service.downloadFile(path);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentLength(data.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } else
            return null;
    }

    @PostMapping("/finish")
    public @ResponseBody
    Quest.FinishMessage getFinishMessage(@RequestParam String ticket, @RequestParam long questId, @RequestParam String language) {
        Optional<Quest> opt = questsRepo.findById(questId);
        if(opt.isPresent()) {
            Quest quest = opt.get();
            User user = usersRepo.findByTicket(ticket);
            if(user != null && userToQuestItemRepo.findById(new UserToQuestId(user.getUserId(), quest.getQuestId())).get().isPassed()) {
                return quest.getFinishMessage(language);
            } else return null;
        } else return null;
    }
}