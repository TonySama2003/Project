package vn.myhome.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_king")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "titlel")
    private  String blogTitlel;

    @Column(name = "content")
    private String blogContent;

    @Column(name = "blog_img")
    private String blogImg;


    @Column(name = "createdDate")
    private LocalDate blogCreatedDate;

    @Column(name = "author")
    private String blogAuthor;

    public Blog() {
        this.comments = new ArrayList<>();
    }
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments =  new ArrayList<>();

    @OrderBy("createdAt DESC")
    public List<Comment> getComments() {
        return comments;
    }

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    public Blog(int id, String blogTitlel, String blogContent, String blogImg, LocalDate blogCreatedDate, String blogAuthor, List<Comment> comments) {
        this.id = id;
        this.blogTitlel = blogTitlel;
        this.blogContent = blogContent;
        this.blogImg = blogImg;
        this.blogCreatedDate = blogCreatedDate;
        this.blogAuthor = blogAuthor;
        this.comments = comments;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlogTitlel() {
        return blogTitlel;
    }

    public void setBlogTitlel(String blogTitlel) {
        this.blogTitlel = blogTitlel;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogImg() {
        return blogImg;
    }

    public void setBlogImg(String blogImg) {
        this.blogImg = blogImg;
    }

    public LocalDate getBlogCreatedDate() {
        return blogCreatedDate;
    }

    public void setBlogCreatedDate(LocalDate blogCreatedDate) {
        this.blogCreatedDate = blogCreatedDate;
    }

    public String getBlogAuthor() {
        return blogAuthor;
    }

    public void setBlogAuthor(String blogAuthor) {
        this.blogAuthor = blogAuthor;
    }

    public void addComment( Comment comment) {
        comments.add(comment);
    }
}
