package com.board.board.Controller;


import com.board.board.Dto.UserDto.UserRequestDto;
import com.board.board.Dto.UserDto.UserResponseDto;
import com.board.board.Service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){

        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        // RequestDto -> Entity
        return userService.createUser(userRequestDto);
    }
    @PostMapping("login")
    public UserResponseDto login(@RequestBody UserRequestDto userRequestDto){
        return userService.login(userRequestDto);
    }

}
