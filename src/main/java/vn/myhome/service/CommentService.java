package vn.myhome.service;


import org.springframework.data.domain.Page;
import vn.myhome.entity.Comment;
import vn.myhome.entity.Room;

import java.util.List;

public interface CommentService {
    Comment saveComment(Comment comment);
    List<Comment> getAllComments();
    Comment getCommentById(Long id);
    void deleteCommentById(Long id);
    void deleteComment(int commentId);

    Comment findById(int commentId);

    Comment findCommentById(Long id);

    Page<Comment> findAllPage(Integer pageNo);
    Page<Comment> findCommentsByCommentIdPaged(Long commentId, Integer pageNo, Integer pageSize);
<<<<<<< Updated upstream

    Page<Comment> findAllByBlogId(Integer blogId, Integer pageNo);
=======
    Page<Comment> findAllByBlogId(Long blogId,Integer pageNo);

>>>>>>> Stashed changes
}


