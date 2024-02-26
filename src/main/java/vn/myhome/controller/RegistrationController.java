package vn.myhome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.myhome.dto.RegistrationForm;
import vn.myhome.entity.AppUser;
import vn.myhome.entity.Role;
import vn.myhome.entity.UserRole;
import vn.myhome.service.RoleService;
import vn.myhome.service.UserRoleService;
import vn.myhome.service.UserService;
import vn.myhome.service.UserServiceImpl;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationForm") @Valid RegistrationForm registrationForm,
                               BindingResult bindingResult, Model model) {
        if (!registrationForm.password.equals(registrationForm.confirmPassword)){
            bindingResult.rejectValue("password", "error.registrationForm", "Password and confirm pasword not match !");
            return "registration";
        }
        // Kiểm tra và xử lý dữ liệu đăng ký ở đây
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "registration"; // Nếu có lỗi, hiển thị lại form đăng ký với thông báo lỗi
        }
        AppUser existingUser = userService.findByEmail(registrationForm.getEmail());
        if (existingUser != null) {
            // Người dùng đã tồn tại, bạn có thể thực hiện xử lý tùy chọn ở đây
            // Ví dụ: hiển thị thông báo lỗi cho người dùng
            bindingResult.rejectValue("email", "error.registrationForm", "Email Existed");
            return "registration";
        }
        // Tạo một đối tượng AppUser mới và gán thông tin từ RegistrationForm
        AppUser newUser = new AppUser();
        newUser.setEmail(registrationForm.getEmail());
        newUser.setFirstName(registrationForm.getFirstName());
        newUser.setAddress(registrationForm.getAddress());
        newUser.setLastName(registrationForm.getLastName());
        newUser.setPhoneNumber(registrationForm.getPhoneNumber());
        newUser.setDateOfBrith(registrationForm.getDateOfBirth());
        newUser.setEncrytedPassword(passwordEncoder.encode(registrationForm.getPassword()));
        newUser.setCreateDate(new Date());
        newUser.setUpdateDate(new Date());
        newUser.setEnabled(true); // Bật tài khoản sau khi đăng ký

        // Tạo hoặc lấy vai trò "USER" từ cơ sở dữ liệu
        Role userRole = roleService.findByRoleName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setRoleName("ROLE_USER");
            roleService.save(userRole); // Lưu vai trò "USER" vào cơ sở dữ liệu nếu chưa tồn tại
        }
        // Tạo một đối tượng UserRole và gán AppUser và Role cho nó
        UserRole userRoleMapping = new UserRole();
        userRoleMapping.setUser(newUser);
        userRoleMapping.setRole(userRole);

        // Lưu AppUser và UserRole vào cơ sở dữ liệu
        userService.save(newUser);
        userRoleService.save(userRoleMapping);
        // Thực hiện việc lưu thông tin đăng ký vào cơ sở dữ liệu ở đây


        return "redirect:/login"; // Sau khi đăng ký thành công, chuyển hướng đến trang đăng nhập
    }
}