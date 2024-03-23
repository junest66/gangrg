package com.project.gangrg.controller;

import com.project.gangrg.domain.User;
import com.project.gangrg.dto.UserJoinRequest;
import com.project.gangrg.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final JoinService joinService;

    @PostMapping("/api/v1/users")
    public Long join(@RequestBody UserJoinRequest userJoinRequest) {
        User user = joinService.signup(userJoinRequest);
        return user.getId();
    }

}
