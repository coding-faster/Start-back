package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.UserDAO;
import com.example.domain.UserVO;
import com.example.util.JWTutil;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserDAO dao;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JWTutil jwtUtil; //JWt 유틸리티 클래스 끌어오기
	
	@PostMapping("/login")
	public int login(@RequestBody UserVO vo) {
		int result = 0;
		UserVO user= dao.login(vo.getUid());
		if(user != null) {
			if(user.getState() == 0) {
				result =3; //탈퇴한 회원 0 리턴
			}else {
				//암호화비번 검증
//				if(encoder.matches(vo.getUpass(), user.getUpass())) {		// 암호화 비번 처리는 matches 로 해야 함 
				if(vo.getUpass().equals(user.getUpass())) {  //이게 기본 스트링비교는 equals ,  int는 == , 이렇게 하면 암호화 비번 때 실패할 것 그러니 위로바꾸기
					result = 1; // 로그인 성공 1 리턴
				}else {
					result = 2; //로그인 틀림 2 리턴
				}
			}
		}else {
			//아이디 미입력  or 아이디 틀림 0리턴
			result = 0;
		}
		System.out.println(user);
		return result;
		
		
		}
		@PostMapping("/JWTlogin")    // postman 확인 실패 
	public Map<String,Object> JWTlogin(@RequestBody UserVO vo) {    //vo 는 프론트에서 받을 바구니 
			Map<String,Object> res= new HashMap<>();
			UserVO user = dao.login(vo.getUid());					//sql문을 실행한 결과로 받을 것이니까 vo라고 똑같은 이름을 주어서는 안됨
			if(user !=null) {
				if(user.getState() == 0) {
					res.put("status", 3); // 탈퇴한 회원
					
				}else {
					//암호화된 비밀번호 검증 ( 기존 로직 유지) 
					if(encoder.matches(vo.getUpass(),user.getUpass())) {
						//JWT 토큰 생성 (새로운 객체 사용)
						 String token = jwtUtil.generateToken(user);
						 res.put("status", 1); //로그인 성공
						 res.put("token", token);	// 생성된 jwt 토큰 반환
					}else {
						res.put("status", 2); // 로그인 실패(비밀번호 틀림)
					}
				}
			}else {
				res.put("status", 0); // 아이디 미입력 또는 아이디 틀림
			}
			return res;
			
			
	}

}
