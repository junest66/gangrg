package com.project.gangrg.dto;

import static com.project.gangrg.domain.Role.ROLE_USER;

import com.project.gangrg.domain.Neighborhood;
import com.project.gangrg.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
    private Long neighborhoodId;
    private String email;
    private String nickname;
    private String name;
    private String phone;
    private String password;

    public User toEntity(Neighborhood neighborhood, String encodePassword) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .name(name)
                .phone(phone)
                .password(encodePassword)
                .neighborhood(neighborhood)
                .role(ROLE_USER)
                .build();
    }
}
