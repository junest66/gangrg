package com.project.gangrg.controller;

import com.project.gangrg.common.ApiResponse;
import com.project.gangrg.domain.User;
import com.project.gangrg.dto.UserJoinRequest;
import com.project.gangrg.dto.UserJoinResponse;
import com.project.gangrg.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final JoinService joinService;

    @PostMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserJoinResponse> join(@Valid @RequestBody final UserJoinRequest userJoinRequest) {
        User user = joinService.signup(userJoinRequest);
        return ApiResponse.successResponse(UserJoinResponse.of(user));
    }

}
