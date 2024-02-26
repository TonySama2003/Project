package vn.myhome.service;


import org.springframework.data.domain.Page;
import vn.myhome.entity.Blog;
import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(int theId);
    void save(Blog theBlog);
    void deleteById(int theId);

    Page<Blog> findAllPage(Integer pageNo);

    Page<Blog> findBlogByTitleOrType(String theText, Integer pageNo);

    Page<Blog> findAllPageLimited(Integer pageNo);
}
