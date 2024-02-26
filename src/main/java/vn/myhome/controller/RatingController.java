package vn.myhome.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import vn.myhome.entity.*;
import vn.myhome.service.RatingService;
import vn.myhome.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.myhome.service.UserService;


@Controller
@RequestMapping("/ratings")
public class RatingController {

    private final RoomService roomService;
    private final RatingService ratingService;
    @Autowired
    private UserService userService;
    @Autowired
    public RatingController(RoomService roomService, RatingService ratingService) {
        this.roomService = roomService;

        this.ratingService = ratingService;
    }
    @GetMapping("/list-comment")
    public String getAllComments(Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        try {
            Page<Rating> commentList = ratingService.findAllPage(pageNo);
            if (commentList != null) {
                model.addAttribute("totalPage", commentList.getTotalPages());
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("comments", commentList);
                return "userRoom/user-room-single.html";
            }
        } catch (Exception e) {
            // Handle the exception if needed
        }
        return "userRoom/user-room-single.html";
    }
    @PostMapping("/add")
    public String addComment(@RequestParam("roomId") int roomId, @RequestParam("comment") String comment,@RequestParam("evaluationScore") int evaluationScore ,Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Retrieve the user information from the User table
            AppUser user = userService.findByEmail(userDetails.getUsername());
            if (user != null) {
                // Create a Comment object from the data sent from the form
                Room room = roomService.findById(roomId);
                Rating rating = new Rating(room, user, comment, evaluationScore);
                // Add the new comment to the beginning of the list in Blog
                room.addRating(rating);
                // Save the comment to the database using CommentService (if needed)
                ratingService.saveRating(rating);
                // Redirect the user to the blog details page after submitting the comment
                return "redirect:/roomSingle?roomId=" + roomId;
            } else {
                // Handle the case where the user with the provided userId doesn't exist
                return "redirect:/roomSingle?roomId=" + roomId ; // You can define an error page URL
            }
        } else {
            // Handle the case where there's no authenticated user
            return "redirect:/login"; // You can define a login page URL
        }
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Rating rating = ratingService.findRatingById(id);
        model.addAttribute("rating", rating);
        return "edit-rating";
    }

    @PostMapping("/edit/{id}")
    public String editRating(@PathVariable Long id,
                             @ModelAttribute Rating updatedRating,
                             @RequestParam("roomId") Long roomId) {
        Rating existingRating = ratingService.findRatingById(id);
        existingRating.setComment(updatedRating.getComment());
        existingRating.setEvaluationScore(updatedRating.getEvaluationScore());


        // Update the comment in the database
        ratingService.saveRating(existingRating);

        // Redirect user to the blog details page after editing the comment
        return "redirect:/roomSingle?roomId=" + roomId;
    }

    @PostMapping("/delete/{id}")
    public String deleteRating(@PathVariable Long id, @RequestParam("roomId") Long roomId) {
        ratingService.deleteRatingById(id);
        // Chuyển hướng người dùng về trang chi tiết blog sau khi xóa comment
        return "redirect:/roomSingle?roomId=" + roomId;
    }

}


