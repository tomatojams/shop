package com.apple.shop.Advice_Global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// 모든 컨트롤러에서 에러나면 실행
@ControllerAdvice
public class ExceptionHandler {

  // 에러처리용
  @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> MethodArgumentTypeMismatchhandler() {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("전역에러처리: 그런거없음");
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> IllegalArgumenthandler() {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("전역에러처리: 잘못된 입력");

  }
}
