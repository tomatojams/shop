package com.apple.shop.Member;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
가입 로그인 라우팅
*/

@Controller
public class Router_Auth {

  @Autowired
  private DBClinet_Member DBClientMember;
  @Autowired
  // 매번 새로운 object를 생성하기보다는 인젝션이 효율적임
  private PasswordEncoder passwordEncoder;

  // 회원가입 페이지
  @GetMapping("/join")
  public String joinPage() {
    return "join";
  }

  @GetMapping("/login")
  public String LoginPage() {
    return "login";
  }

  // 회원가입 처리
  @PostMapping("/join")
  public String join(@RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("passwordConfirm") String passwordConfirm) {
    if (!password.equals(passwordConfirm)) {
      return "redirect:/join?error=passwordMismatch";
    }
    if (password.length() < 8 || username.length() < 8) {
      return "redirect:/join?error=passwordLengthError";
    }

    try {
      // 가입 함수 불러옴
      this.registerMember(username, password);
    } catch (IllegalArgumentException e) {
      return "redirect:/join?error=" + e.getMessage();
    }
    return "redirect:/login";
  }

  // Security Config  에서 @EnableGlobalMethodSecurity(prePostEnabled = true) 를 해야
  // 아래 어노테이션이 작동함
  //@PreAuthorize("isAnonymous()")  로 테스트해봄
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/my-page")
  public String myPage(Authentication auth, Principal principal, Model model) {
    // 타입 캐스팅 해야 제대로 나올수있음
    CustomUser result = (CustomUser) auth.getPrincipal();
    System.out.println("getDisplayName" + " " + result.displayName);

    System.out.println(auth.getAuthorities());
    System.out.println(auth.isAuthenticated());
    System.out.println(principal.getName());
    System.out.println(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    model.addAttribute("user", auth.toString());
//    if (auth == null) {
//      return "error";
//    }
    return "my-page";
  }

  // 저장함수 Service로 빼기는 단순해서
  public void registerMember(String username, String rawPassword) {

    if (DBClientMember.findByUsername(username) != null) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    }
    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(rawPassword);
    // 사용자 정보 저장
    Member member = new Member();
    member.setUsername(username);
    member.setPassword(encodedPassword);
    member.setEnabled(true); // 계정 활성화 설정
    this.DBClientMember.save(member);
  }


}
