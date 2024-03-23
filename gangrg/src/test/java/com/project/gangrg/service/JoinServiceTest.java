package com.project.gangrg.service;

import static org.assertj.core.api.Assertions.*;

import com.project.gangrg.domain.Neighborhood;
import com.project.gangrg.domain.User;
import com.project.gangrg.dto.UserJoinRequest;
import com.project.gangrg.repository.NeighborhoodRepository;
import com.project.gangrg.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class JoinServiceTest {

    @Autowired
    JoinService joinService;

    @Autowired
    NeighborhoodRepository neighborhoodRepository;

    @Autowired
    UserRepository userRepository;

    private Neighborhood neighborhood;

    @BeforeEach
    void setup() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(127.0, 37.0));
        neighborhood = new Neighborhood("서울특별시", "강남구", "청담동", point);
        neighborhoodRepository.save(neighborhood);
    }

    @Test
    @DisplayName("회원가입을 성공적으로 진행한다")
    void signup() {
        UserJoinRequest userJoinRequest = new UserJoinRequest(neighborhood.getId(), "asd@naver.com", "닉네임", "홍길동", "010-1234-1234", "12341234");
        User user = joinService.signup(userJoinRequest);
        assertThat(userRepository.findById(user.getId()).isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일이 중복이면 예외가 발생한다")
    void duplicateEmail() {
        UserJoinRequest userJoinRequest = new UserJoinRequest(neighborhood.getId(), "asd@naver.com", "닉네임", "홍길동", "010-1234-1234", "12341234");
        joinService.signup(userJoinRequest);

        UserJoinRequest userJoinRequest1 = new UserJoinRequest(neighborhood.getId(), "asd@naver.com", "닉네임123", "홍길동", "010-1234-1234", "12341234");
        assertThatThrownBy(() -> joinService.signup(userJoinRequest1))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("닉네임이 중복이면 예외가 발생한다")
    void duplicateNickname() {
        UserJoinRequest userJoinRequest = new UserJoinRequest(neighborhood.getId(), "asd@naver.com", "닉네임", "홍길동", "010-1234-1234", "12341234");
        joinService.signup(userJoinRequest);

        UserJoinRequest userJoinRequest1 = new UserJoinRequest(neighborhood.getId(), "asd123@naver.com", "닉네임", "홍길동", "010-1234-1234", "12341234");
        assertThatThrownBy(() -> joinService.signup(userJoinRequest1))
                .isInstanceOf(DuplicateKeyException.class);
    }

}
