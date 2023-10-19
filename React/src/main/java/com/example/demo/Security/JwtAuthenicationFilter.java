package com.example.demo.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component
;import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component//컴포넌트로 정의해줘야된다.

public class JwtAuthenicationFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenProvider tokenProvider;
	
	private String parseBearerToken(HttpServletRequest request) {
		
		//bearerToken : 토큰을 전송하면 브라우저에  토큰의 키 이름을 나타냅니다.
		//이 값이 있다면 토큰값만 분리해서 리턴함.
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			
			System.err.println("파싱된 토큰 값 : " + bearerToken.substring(7));
			return bearerToken.substring(7);
		}
		
		return null;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			try {
					String token = parseBearerToken(request);
					
					if(token != null && !token.equalsIgnoreCase("null")) {
						//UserId 가져와서 위조 여부 검사
						String userId = tokenProvider.validateAndSetUserId(token);
						System.err.println("인증 USER ID : " + userId);
						
						//인증이 완료되었으면, 인증을 담당하는 ContextHolder 에 등록을 해줘야만  부트 시큐어에서 인증된 사용자로 인식함
						
						//principal 아래 생성자에서 첫번째 파라미터인 userId는 해당 ID 로 부여된 토큰을 filter 가 검증할수 있는 key입니다.
						//이 값은 주어진 변수 이름 그대로 컨트롤러에서 사용할수 있는데 그 제공자로 @principal로 제공됩니다.
						AbstractAuthenticationToken authenticationToken = 
								new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
						securityContext.setAuthentication(authenticationToken);
						SecurityContextHolder.setContext(securityContext);
					}
				
			} catch (Exception e) {
				logger.error("JWT 인증 필터에서 예외 발생함 : " + e.getMessage());
			}
			
			filterChain.doFilter(request, response);

	}

}
