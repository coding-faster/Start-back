package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.UserDAO;
import com.example.domain.UserVO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserDAO dao;
	
	@Autowired
	PasswordEncoder encoder;
	
	@PostMapping("/login")
	public int login(@RequestBody UserVO vo) {
		int result = 0;
		UserVO user= dao.login(vo.getUid());
		if(user != null) {
			if(user.getState() == 0) {
				result =3; //탈퇴한 회원 0 리턴
			}else {
				//암호화비번 검증
//				if(encoder.matches(vo.getUpass(), user.getUpass())) {
				if(vo.getUpass().equals(user.getUpass())) {  //이게 기본 스트링비교는 equals ,  int는 == 
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
//		
	}

}
