package com.board.board.Filter;

import com.board.board.Dto.UserDto.LoginRequestDto;
import com.board.board.Entity.User;
import com.board.board.Jwt.JwtUtil;
import com.board.board.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
@RequiredArgsConstructor
@Order(2)
public class AuthFilter implements Filter {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = httpServletRequest.getRequestURI();
        log.info("현재 url: {}", url);
        if (StringUtils.hasText(url) && url.equals("/api/user/login")) {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            String username = requestDto.getUsername();
            String password = requestDto.getPassword();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("비밀번호가 맞지 않습니다.");
            }
            httpServletResponse.addHeader("token", jwtUtil.createToken(user.getUsername(), user.getRole()));
            chain.doFilter(request, response); // 다음 Filter 로 이동
        } else if (StringUtils.hasText(url) &&
                (url.equals("/") || url.startsWith("/index") || url.startsWith("/api/user")
                        || url.startsWith("/css") || url.startsWith("/js") || url.startsWith("/favicon.ico"))) {
            log.info("인증 처리를 하지 않는 url : " + url);
            // 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
            chain.doFilter(request, response); // 다음 Filter 로 이동
        } else {
            // 토큰 확인
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                String token = jwtUtil.substringToken(tokenValue);

                // 토큰 검증
                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("Token Error");
                }

                // 토큰에서 사용자 정보 가져오기
                Claims info = jwtUtil.getUserInfoFromToken(token);

                User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() ->
                        new NullPointerException("Not Found User")
                );

                log.info("login user : {}", user.getUsername());

                request.setAttribute("user", user);
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                throw new IllegalArgumentException("Not Found Token");
            }
        }
    }

}
