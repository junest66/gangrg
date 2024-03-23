package com.project.gangrg.service;

import com.project.gangrg.domain.Neighborhood;
import com.project.gangrg.domain.User;
import com.project.gangrg.dto.UserJoinRequest;
import com.project.gangrg.repository.NeighborhoodRepository;
import com.project.gangrg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinService {

    private final UserRepository userRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User signup(UserJoinRequest userJoinRequest) {
        String encodePassword = bCryptPasswordEncoder.encode(userJoinRequest.getPassword());
        Neighborhood neighborhood = neighborhoodRepository.findById(userJoinRequest.getNeighborhoodId()).orElseThrow(() -> new IllegalArgumentException("해당 동네가 존재하지 않습니다."));
        validateSignup(userJoinRequest.getEmail(), userJoinRequest.getNickname());
        User user = userJoinRequest.toEntity(neighborhood, encodePassword);
        return userRepository.save(user);
    }

    private void validateSignup(String email, String nickname) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateKeyException("아이디가 중복입니다.");
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateKeyException("닉네임이 중복입니다.");
        }
    }
}
