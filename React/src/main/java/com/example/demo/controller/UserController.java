package com.example.demo.controller;

import com.example.demo.Security.TokenProvider;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.model.UserRole;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

  @Autowired
  private UserService userService;

  // Bean으로 작성해도 됨.
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


  @Autowired
  private TokenProvider tokenProvider;

  

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
	  
	System.err.print("call---> " + userDTO);
    try {
      if(userDTO == null || userDTO.getPassword() == null ) {
        throw new RuntimeException("Invalid Password value.");
      }
      if (userDTO.getUsername() == null || userDTO.getName() == null || userDTO.getAddr() == null ||
              userDTO.getPhone() == null || userDTO.getEmail() == null ) {
              throw new RuntimeException("유저 값이 없습니다.");
          }
      System.err.print("call2---> ");

      // 요청을 이용해 저장할 유저 만들기
      UserEntity user = UserEntity.builder()
          .username(userDTO.getUsername())
          .password(passwordEncoder.encode(userDTO.getPassword()))
          .name(userDTO.getName())
          .email(userDTO.getEmail())
          .addr(userDTO.getAddr())
          .phone(userDTO.getPhone())
          .role(UserRole.USER)
          .build();
      
      // 서비스를 이용해 리포지터리 에 유저 저장
      UserEntity registeredUser = userService.create(user);
      UserDTO responseUserDTO = UserDTO.builder()
          .id(registeredUser.getId())
          .username(registeredUser.getUsername())
          .name(registeredUser.getName())
          .email(registeredUser.getEmail())
          .addr(registeredUser.getAddr())
          .phone(registeredUser.getPhone())
          .role(UserRole.USER)
          .build();

      return ResponseEntity.ok().body(responseUserDTO);
    } catch (Exception e) {
      // 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.

      System.err.println("HERE......." + e);
      ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
      return ResponseEntity
          .badRequest()
          .body(responseDTO);
    }
  }


  @PostMapping("/checkEmail")
  public ResponseEntity<?> checkEmail(@RequestBody UserEntity userEntity) {
      boolean isDuplicate = userService.checkEmail(userEntity);
      if (isDuplicate) {
          // 중복된 이메일 주소인 경우
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "중복된 이메일 주소입니다."));
      } else {
          // 유효한 이메일 주소인 경우
          return ResponseEntity.ok(Map.of("message", "유효한 이메일 주소입니다."));
      }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
    UserEntity user = userService.getByCredentials(
        userDTO.getUsername(),
        userDTO.getPassword(),
        passwordEncoder);

    if(user != null) {
      // 토큰 생성
      final String token = tokenProvider.create(user);
      final UserDTO responseUserDTO = UserDTO.builder()
          .username(user.getUsername())
          .id(user.getId())
          .token(token)
          .build();
      return ResponseEntity.ok().body(responseUserDTO);
    } else {
      ResponseDTO responseDTO = ResponseDTO.builder()
          .error("Login failed.")
          .build();
      return ResponseEntity
          .badRequest()
          .body(responseDTO);
    }
  }
  
  

 

}