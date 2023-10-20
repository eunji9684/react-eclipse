package com.example.demo.dto;

import com.example.demo.model.UserEntity;
import com.example.demo.model.UserRole;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
  private String token;
  private String username;
  private String password;
  private String passwordConfirm;
  private String id;
  private String email;
  private String addr;
  private String phone;
  private String name;
  private UserRole role;
  

  public static UserEntity toUserEntity(UserDTO userDTO) {
		
		
		
		UserEntity userentity = UserEntity.builder()
						.id(userDTO.getId())
						.addr(userDTO.getAddr())
						.email(userDTO.getEmail())
						.username(userDTO.getUsername())
						.name(userDTO.getName())
						.phone(userDTO.getPhone())
						//.role(UserRole.USER)
						.role((UserRole)userDTO.getRole())
						.build();
		
		
		
		return userentity;
	}
  
}