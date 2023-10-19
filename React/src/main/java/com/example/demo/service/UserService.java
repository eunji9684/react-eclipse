package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//아래 PasswordEncoder 사용하기 위해선 gradle 에 의존성 추가해야함.
//implementation 'org.springframework.security:spring-security-crypto:5.7.1'
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public UserEntity create(final UserEntity userEntity) {
    if(userEntity == null || userEntity.getUsername() == null ) {
      throw new RuntimeException("Invalid arguments");
    }
    final String username = userEntity.getUsername();
    if(userRepository.existsByUsername(username)) {
      log.warn("Username already exists {}", username);
      throw new RuntimeException("Username already exists");
    }

    return userRepository.save(userEntity);
  }

  public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
    final UserEntity originalUser = userRepository.findByUsername(username);

    // matches 메서드를 이용해 패스워드가 같은지 확인
    if(originalUser != null &&
        encoder.matches(password,
            originalUser.getPassword())) {
      return originalUser;
    }
    return null;
  }
}