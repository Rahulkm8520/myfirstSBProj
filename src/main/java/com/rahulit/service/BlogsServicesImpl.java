package com.rahulit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahulit.Utils.AppUtils;
import com.rahulit.binding.AddBlogRequestModel;
import com.rahulit.binding.BlogsDataResp;
import com.rahulit.binding.CommentRequestAndResp;
import com.rahulit.entity.CommentEntity;
import com.rahulit.entity.PostEntity;
import com.rahulit.entity.UserEntity;
import com.rahulit.repo.CommentRepo;
import com.rahulit.repo.PostRepo;
import com.rahulit.repo.UserRepo;

@Service
public class BlogsServicesImpl implements BlogsServices {

	@Autowired
	PostRepo postRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	HttpSession httpSession;
	
	@Autowired
	CommentRepo commentRepo;

	@Override
	public String addBlog(AddBlogRequestModel addBlogRequest) {

		Optional<UserEntity> userData = userRepo.findById((Integer) httpSession.getAttribute(AppUtils.USER_ID));

		PostEntity postEntity = new PostEntity();
		if(addBlogRequest.getBlogId() != null && addBlogRequest.getBlogId() != 0) {
			postEntity.setPost_id(addBlogRequest.getBlogId());
		}
		
		postEntity.setContent(addBlogRequest.getContent());
		postEntity.setTitle(addBlogRequest.getTitle());
		postEntity.setDescription(addBlogRequest.getShortDescription());
		postEntity.setUser(userData.get());
		postRepo.save(postEntity);
		return AppUtils.POST_ADDED_SUCCESS;
	}

	@Override
	public String editBlog(AddBlogRequestModel addBlogRequest) {
		Optional<UserEntity> userData = userRepo.findById((Integer) httpSession.getAttribute(AppUtils.USER_ID));

		PostEntity postEntity = new PostEntity();
		postEntity.setContent(addBlogRequest.getContent());
		postEntity.setTitle(addBlogRequest.getTitle());
		postEntity.setDescription(addBlogRequest.getShortDescription());
		postEntity.setUser(userData.get());
		postRepo.save(postEntity);
		return AppUtils.POST_ADDED_SUCCESS;
	}

	@Override
	public String deleteBlog(Integer blogId) {
		Optional<PostEntity> postData = postRepo.findById(blogId);
		PostEntity postEntity = null;
		if(postData.isPresent()) {
			postEntity = postData.get();
		}
		postEntity.setDeleted(true);
		postRepo.save(postEntity);
		return "DELETED";
	}

	@Override
	public List<BlogsDataResp> getAllBlogs() {
		List<BlogsDataResp> blogsDataResps = new ArrayList<>();
		postRepo.findByDeletedIsFalse().iterator().forEachRemaining(allBlogsData -> {
			BlogsDataResp data = new BlogsDataResp();
			data.setBlogId(allBlogsData.getPost_id());
			data.setContent(allBlogsData.getContent());
			data.setTitle(allBlogsData.getTitle());
			data.setShortDescription(allBlogsData.getDescription());
			data.setCreatedOn(allBlogsData.getCreatedOn().toString());
			blogsDataResps.add(data);
		});

		return blogsDataResps;
	}

	@Override
	public List<BlogsDataResp> getBlogsByUserId() {
		//System.out.println((Integer) httpSession.getAttribute(AppUtils.USER_ID));
		Optional<UserEntity> userData = userRepo.findById((Integer) httpSession.getAttribute(AppUtils.USER_ID));
		UserEntity userEntity = userData.get();
		List<PostEntity> findByUser = postRepo.findByUserAndDeletedIsFalse(userEntity);
		List<BlogsDataResp> blogsDataResps = new ArrayList<>();
		if (findByUser != null && findByUser.size() > 0) {
			findByUser.forEach(currentUsersAllPost -> {
				BlogsDataResp data = new BlogsDataResp();
				data.setBlogId(currentUsersAllPost.getPost_id());
				data.setContent(currentUsersAllPost.getContent());
				data.setTitle(currentUsersAllPost.getTitle());
				data.setShortDescription(currentUsersAllPost.getDescription());
				data.setCreatedOn(currentUsersAllPost.getCreatedOn().toString());
				blogsDataResps.add(data);
			});
		}
		return blogsDataResps;
	}

	@Override
	public String addCommentToBlog(CommentRequestAndResp commentRequest) {
		
		Optional<PostEntity> postData = postRepo.findById(commentRequest.getBlogId());
		
		CommentEntity commentEntity = new CommentEntity();
		commentEntity.setEmail(commentRequest.getEmail());
		commentEntity.setContent(commentRequest.getComment());
		commentEntity.setName(commentRequest.getName());
		commentEntity.setPost(postData.get());
		commentRepo.save(commentEntity);
		return "SAVED";
	}

	@Override
	public List<CommentRequestAndResp> getAllCommentsByBlogId(Integer blogId) {
		Optional<PostEntity> postData = postRepo.findById(blogId);
		PostEntity postEntity = postData.get();
		List<CommentEntity> allCommentByPostId = commentRepo.findByPostAndDeletedIsFalse(postEntity);
		List<CommentRequestAndResp> commentsData =  new ArrayList<>();
		allCommentByPostId.forEach(data->{
			CommentRequestAndResp commentObj =  new CommentRequestAndResp();
			commentObj.setComment(data.getContent());
			commentObj.setEmail(data.getEmail());
			commentObj.setName(data.getName());
			commentObj.setCommentId(String.valueOf(data.getComment_id()));
			commentObj.setCommentedOn(data.getCreatedOn().toString());
			commentsData.add(commentObj);
		});
		return commentsData;
	}

	@Override
	public BlogsDataResp getBlogsByBlogId(Integer blogId) {
		Optional<PostEntity> blogPost = postRepo.findById(blogId);

		BlogsDataResp blogsDataResp = new BlogsDataResp();
		if (blogPost.isPresent()) {
			PostEntity postEntity = blogPost.get();
			blogsDataResp.setBlogId(postEntity.getPost_id());
			blogsDataResp.setContent(postEntity.getContent());
			blogsDataResp.setShortDescription(postEntity.getDescription());
			blogsDataResp.setTitle(postEntity.getTitle());
		}
		return blogsDataResp;
	}

	@Override
	public List<CommentRequestAndResp> getAllCommentsByUserId() {
		Optional<UserEntity> userData = userRepo.findById((Integer) httpSession.getAttribute(AppUtils.USER_ID));
		UserEntity userEntity = userData.get();
		List<PostEntity> findByUser = postRepo.findByUserAndDeletedIsFalse(userEntity);
		List<Integer> postId =  new ArrayList<>();
		findByUser.forEach(data->{
			(postId).add(data.getPost_id());
		});
		
		List<CommentEntity> allCommentByPostId  = commentRepo.findByPostInAndDeletedIsFalse(findByUser);
		
		List<CommentRequestAndResp> commentsData =  new ArrayList<>();
		allCommentByPostId.forEach(data->{
			CommentRequestAndResp commentObj =  new CommentRequestAndResp();
			commentObj.setCommentId(String.valueOf(data.getComment_id()));
			commentObj.setComment(data.getContent());
			commentObj.setEmail(data.getEmail());
			commentObj.setName(data.getName());
			commentObj.setCommentedOn(data.getCreatedOn().toString());
			commentsData.add(commentObj);
		});
		
		return commentsData;
	}

	@Override
	public String deleteComment(Integer commentId) {
		Optional<CommentEntity> commentData = commentRepo.findById(commentId);
		CommentEntity commentEntity  = commentData.get();
		commentEntity.setDeleted(true);
		commentRepo.save(commentEntity);
		return "SUCCESS";
	}

	@Override
	public List<BlogsDataResp> searchBlogs(String keyWord) {
		List<PostEntity> allData = postRepo.findByDeletedIsFalseAndTitleContainsOrDescriptionContains(keyWord,keyWord);
		List<BlogsDataResp> blogsDataResps = new ArrayList<>();
		allData.forEach(allBlogsData -> {
			BlogsDataResp data = new BlogsDataResp();
			data.setBlogId(allBlogsData.getPost_id());
			data.setContent(allBlogsData.getContent());
			data.setTitle(allBlogsData.getTitle());
			data.setShortDescription(allBlogsData.getDescription());
			data.setCreatedOn(allBlogsData.getCreatedOn().toString());
			blogsDataResps.add(data);
		});
		return blogsDataResps;
	}

}

