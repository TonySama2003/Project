package vn.myhome.service;

import org.springframework.data.domain.Sort;
import vn.myhome.dao.CommentRepository;
import vn.myhome.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment saveComment(Comment comment) {
        if (comment.getId() != null) {
            Optional<Comment> existingComment = commentRepository.findById(comment.getId());
            if (existingComment.isPresent()) {
                Comment updatedComment = existingComment.get();
                updatedComment.setUser(comment.getUser());
                updatedComment.setContent(comment.getContent());
                // Cập nhật các trường khác nếu cần
                return commentRepository.save(updatedComment);
            }
        }
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteById((long) commentId);
    }

    @Override
    public Comment findById(int commentId) {
        return commentRepository.findById((long) commentId).orElse(null);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Comment> findAllPage(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return commentRepository.findAll(pageable);
    }

    @Override
    public Page<Comment> findCommentsByCommentIdPaged(Long commentId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
<<<<<<< Updated upstream
    public Page<Comment> findAllByBlogId(Integer blogId, Integer pageNo) {
=======
    public Page<Comment> findAllByBlogId(Long blogId, Integer pageNo) {
>>>>>>> Stashed changes
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return commentRepository.findAllByBlogId(blogId,pageable);
    }


}
