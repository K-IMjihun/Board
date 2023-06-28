package com.board.board.Service;

import com.board.board.Dto.UserDto.UserRequestDto;
import com.board.board.Dto.UserDto.UserResponseDto;
import com.board.board.Entity.User;
import com.board.board.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = new UserResponseDto();

        // 아이디 존재여부 확인
        boolean hasId = userRepository.findByUsername(userRequestDto.getUsername()) == null;

        userResponseDto.setSuccess(hasId);
        if(hasId){
            User user = new User(userRequestDto);
            userRepository.save(user);
        }
        return userResponseDto;

    }
    @Transactional
    public UserResponseDto login(UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = new UserResponseDto();
        User user = loginCheck(userRequestDto);
        user.setLogin(true);

        userResponseDto.setSuccess(true);
        return userResponseDto;

    }

    private User loginCheck(UserRequestDto userRequestDto) {
        if(userRequestDto.getPassword().equals(userRepository.findByUsername(userRequestDto.getUsername()).getPassword())){
            return userRepository.findByUsername(userRequestDto.getUsername());
        }
        else {
            throw new IllegalArgumentException("개인정보를 다시한번 확인해 주세요");
        }
        }
}
