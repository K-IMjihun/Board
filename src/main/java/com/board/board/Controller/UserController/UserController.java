package com.board.board.Controller.UserController;

import com.board.board.Dto.ResultResponseDto;
import com.board.board.Dto.UserDto.LoginRequestDto;
import com.board.board.Dto.UserDto.SignupRequestDto;
import com.board.board.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/login")
    public ResponseEntity<ResultResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping("/user/signup")
    public ResultResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {

        log.info(requestDto.getUsername() + " , " + requestDto.getPassword());
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new RuntimeException("데이터 유효성 검사 오류");
        }
        return userService.signup(requestDto);
    }
}