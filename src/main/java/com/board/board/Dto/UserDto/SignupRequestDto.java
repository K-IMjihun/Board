package com.board.board.Dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
        @NotBlank
        @Pattern(regexp = "[a-z0-9]+", message = "소문자 a~z와 숫자 0~9만 사용할 수 있습니다.")
        @Size(min = 4, max = 10, message = "길이는 4글자 이상, 10글자 이하여야 합니다.")
        private String username;
        @NotBlank
        @Pattern(regexp = "[a-z0-9]+", message = "영문자 a~z, A~Z 와 숫자 0~9만 사용할 수 있습니다.")
        @Size(min = 8, max = 15, message = "길이는 8글자 이상, 15글자 이하여야 합니다.")
        private String password;
}
