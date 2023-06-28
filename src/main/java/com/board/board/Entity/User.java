package com.board.board.Entity;

import com.board.board.Dto.UserDto.UserRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Pattern(regexp = "[a-z0-9]+", message = "소문자 a~z와 숫자 0~9만 사용할 수 있습니다.")
    @Size(min = 4, max = 10, message = "길이는 4글자 이상, 10글자 이하여야 합니다.")
    private String username;

    @Column(nullable = false)
    @Pattern(regexp = "[a-zA-z0-9]+", message = "영문자 a~z, A~Z 와 숫자 0~9만 사용할 수 있습니다.")
    @Size(min = 8, max = 15, message = "길이는 8글자 이상, 15글자 이하여야 합니다.")
    private String password;

    @Column
    private boolean login;

    public User(UserRequestDto userRequestDto) {
        this.username = userRequestDto.getUsername();
        this.password = userRequestDto.getPassword();
    }

}
