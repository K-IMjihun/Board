package com.board.board.Service;

import com.board.board.Dto.ResultResponseDto;
import com.board.board.Dto.UserDto.LoginRequestDto;
import com.board.board.Dto.UserDto.SignupRequestDto;
import com.board.board.Entity.User;
import com.board.board.Jwt.JwtUtil;
import com.board.board.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResultResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        // 사용자 등록
        User user = new User(username, password);
        log.info("user: {}", user);
        userRepository.save(user);
        ResultResponseDto resultResponseDto = new ResultResponseDto("회원가입 성공", 200L);

        return resultResponseDto;
    }

    public ResponseEntity<ResultResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user =  userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("등록되지 않은 회원입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUsername());

        HttpHeaders headers = new HttpHeaders();
        // 헤더 생성
        headers.add(JwtUtil.AUTHORIZATION_HEADER, token);

        return new ResponseEntity<>(new ResultResponseDto("로그인 성공!", 200L), headers, HttpStatus.OK);
    }
}
