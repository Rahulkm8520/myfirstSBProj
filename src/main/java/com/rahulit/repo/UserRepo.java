package com.rahulit.repo;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rahulit.entity.UserEntity;

public interface UserRepo extends CrudRepository<UserEntity, Serializable> {

	public Optional<UserEntity> findByEmailAndPwd(String email, String pwd);
	
	public Optional<UserEntity> findByEmail(String email);
}
