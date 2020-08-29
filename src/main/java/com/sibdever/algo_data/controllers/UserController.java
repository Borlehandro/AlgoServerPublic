package com.sibdever.algo_data.controllers;

import com.sibdever.algo_data.user.User;
import com.sibdever.algo_data.user.UsersRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    // Todo Use UserResponse
    private final UsersRepo userRepository;

    public UserController(UsersRepo userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/register")
    public @ResponseBody
    String addNewUser(@RequestParam String name,
                      @RequestParam String password) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            User newUser = new User();
            newUser.setName(name);
            newUser.setPassword(encoder.encode(password));
            newUser.generateTicket();
            newUser.setRegistrationTime(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(newUser);
            System.err.println("Register " + name + " " + LocalDateTime.now());
            return newUser.getTicket();
        } catch (DataIntegrityViolationException e) {
            return "Already exist";
        }
    }

    @PostMapping(path = "/login")
    public @ResponseBody
    String loginUser(@RequestParam String name,
                     @RequestParam String password) {

        User current = userRepository.findByUserName(name);

        if (current == null)
            return "Incorrect name";

        System.err.println("Start encoding");

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, current.getPassword())) {
            current.generateTicket();
            userRepository.save(current);
            return current.getTicket();
        } else
            return "Incorrect password";
    }

    @RequestMapping(path = "/info")
    public @ResponseBody
    User.UserInfo getUserInfo(@RequestParam String ticket) {
        User user = userRepository.findByTicket(ticket);
        if (user != null) {
            return user.getInfo();
        } else {
            return null; // Todo Add error message.
        }
    }
}