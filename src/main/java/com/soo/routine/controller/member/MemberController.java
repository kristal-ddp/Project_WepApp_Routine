package com.soo.routine.controller.member;

import com.soo.routine.dto.member.*;
import com.soo.routine.entity.member.Member;
import com.soo.routine.entity.member.Role;
import com.soo.routine.security.LoginUser;
import com.soo.routine.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    /*
    User Page
    */

    // 시작 페이지
    @GetMapping(value = {"/", "startRoutine"})
    public String startRoutine(){
        return "mypage/startRoutine";
    }

    // 회원가입 페이지
    @GetMapping("join")
    public String join(MemberJoinDTO memberJoinDTO){
        return "mypage/member/join";
    }

    // 회원가입
    @PostMapping("join")
    public String join(@Valid @ModelAttribute()
                           MemberJoinDTO memberJoinDTO, BindingResult bindingResult, Model model){

        // 이메일 중복 처리
        if(memberService.isEmailExists(memberJoinDTO.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 처리
        if(memberService.isNicknameExists(memberJoinDTO.getNickname())) {
            bindingResult.rejectValue("nickname", "error.nickname", "이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 불일치 처리
        if(memberJoinDTO.getPwd2() != "" && !memberJoinDTO.getPwd().equals(memberJoinDTO.getPwd2())) {
            bindingResult.rejectValue("pwd2", "error.pwd2", "비밀번호가 일치하지 않습니다.");
        }

        // 에러 처리
        if(bindingResult.hasErrors()){ // 검증 실패시
            model.addAttribute("memberJoinDTO", memberJoinDTO); // 입력 데이터 값을 유지
            return "mypage/member/join";
        }

        memberService.join(memberJoinDTO);
        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("login")
    public String login(MemberDTO memberDTO, @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "mypage/member/login";
    }

    // 로그아웃
    @GetMapping("logout")
    public String logout() {
        return "redirect:/login";
    }

    // 비밀번호 찾기 페이지
    @GetMapping("pwd-find")
    public String pwdFind(@ModelAttribute("memberDTO") MemberDTO memberDTO){
        return "mypage/member/pwd_find";
    }

    // 비밀번호 찾기
    @PostMapping("pwd-find")
    public String pwdFind(MemberDTO memberDTO, BindingResult bindingResult, Model model) throws Exception {

        if(bindingResult.hasErrors()){
            model.addAttribute("memberDTO", memberDTO);
            return "mypage/member/pwd_find";
        }

        Member member = memberService.pwdFind(memberDTO.getEmail());

        if(member == null) {
            bindingResult.addError(new FieldError("memberDTO", "email", "이메일이 올바르지 않습니다."));
            return "mypage/member/pwd_find";
        }

        return "redirect:/login";
    }

    // 마이페이지
    @GetMapping("mypage")
    public String mypage(@LoginUser SessionDTO sessionDTO, Model model) {

        model.addAttribute("sessionDTO", sessionDTO);
        return "mypage/member/mypage";
    }

    // 회원정보 수정 페이지
    @GetMapping("mypage-edit")
    public String editProfile(@LoginUser SessionDTO sessionDTO, Model model){

        if(sessionDTO != null) {

            MemberEditDTO memberEditDTO = memberService.editPage(sessionDTO.getEmail());
            model.addAttribute("memberEditDTO", memberEditDTO);
        }

        return "mypage/member/edit_profile";
    }

    // 회원정보 수정
//    @PostMapping("mypage-edit")
    @PutMapping("mypage-edit")
    public ResponseEntity<String> editProfile(@RequestBody MemberEditDTO memberEditDTO) throws Exception { // @RequestBody : ajax에서 JSON데이터 Http Body에 담아서 요청하기 때문에 사용
//    public String editProfile(@LoginUser SessionDTO sessionDTO, @RequestBody MemberEditDTO memberEditDTO, BindingResult bindingResult, Model model) {

//        if(bindingResult.hasErrors()){
//
//            model.addAttribute("sessionDTO", sessionDTO);
//            model.addAttribute("memberEditDTO", memberEditDTO);
//            return "mypage/member/edit_profile";
//        }

        memberService.edit(memberEditDTO);

        // 변경된 Session 등록
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(memberEditDTO.getNewPwd(), memberEditDTO.getNickname()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(HttpStatus.OK);
//        return "mypage/member/edit_profile";
    }

    // 회원탈퇴 페이지
    @GetMapping("mypage-withdraw")
    public String withdraw(MemberDTO memberDTO){
        return "mypage/member/withdraw";
    }

    // 회원탈퇴
    @PostMapping("mypage-withdraw")
    public String withdraw(@LoginUser SessionDTO sessionDTO, MemberDTO memberDTO,
                           BindingResult bindingResult, Model model){

        Member checkPwd = memberService.checkPwd(sessionDTO.getEmail(), memberDTO.getPwd());

        if (checkPwd == null) { // 비밀번호 불일치 시

            bindingResult.addError(new FieldError("memberDTO", "pwd", "비밀번호가 일치하지 않습니다."));
            return "mypage/member/withdraw";

        }else { // 비밀번호 일치 시

            memberService.withdraw(sessionDTO.getEmail());
            return "redirect:/login";
        }
    }

    /*
    Admin Page
    */

    @GetMapping("admin")
    public String admin() {
        return "redirect:/admin/user-list?role=MEMBER";
    }

    @GetMapping("admin/user-list")
    public String memberList(Model model, Role role, @PageableDefault Pageable pageable) {

        Page<MemberReadDTO> lists = memberService.getMemberList(role, pageable);

		model.addAttribute("lists", lists);
		model.addAttribute("role", role.name());
        model.addAttribute("pageName", role.name() + " List");
        return "admin/user/list";
    }


    /*
    임시 페이지
    */

    // 접근 불가 페이지
    @GetMapping("deny")
    public String deny() {
        return "mypage/deny";
    }

    // 준비중인 페이지
    @GetMapping("recommend")
    public String recommend() {
        return "mypage/getReady";
    }

    @GetMapping("analysis")
        public String analysis() {
            return "mypage/getReady";
        }

}
