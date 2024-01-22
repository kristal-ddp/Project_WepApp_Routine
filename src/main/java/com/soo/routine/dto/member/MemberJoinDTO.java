package com.soo.routine.dto.member;

import com.soo.routine.entity.member.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberJoinDTO {

    private Long memberId; // 회원번호
    private Role role; // 회원등급
    private LocalDateTime joinDate; // 가입일

    @NotBlank(message="이메일 주소를 입력하세요.")
    @Email(message = "이메일 형식에 맞게 입력하세요.")
    private String email; // 이메일 주소

    @NotBlank(message="비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])(?=\\S+$).{5,15}",
            message = "5~15자 이내의 영문 및 숫자를 사용하세요.")
    private String pwd; // 비밀번호 확인

    @NotBlank(message="비밀번호를 입력하세요.")
    private String pwd2; // 비밀번호 확인

    @NotBlank(message="닉네임을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,10}$", message = "특수문자를 제외하고 2-10자로 입력하세요.")
    private String nickname; // 닉네임

    @NotBlank(message="성별을 선택하세요.")
    private String gender; // 성별

    @NotBlank(message = "")
    @Pattern(regexp = "^[0-9]{8}$", message = "생년월일 형식에 맞게 입력하세요.")
    private String birth; // 생년월일

}