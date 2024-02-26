package vn.myhome.dao;

import vn.myhome.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByCreatedAtBetween(Date startDate, Date endDate);
<<<<<<< Updated upstream
    @Query("select c From Comment c where c.blog.id = :blogId")
    Page<Comment> findAllByBlogId(Integer blogId, Pageable pageable);
=======
    @Query("select c From Comment c where c.blog.id = : blodId")
    Page<Comment> findAllByBlogId(Long blogId, Pageable pageable);
>>>>>>> Stashed changes
    @Query("select r from Comment r where upper(r.user) like upper(concat('%', :theText , '%')) or upper(r.content) like upper(concat('%', :theText, '%'))")
    Page<Comment> findAllByTitleContainingIgnoreCaseOrTypeContainingIgnoreCase(String theText, Pageable pageable);
}