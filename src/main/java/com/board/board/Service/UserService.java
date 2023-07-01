package com.board.board.Service;

import com.board.board.Dto.UserDto.SignupRequestDto;
import com.board.board.Entity.User;
import com.board.board.Entity.UserRoleEnum;
import com.board.board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        // 사용자 확인을 위해 우선 일반 사용자 권한으로 코드 실행
        UserRoleEnum role = UserRoleEnum.USER;
        // Admin이 true(관리자)라면 실행
        if (requestDto.isAdmin()) {
            // ADMIN_TOKEN과 requestDto.getAdminToken()이 다르다면
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            // 위의 if문을 통과했으므로 사용자 권한을 ADMIN(관리자)로 덮어씌움
            role = UserRoleEnum.ADMIN;
        }

        // 일반 사용자 등록
        User user = new User(username, password, role);
        log.info("user: {}", user);
        userRepository.save(user);
    }
}
