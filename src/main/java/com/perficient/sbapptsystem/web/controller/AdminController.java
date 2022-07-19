package com.perficient.sbapptsystem.web.controller;

import com.perficient.sbapptsystem.web.client.AdminClient;
import com.perficient.sbapptsystem.web.model.ApptDto;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/create-user")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "create-user";
    }

    @GetMapping("/users/{userId}/create-appt")
    public String createAppt(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("appt", new ApptDto());
        return "create-appt";
    }


    @PostMapping
    public String createUser(@ModelAttribute @RequestBody UserDto user) {
        new AdminClient(new RestTemplateBuilder()).createUser(user);
        return "redirect:/users";
    }

    @PostMapping("/{userId}/appointments")
    public String createAppt(@ModelAttribute @RequestBody ApptDto apptDto, @PathVariable("userId") String userId) {
        new AdminClient(new RestTemplateBuilder()).createAppt(apptDto);
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/{userId}/edit")
    public String updateUser(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        return "edit-user";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @ModelAttribute @RequestBody UserDto user) {
        user.setAppointmentList(new AdminClient(new RestTemplateBuilder()).getUserById(userId).getAppointmentList());
        user.setId(userId);

        new AdminClient(new RestTemplateBuilder()).updateUser(userId, user);
        return "redirect:/users" ;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public String deleteUser(@PathVariable("userId") String userId) {
        new AdminClient(new RestTemplateBuilder()).deleteUser(userId);
        return "refresh:/users";
    }
}
