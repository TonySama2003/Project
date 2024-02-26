package vn.myhome.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "USER_UK", columnNames = "Email")
)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Email",length = 250,nullable = false)
    private String email;

    @Column(name = "Encryted_Password", length = 250, nullable = false)
    private String encrytedPassword;

    @Column(name = "Enabled", length = 1, nullable = false)
    private boolean enabled;

    @Column(name = "First_Name",length = 100)
    @NotEmpty(message = "Fill in required !")
    private String firstName;

    @Column(name = "Last_name",length = 150)
    @NotEmpty(message = "Fill in required !")
    private String lastName;

    @Column(name = "Phone_Number",length = 15)
    @NotNull(message = "Fill in required !")
    @Digits(integer = 15 , fraction = 0,message = "Invalid Phone Number !")
    private Integer phoneNumber;

    @Column(name = "Address",length = 250)
    @NotEmpty(message = "Fill in required !")
    private String address;

    @Column(name = "DateOfBrith")
    private Date dateOfBrith;

    @Column(name = "Create_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Update_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "User_img")
    private String userImg;

    @OneToMany(mappedBy = "appUser")
    private List<Booking> bookings;
    public AppUser() {
    }

    public AppUser(int id, String email, String encrytedPassword, boolean enabled, int phoneNumber, String address, Date dateOfBrith, Date createDate, Date updateDate) {
        this.id = id;
        this.email = email;
        this.encrytedPassword = encrytedPassword;
        this.enabled = enabled;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBrith = dateOfBrith;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncrytedPassword() {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPhoneNumber() {
        if (phoneNumber == null) {
            return 0;
        }
        // Xử lý trường hợp khi phoneNumber là null
        return phoneNumber;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBrith() {
        return dateOfBrith;
    }

    public void setDateOfBrith(Date dateOfBrith) {
        this.dateOfBrith = dateOfBrith;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return lastName+" "+firstName;
    }
}
