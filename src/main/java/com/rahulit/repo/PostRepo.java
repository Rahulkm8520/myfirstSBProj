package com.rahulit.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rahulit.entity.PostEntity;
import com.rahulit.entity.UserEntity;

public interface PostRepo extends CrudRepository<PostEntity, Serializable>{

	public List<PostEntity> findByUserAndDeletedIsFalse(UserEntity user);
	
	public List<PostEntity> findByDeletedIsFalse();
	
	public List<PostEntity> findByDeletedIsFalseAndTitleContainsOrDescriptionContains(String keyword,String word);
}
