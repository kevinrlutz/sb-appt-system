package com.perficient.sbapptsystem.web.controller;

import com.perficient.sbapptsystem.web.client.AdminClient;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class AdminController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("userList", new AdminClient(new RestTemplateBuilder()).getAllUsers());
        return "list-users";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        model.addAttribute("apptList", new AdminClient(new RestTemplateBuilder()).getAllAppts());
        return "list-appts";
    }

    @GetMapping("/users/{userId}")
    public String user(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        return "user";
    }
}
