package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;


import com.example.demo.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;



@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity {
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id; // 유저에게 고유하게 부여되는 id.

  @Column(nullable = false)
  private String username; // 아이디로 사용할 유저네임. 이메일일 수도 그냥 문자열일 수도 있다.

  private String password; // 패스워드.

  private UserRole role; // 유저의 롤.

  private String authProvider; // example : facebook
  

  private String email;
  
  private String addr;
  
  private String phone;
  private String name;
  

	
}