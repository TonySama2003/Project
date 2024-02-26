package vn.myhome.service;

import org.springframework.data.domain.Page;
import vn.myhome.dto.RegistrationForm;
import vn.myhome.dto.UserDto;
import vn.myhome.entity.AppUser;

import java.util.List;

public interface UserService {
    AppUser findByEmail(String email);

    void registerUser(RegistrationForm registrationForm);
    void save(AppUser user);

    List<AppUser> findAll();
    List<UserDto> getAllUsersWithRoles();
    List<UserDto> findAllWithRolesById(int theId);
    AppUser findById(int theId);
    Page<AppUser> findAllPage(Integer pageNo);

    Page<AppUser> searchUserByEmailOrPhone(String theText,Integer pageNo);
    void updateProfile(AppUser user);
}
