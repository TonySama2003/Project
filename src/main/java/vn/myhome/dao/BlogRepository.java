package vn.myhome.dao;

import vn.myhome.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer>{
    List<Blog> findAll();
    @Query("SELECT b FROM Blog b LEFT JOIN FETCH b.comments WHERE b.id = :blogId")
    Optional<Blog> findByIdWithComments(@Param("blogId") Long blogId);

    @Query("select r from Blog r where upper(r.blogTitlel) like upper(concat('%', :theText , '%')) or upper(r.blogAuthor) like upper(concat('%', :theText, '%'))")
    Page<Blog> findAllByTitleContainingIgnoreCaseOrTypeContainingIgnoreCase(String theText, Pageable pageable);

}