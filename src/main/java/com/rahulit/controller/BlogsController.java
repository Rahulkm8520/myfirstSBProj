package com.rahulit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rahulit.Utils.AppUtils;
import com.rahulit.binding.AddBlogRequestModel;
import com.rahulit.binding.BlogsDataResp;
import com.rahulit.binding.CommentRequestAndResp;
import com.rahulit.service.BlogsServices;



@Controller
public class BlogsController {

	@Autowired
	BlogsServices blogsServices;

	@GetMapping(value = "/addblog")
	public String addPost(@RequestParam(name = "id", required = false) String blogId, Model model) {
		BlogsDataResp blogByBlogId = new BlogsDataResp();
		if (blogId != null) {
			blogByBlogId = blogsServices.getBlogsByBlogId(Integer.valueOf(blogId));
		}
		model.addAttribute("add_blog_req", blogByBlogId);
		return AppUtils.ADD_BLOG;
	}

	@PostMapping(value = "/addblog")
	public String addBlog(AddBlogRequestModel addBlogReq, Model model) {
		System.out.println(addBlogReq);
		String addBlog = blogsServices.addBlog(addBlogReq);
		System.out.println(addBlog);
		return "redirect:" + AppUtils.USER_INDEX;
	}

	@GetMapping(value = "/delete")
	public String deleteBlog(@RequestParam(name = "id", required = false) String blogId, Model model) {
		blogsServices.deleteBlog(Integer.valueOf(blogId));
		return "redirect:" + AppUtils.USER_INDEX;
	}

	@GetMapping(value = "/getBlogById")
	public String getMyBlogs(@RequestParam(name = "id") String blogId, Model model) {
		BlogsDataResp blogByBlogId = new BlogsDataResp();
		if (blogId != null) {
			blogByBlogId = blogsServices.getBlogsByBlogId(Integer.valueOf(blogId));
		}
		List<CommentRequestAndResp> allComments = blogsServices.getAllCommentsByBlogId(Integer.valueOf(blogId));
		System.out.println(allComments);
		model.addAttribute("blog_detail", blogByBlogId);
		model.addAttribute("comments", allComments);
		model.addAttribute("add_comment", new CommentRequestAndResp());
		return AppUtils.BLOG_DETAIL;
	}

	@GetMapping(value = "/user-index")
	public String userIndexLoad(Model model) {
		List<BlogsDataResp> blogsByUserId = blogsServices.getBlogsByUserId();
		//System.out.println(blogsByUserId);
		model.addAttribute("my_blog_list", blogsByUserId);
		return AppUtils.USER_INDEX;
	}
	
	@PostMapping(value = "/comment")
	public String addComment(CommentRequestAndResp addCommentReq, Model model) {
		System.out.println(addCommentReq);
		String addBlog = blogsServices.addCommentToBlog(addCommentReq);
		System.out.println(addBlog);
		return "redirect:getBlogById?id="+addCommentReq.getBlogId();
	}
	
	@GetMapping(value = "/comments")
	public String getAllComment(Model model) {
		 List<CommentRequestAndResp> allCommentsForUsersPost = blogsServices.getAllCommentsByUserId();
			model.addAttribute("comments", allCommentsForUsersPost);
		return AppUtils.USER_POST_COMMENTS;
	}
	
	@GetMapping(value = "/del-comment")
	public String deleteComment(@RequestParam(name = "id", required = false) String commentId, Model model) {
		System.out.println(commentId);
		blogsServices.deleteComment(Integer.valueOf(commentId));
		return "redirect:comments";
	}

}