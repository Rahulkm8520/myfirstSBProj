package com.rahulit.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahulit.Utils.AppUtils;
import com.rahulit.binding.LoginRequestModel;
import com.rahulit.binding.SignUpRequestModel;
import com.rahulit.entity.UserEntity;
import com.rahulit.repo.UserRepo;



@Service
public class UserServiceImpl implements UserServices {

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	HttpSession httpSession;
	
	@Override
	public String login(LoginRequestModel loginRequest) {
		Optional<UserEntity> findByEmailAndPwd = userRepo.
												 findByEmailAndPwd(loginRequest.getEmail(), 
												 loginRequest.getPassword());
		if(findByEmailAndPwd.isPresent()) {
			httpSession.setAttribute(AppUtils.USER_ID, findByEmailAndPwd.get().getId());
			return AppUtils.LOGIN_SUCCESS;
		}
		return AppUtils.LOGIN_FAILURE;
	}

	@Override
	public String signUp(SignUpRequestModel signUpRequest) {
		
		Optional<UserEntity> findByEmail = userRepo.findByEmail(signUpRequest.getEmail());
		if(findByEmail.isPresent()) {
			return AppUtils.SIGN_UP_DUPLICATE_EMAIL_ERROR;
		}
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(signUpRequest, userEntity);
		userEntity.setUserFName(signUpRequest.getFirstName());
		userEntity.setUserLName(signUpRequest.getLastName());
		userEntity.setPwd(signUpRequest.getPassword());
		userRepo.save(userEntity);
		return AppUtils.SIGN_UP_SUCCESS;
	}

}

