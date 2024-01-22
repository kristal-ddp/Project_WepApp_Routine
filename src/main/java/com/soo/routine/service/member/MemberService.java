package com.soo.routine.service.member;

import com.soo.routine.dto.member.MemberEditDTO;
import com.soo.routine.dto.member.MemberJoinDTO;
import com.soo.routine.dto.member.MemberReadDTO;
import com.soo.routine.entity.member.Member;
import com.soo.routine.entity.member.Role;
import com.soo.routine.repository.member.MemberRepository;
import com.soo.routine.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    Member Page
     */

    // 회원가입
    public void join(MemberJoinDTO memberJoinDTO) {

        Member member = new Member(Role.MEMBER, LocalDateTime.now(),
                memberJoinDTO.getEmail(), passwordEncoder.encode(memberJoinDTO.getPwd()),
                memberJoinDTO.getNickname(), memberJoinDTO.getGender(), LocalDate.parse(memberJoinDTO.getBirth()));

        memberRepository.save(member);
    }

    // 이메일 중복 처리
    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 닉네임 중복 처리
    public boolean isNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }


    // 회원탈퇴
    public Member checkPwd(String email, String pwd) {
        return memberRepository.findByEmail(email) // email로 회원을 조회하고
                .filter(m -> this.passwordEncoder.matches(pwd, m.getPwd())) // 입력한 pwd와 암호화된 pwd(m.getPwd)가 같으면, 회원을 반환하고
                .orElse(null); // 다르면 null을 반환한다
    }

    // 비밀번호 찾기
    public Member pwdFind(String email) throws Exception {

        // 회원정보 불러오기
        Member member = memberRepository.findByEmail(email).orElse(null);

        // 이메일 전송
        if(member!=null) {

            // 임시 비밀번호 생성
            String tempPwd = UUID.randomUUID().toString().replace("-", ""); // - 제거
            tempPwd = tempPwd.substring(0, 10); // 10자리로 생성

            System.out.print("임시 비밀번호 : " + tempPwd);
            member.findPwd(tempPwd);

            // 이메일 전송
            MailUtil mail = new MailUtil();
            mail.sendMail(member);

            // 암호화된 임시 비밀번호 저장
            member.findPwd(passwordEncoder.encode(member.getPwd()));

            memberRepository.save(member);
        }
            return member;

    }
    
    // 회원정보 수정 페이지
    @Transactional
    public MemberEditDTO editPage(String email) {

        return memberRepository.findByEmail(email)
                .map(MemberEditDTO::new)
                .orElse(null);
    }

    // 회원정보 수정
    @Transactional
    public void edit(MemberEditDTO memberEditDTO) throws Exception {

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member member = memberRepository.findByEmail(memberEditDTO.getEmail()).orElse(null);
        System.out.print(member.getId());
        member.edit(passwordEncoder.encode(memberEditDTO.getPwd()),memberEditDTO.getNickname());

    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(String email) {

        Member member = memberRepository.findByEmail(email).orElse(null);
        member.withdraw(Role.valueOf("WITHDRAW")); // 회원 비활성화
        memberRepository.save(member);
    }

    /*
    Admin Page
    */
    public Page<MemberReadDTO> getMemberList(Role role, Pageable pageable) {

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));
		pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), Sort.by(sorts));

        return memberRepository.findByRole(role, pageable).map(MemberReadDTO::new);
    }
}
