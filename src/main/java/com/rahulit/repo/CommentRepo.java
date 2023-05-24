package com.rahulit.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rahulit.entity.CommentEntity;
import com.rahulit.entity.PostEntity;

public interface CommentRepo extends CrudRepository<CommentEntity, Serializable> {

	List<CommentEntity> findByPostAndDeletedIsFalse(PostEntity post);

	List<CommentEntity> findByPostInAndDeletedIsFalse(List<PostEntity> postId);

}
