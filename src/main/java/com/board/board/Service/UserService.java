package com.board.board.Service;

import com.board.board.Dto.UserDto.UserRequestDto;
import com.board.board.Dto.UserDto.UserResponseDto;
import com.board.board.Entity.User;
import com.board.board.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setSuccess(false);
        idCheck(userRequestDto.getUsername());

        User user = new User(userRequestDto);
        userRepository.save(user);
        userResponseDto.setSuccess(true);
        return userResponseDto;

    }

    private void idCheck(String username) {
        if(userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("존재하는 아이디 입니다.");
        }
    }
}
