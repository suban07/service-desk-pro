package com.servicedesk.servicedesk_pro.controller;

import com.servicedesk.servicedesk_pro.dto.CreateUserRequest;
import com.servicedesk.servicedesk_pro.dto.UserResponse;
import com.servicedesk.servicedesk_pro.model.User;
import com.servicedesk.servicedesk_pro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
}
