package com.rahulit.service;

import com.rahulit.binding.LoginRequestModel;
import com.rahulit.binding.SignUpRequestModel;

public interface UserServices {
	
	public String login(LoginRequestModel loginRequest);
	public String signUp(SignUpRequestModel signUpRequest);
}
