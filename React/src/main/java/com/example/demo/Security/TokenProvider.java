package com.example.demo.Security;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

//이 클래스는 사용자의 정보를 받아서 JWT 토큰을 생성하는 역할을 합니다.
//나중에 이 토큰을 컨트롤러에 응답시 같이 보내게 됩니다.
//정의하는 방법은 형식화 되어 있으니 외우지 말고 개념만 잡으세요. 일반적으로 가져다 씁니다.

@Slf4j
@Service
public class TokenProvider {
	//제일먼저 사용자의 PK와 조합될 서버의 SecretKey를 선언합니다. 아무거나 선언해도 됩니다. 길게 하세요.
	
	private static final String SECRET_KEY = "abcdefgABCDEFG123456789!@#$%&*()_+-=[]|,./?>123123212141435423"
			+ "878978764asdfasdfadlkjasdljfalkdfjaldfjaldfjdsfakldfjlajfaldfjalkjfkladjfaklfjakdlfjalkktk";
	
	//컨트롤러에서 서브스로 등록시켜 사용자 정보를 담고있는 Entity를 전달해서
	//암호화된 토큰을 생성하는 메서드를 정의합니다.
	public String create(UserEntity userEntity) {
		//토큰의 유효 기간 설정부터 먼저 할게요.
	Date expireDate = Date.from(Instant.now().plus(10,ChronoUnit.DAYS));//10일간 유효한 토큰 날짜 생성함.
	
	/*
	 * jwt.bulder()를 이용해서 암호화된 토큰을 생성하는데, 결과는 아래처럼 구성될겁니다.
	 * {//header 부분
	 * 		"alg":여러 알고리즘중 하나"
	 * }.
	 * {
	 *	//Payload 부분
	 *	"sub":"특정값...",
	 	"iss":"스프링부트 생성시 group id",
	 	"ist":"특정숫자값",
	 	"exp":long값
	 	}.
	 	위 Secret_Key 를 이용해서 서명한 값 이 여기에 할당됨...(어떤값이 나올지는 나중에 확인해보죠)
	 */
	
	return Jwts.builder()
			//header 에 들어갈 내용 및 서명을 위한 비밀키
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			//payload 에 들어갈 내용 작성함
			.setSubject(userEntity.getId())//user pk와 비밀키로 생성한 값.
			.setIssuer("fullstack2")
			.setIssuedAt(expireDate)
			.compact();
	
	}
	
	//사용자로부터 전달받은 토큰을 64 Decoding 해서
	//헤더, 페이로드 부터 전달받은 값들을 서버의 비밀키 이용 서명 후, 넘어온 token 의 서명과 비교합니다.
	//만약 위조되지 않았다면 페이로드(Claims 객체)를 리턴, 아닌경우 예외 날려버림.
	//이중 사용자의 id와 조합해서 token 을 생성 했으니 id 값도 필요함.
	public String validateAndSetUserId(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();		
		 
	}
	
	
}
