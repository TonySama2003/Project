package vn.myhome.service;

import vn.myhome.dao.BlogRepository;

import vn.myhome.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import  java.util.List;
import  java.util.Optional;
@Service
public class BlogServiceImpl implements BlogService{
    private BlogRepository blogRepository ;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository){

        this.blogRepository = blogRepository;

    }

    @Override
    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Blog findById(int theId) {
        Optional<Blog> result = blogRepository.findById(theId);
        Blog theBlog = null;
        if (result.isPresent()) {
            theBlog = result.get();

        } else {
            throw new RuntimeException("Did not find the Blog id - " + theId);
        }
        return theBlog;
    }

    @Override
    public void save(Blog theBlog) {
        blogRepository.save(theBlog);
    }

    @Override
    public void deleteById(int theId) {
        blogRepository.deleteById(theId);
    }


    @Override
    public Page<Blog> findAllPage(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,12);
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> findBlogByTitleOrType(String theText, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,10);
        return blogRepository.findAllByTitleContainingIgnoreCaseOrTypeContainingIgnoreCase(theText,pageable);
    }

    @Override
    public Page<Blog> findAllPageLimited(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,6);
        return blogRepository.findAll(pageable);
    }
}
