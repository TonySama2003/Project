package vn.myhome.controller;

import vn.myhome.entity.Blog;
import vn.myhome.entity.Comment;
import vn.myhome.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.myhome.service.CommentService;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    //show blog list - user
    @GetMapping("/list")
    public String adminShowRooms(Model model ,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo){
        try{
            Page<Blog> blogList = blogService.findAllPage(pageNo);
            if (blogList != null){
                model.addAttribute("totalPage",blogList.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("blogs",blogList);
                return "userBlog/blogs.html";
            }
        }catch (Exception e){

        }
        return "userBlog/blogs.html";
    }
    //search by title and type

    //get blog by Id
    @GetMapping("/blog")
    public String getBlogById(@RequestParam(value = "blogId") int theId, Model model, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Blog theBlog = blogService.findById(theId);

<<<<<<< Updated upstream
        Page<Comment> commentList = commentService.findAllByBlogId(theId,pageNo);
=======
        Page<Comment> commentList = commentService.findAllPage(pageNo);
>>>>>>> Stashed changes
        if (commentList != null) {
            model.addAttribute("totalPage", commentList.getTotalPages());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("comments", commentList);
            model.addAttribute("blog", theBlog);
            return "userBlog/blogs-single.html";
        }
        return "userBlog/blogs-single.html";
    }

    //show blog list - admin
    @GetMapping("/admin/list")
    public String getListBlog(Model theModel,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo) {
        try {
            Page<Blog> theBlogs = blogService.findAllPageLimited(pageNo);
            if (theBlogs != null) {
                theModel.addAttribute("totalPage", theBlogs.getTotalPages());
                theModel.addAttribute("currentPage", pageNo);
                theModel.addAttribute("blogs", theBlogs);
                return "/adminBlog/list-blog.html";
            }
        }catch (Exception e){

        }
        return  "adminBlog/list-blog.html";
    }

    //add blog
    @GetMapping("/admin/showFormForAdd")
    public String showFormForAdd(Model model){
        Blog newBlog = new Blog();
        model.addAttribute("blog",newBlog);
        return "/adminBlog/blog-form.html";
    }

    @PostMapping("/admin/save")
    public String saveBlog(@ModelAttribute("blog")Blog theBlog,@RequestParam("image") MultipartFile imageFile){
        Blog check = new Blog();
        check = theBlog;
        if (!imageFile.isEmpty()) {
            try {
                addImg(theBlog,imageFile);
                theBlog.setBlogCreatedDate(LocalDate.now());
                blogService.save(theBlog);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        theBlog.setBlogCreatedDate(LocalDate.now());
        blogService.save(theBlog);
        return "redirect:/blogs/admin/list";
    }
    //update blog
    @GetMapping("/admin/showFormForUpdate")
    public String showFormForAdd(@RequestParam("blogId") int theId, Model theModel){
        //get room from service
        Blog theBlog = blogService.findById(theId);
        theModel.addAttribute("blog",theBlog);
        return "/adminBlog/blog-form-update.html";
    }
    @PostMapping("/admin/update")
    public String updateBlog(@ModelAttribute("blog")Blog theBlog,@RequestParam("image") MultipartFile imageFile){
        if(!imageFile.isEmpty()) {
            try {
                addImg(theBlog,imageFile);
                theBlog.setBlogCreatedDate(LocalDate.now());
                blogService.save(theBlog);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        theBlog.setBlogCreatedDate(LocalDate.now());
        blogService.save(theBlog);
        return "redirect:/blogs/admin/list";
    }
    //delete blog
    @GetMapping("/admin/delete")
    public String delete(@RequestParam("blogId")int theId){
        blogService.deleteById(theId);
        return "redirect:/blogs/admin/list";
    }

    @RequestMapping ("/searchsBlog")
    public String searchRoomByTitleOrType(Model model,@RequestParam(name = "blogid")Integer blogid,@RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,@RequestParam("searchText") String searchText){
        try {
            Page<Blog> blogList = blogService.findBlogByTitleOrType(searchText,pageNo);
            if (!blogList.isEmpty()){
                model.addAttribute("totalPage",blogList.getTotalPages());
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("searchText",searchText);
                model.addAttribute("blogs",blogList);
            }else {
                Blog blog=blogService.findById(blogid);
                model.addAttribute("blog",blog);
                model.addAttribute("currentPage",pageNo);
                model.addAttribute("successMessage", "Khong tim thay noi dung !");

                return "userBlog/blogs-single.html";

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "userBlog/blogs.html";


    }
    public Blog addImg(Blog theBlog,MultipartFile imageFile) throws IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = theBlog.getBlogTitlel().replaceAll("\\s+","_")+ UUID.randomUUID()+fileExtension;
        String staticDirectory = "src/main/resources/static";
        String imageDirectory = "/images/blogImg/";
        Path imageFilePath = Paths.get(staticDirectory + imageDirectory + fileName);
        Files.copy(imageFile.getInputStream(),imageFilePath);
        theBlog.setBlogImg(fileName);
        return theBlog;
    }
    
}