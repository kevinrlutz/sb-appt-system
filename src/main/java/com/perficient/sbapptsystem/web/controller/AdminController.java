package com.perficient.sbapptsystem.web.controller;

import com.perficient.sbapptsystem.web.client.AdminClient;
import com.perficient.sbapptsystem.web.model.ApptDto;
import com.perficient.sbapptsystem.web.model.ApptFormatter;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @GetMapping("/")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/home")
    public String index() {
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("userList", new AdminClient(new RestTemplateBuilder()).getAllUsers());
        model.addAttribute("searchDto", new UserDto());
        return "list-users";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        model.addAttribute("apptList", new AdminClient(new RestTemplateBuilder()).getAllAppts());
        model.addAttribute("searchAppt", new ApptDto());
        return "list-appts";
    }

    @GetMapping("/users/{userId}")
    public String user(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        model.addAttribute("apptList", new AdminClient(new RestTemplateBuilder()).getUserAppointments(userId));
        return "user";
    }

    @GetMapping("/users/{userId}/appointments/{apptId}")
    public String appointment(Model model, @PathVariable("userId") String userId, @PathVariable("apptId") String apptId) {
        model.addAttribute("appt", new AdminClient(new RestTemplateBuilder()).getApptById(apptId));
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        return "appt";
    }

    @GetMapping("/create-user")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "create-user";
    }

    @GetMapping("/users/{userId}/create-appt")
    public String createAppt(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("appt", new ApptDto());
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        return "create-appt";
    }


    @RequestMapping(value = "/users/search/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String searchUser(Model model, UserDto searchDto) {
        model.addAttribute("searchDto", new UserDto());
        model.addAttribute("userList", new AdminClient(new RestTemplateBuilder()).findByLastName(searchDto.getLastName()));
        return "list-users";
    }

    @RequestMapping(value="/appointments/search/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String searchAppt(Model model, ApptDto searchDto) {
        System.out.println("In searchAppt controller method");
        model.addAttribute("searchAppt", new ApptDto());
        model.addAttribute("apptList", new AdminClient(new RestTemplateBuilder()).findByApptName(searchDto.getApptName()));
        return "list-appts";
    }


    @PostMapping("/create")
    public String createUser(@ModelAttribute @RequestBody UserDto user) {
        new AdminClient(new RestTemplateBuilder()).createUser(user);
        return "redirect:/users";
    }

    @PostMapping("/{userId}/appointments")
    public String createAppt(@ModelAttribute @RequestBody ApptFormatter apptFormatter, @PathVariable("userId") String userId) {
        UserDto user = new AdminClient(new RestTemplateBuilder()).getUserById(userId);
        new AdminClient(new RestTemplateBuilder()).createAppt(apptFormatter, user);
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/{userId}/edit-user")
    public String updateUser(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        return "edit-user";
    }

    @GetMapping("users/{userId}/appointments/{apptId}/edit-appt")
    public String updateAppt(Model model, @PathVariable("userId") String userId, @PathVariable("apptId") String apptId) {
        model.addAttribute("user", new AdminClient(new RestTemplateBuilder()).getUserById(userId));
        model.addAttribute("appt", new AdminClient(new RestTemplateBuilder()).getApptById(apptId));
        return "edit-appt";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @ModelAttribute @RequestBody UserDto user) {
        user.setAppointmentList(new AdminClient(new RestTemplateBuilder()).getUserById(userId).getAppointmentList());
        user.setId(userId);

        new AdminClient(new RestTemplateBuilder()).updateUser(userId, user);
        return "redirect:/users" ;
    }

    @PutMapping("/users/{userId}/appointments/{apptId}")
    public String updateAppt(@ModelAttribute @RequestBody ApptFormatter apptFormatter, @PathVariable("userId") String userId, @PathVariable("apptId") String apptId) {
        UserDto user = new AdminClient(new RestTemplateBuilder()).getUserById(userId);
        apptFormatter.setId(apptId);
        new AdminClient(new RestTemplateBuilder()).updateAppt(apptId, apptFormatter, user);

        return "redirect:/users/" + userId + "/appointments/" + apptId;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public String deleteUser(@PathVariable("userId") String userId) {
        new AdminClient(new RestTemplateBuilder()).deleteUser(userId);
        return "refresh:/users";
    }

    @DeleteMapping("{userId}/appointments/{apptId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public String deleteAppt(@PathVariable("userId") String userId, @PathVariable("apptId") String apptId) {
        new AdminClient(new RestTemplateBuilder()).deleteAppt(userId, apptId);
        return "refresh:/users/" + userId;
    }

}
