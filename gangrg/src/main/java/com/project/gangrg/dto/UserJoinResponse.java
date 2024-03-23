package com.project.gangrg.dto;

import com.project.gangrg.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserJoinResponse {
    private Long id;

    public static UserJoinResponse of(User user){
        UserJoinResponse userDto = UserJoinResponse.builder()
                .id(user.getId())
                .build();
        return userDto;
    }
}
