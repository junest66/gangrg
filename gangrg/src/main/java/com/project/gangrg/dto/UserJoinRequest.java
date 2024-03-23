package com.project.gangrg.dto;

import static com.project.gangrg.domain.Role.ROLE_USER;

import com.project.gangrg.domain.Neighborhood;
import com.project.gangrg.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
    @NotNull(message = "동네 정보는 필수입니다.")
    private Long neighborhoodId;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
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
