package com.sibdever.algo_data.controllers;

import com.sibdever.algo_data.point.Point;
import com.sibdever.algo_data.point.PointsRepo;
import com.sibdever.algo_data.quest.QuestsRepo;
import com.sibdever.algo_data.sevice.S3Service;
import com.sibdever.algo_data.tables.UserToQuestId;
import com.sibdever.algo_data.tables.UserToQuestItemRepo;
import com.sibdever.algo_data.user.User;
import com.sibdever.algo_data.user.UsersRepo;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/point")
public class PointController {

    private final PointsRepo pointsRepo;
    private final QuestsRepo questsRepo;
    private final UsersRepo usersRepo;
    private final UserToQuestItemRepo userToQuestItemRepo;
    private final S3Service s3Service;

    public PointController(PointsRepo pointsRepo, QuestsRepo questsRepo, UsersRepo usersRepo, UserToQuestItemRepo userToQuestItemRepo, S3Service s3Service) {
        this.pointsRepo = pointsRepo;
        this.questsRepo = questsRepo;
        this.usersRepo = usersRepo;
        this.userToQuestItemRepo = userToQuestItemRepo;
        this.s3Service = s3Service;
    }

    @PostMapping("/queue")
    public @ResponseBody
    List<Point.ShortPoint> getPointsQueue(@RequestParam long questId) {

        return questsRepo.getListOfPoints(questId).stream().map(Point::cut).collect(Collectors.toList());
    }

    // Use with QR or NFC codes auth when user have scanned it
    @PostMapping("/info")
    public @ResponseBody
    Point getPointInfoByCode(@RequestParam String code,
                             @RequestParam String ticket) {

        User user = usersRepo.findByTicket(ticket);
        if (user == null)
            return null;
        Point result = pointsRepo.findByCode(code);

        // Check this point is in lastPointId
        if (result.getPointId() == userToQuestItemRepo.findById(
                new UserToQuestId(user.getUserId(), user.getCurrentQuestId()))
                .get().getLastPointId()) {

            // MAIN RETURN!
            return result;

        } else {
            // Todo Write special result like "This point is in quest N"
            return null;
        }
    }

    // Get point description from .txt file
    @PostMapping("/description")
    public @ResponseBody
    ResponseEntity<ByteArrayResource> getPointDescription(@RequestParam Long id,
                                                 @RequestParam String language,
                                                 @RequestParam String ticket) {

        System.err.println("Try to get description");

        User user = usersRepo.findByTicket(ticket);

        if (user == null)
            return null;

        Optional<Point> opt = pointsRepo.findById(id);

        if (opt.isPresent() && opt.get().getPointId() == userToQuestItemRepo.findById(
                new UserToQuestId(user.getUserId(), user.getCurrentQuestId()))
                .get().getLastPointId()) {

            String path = "resources/desc/" + opt.get().getDescName() + "_" + language + ".txt";
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

    // Get picture in bytes-format
    // Todo: Test it.
    @PostMapping("/picture")
    public @ResponseBody
    ResponseEntity<ByteArrayResource> getPointDescription(@RequestParam String picName, @RequestParam String ticket) {

        System.err.println("Try to get picture");

        int count = pointsRepo.countByPicture(picName);

        User user = usersRepo.findByTicket(ticket);

        if (count > 0 && user != null) {

            String path = "resources/pic/" + picName;
            byte[] data = s3Service.downloadFile(path);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentLength(data.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);

        } else
            return ResponseEntity.noContent().build();
    }
}