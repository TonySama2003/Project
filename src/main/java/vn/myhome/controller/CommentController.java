package vn.myhome.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import vn.myhome.entity.AppUser;
import vn.myhome.service.UserService;
import vn.myhome.entity.Blog;
import vn.myhome.entity.Comment;
import vn.myhome.service.BlogService;
import vn.myhome.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    public CommentController(UserService userService, CommentService commentService, BlogService blogService, UserService userService1) {
        this.commentService = commentService;
        this.blogService = blogService;
        this.userService = userService1;
    }

    @GetMapping("/list-comment")
    public String getAllComments(Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        try {
            Page<Comment> commentList = commentService.findAllPage(pageNo);
            if (commentList != null) {
                model.addAttribute("totalPage", commentList.getTotalPages());
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("comments", commentList);
                return "userBlog/blogs-single.html";
            }
        } catch (Exception e) {
            // Handle the exception if needed
        }
        return "userBlog/blogs-single.html";
    }
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    @PostMapping("/add")
    public String addComment(@RequestParam("blogId") int blogId, @RequestParam("content") String content, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Retrieve the user information from the User table
            AppUser user = userService.findByEmail(userDetails.getUsername());
            if (user != null) {
                // Create a Comment object from the data sent from the form
                Blog blog = blogService.findById(blogId);
                Comment comment = new Comment(blog, user, content);
                // Add the new comment to the beginning of the list in Blog
                blog.addComment(comment);
                // Save the comment to the database using CommentService (if needed)
                commentService.saveComment(comment);
                // Redirect the user to the blog details page after submitting the comment
                return "redirect:/blogs/blog/?blogId=" + blogId;
            } else {
                // Handle the case where the user with the provided userId doesn't exist
                return "redirect:/error"; // You can define an error page URL
            }
        } else {
            // Handle the case where there's no authenticated user
            return "redirect:/login"; // You can define a login page URL
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Comment comment = commentService.findCommentById(id);
        model.addAttribute("comment", comment);
        return "userBlog/edit-comment";
    }

    @PostMapping("/edit/{id}")
    public String editComment(@PathVariable Long id, @ModelAttribute Comment updatedComment, @RequestParam("blogId") Long blogId) {
        Comment existingComment = commentService.findCommentById(id);
        existingComment.setContent(updatedComment.getContent());

        // Update the comment in the database
        commentService.saveComment(existingComment);

        // Redirect user to the blog details page after editing the comment
        return "redirect:/blogs/blog/?blogId=" + blogId;
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam("blogId") Long blogId) {
        commentService.deleteCommentById(id);
        // Redirect the user to the blog details page after deleting the comment
        return "redirect:/blogs/blog/?blogId=" + blogId;
    }
}
