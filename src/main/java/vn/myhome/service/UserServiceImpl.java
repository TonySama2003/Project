package vn.myhome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.myhome.dao.UserRepository;
import vn.myhome.dao.UserRoleRepository;
import vn.myhome.dto.RegistrationForm;
import vn.myhome.dto.UserDto;
import vn.myhome.entity.AppUser;
import vn.myhome.entity.Role;
import vn.myhome.entity.UserRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public AppUser findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void registerUser(RegistrationForm registrationForm) {
        AppUser user = new AppUser();
        user.setEmail(registrationForm.getEmail());
        user.setEncrytedPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setAddress(registrationForm.getAddress());
        user.setDateOfBrith(registrationForm.getDateOfBirth());
        user.setFirstName(registrationForm.getFirstName());
        user.setLastName(registrationForm.getLastName());
        user.setEnabled(true);
        userRepository.save(user);
    }
    @Override
    public void save(AppUser theUser){
        userRepository.save(theUser);
    }

    @Override
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }
    @Override
    public List<UserDto> getAllUsersWithRoles() {
        List<UserDto> userDtos = new ArrayList<>();

        List<Object[]> usersWithRoles = userRepository.findAllWithRoles();
        boolean check;
        for (Object[] result : usersWithRoles) {
            UserDto userDtoNew = new UserDto();

            // Thêm thông tin người dùng
            userDtoNew.setId((int) result[0]);
            userDtoNew.setFirstName((String) result[1]);
            userDtoNew.setLastName((String) result[2]);
            userDtoNew.setEmail((String) result[3]);
            userDtoNew.setPhoneNumber((int) result[4]);
            userDtoNew.setAddress((String) result[5]);
            userDtoNew.setDateOfBirth((Date) result[6]);
            userDtoNew.setUpdateDate((Date) result[7]);
            userDtoNew.setCreateDate((Date) result[8]);
            userDtoNew.setEnabled((boolean) result[9]);

            // Tạo danh sách vai trò và thêm vào userDto
            List<Role> roles = new ArrayList<>();
            roles.add((Role) result[10]);
            userDtoNew.setRoleList(roles);
            check = true;
            if (userDtos != null){
                for (UserDto userDto : userDtos){//kiểm tra nếu id đã tồn tại thì thêm role vào rolelist
                    if (userDto.getId() == userDtoNew.getId()){
                        userDto.getRoleList().add((Role) result[10]);
                        check=false;
                        continue;
                    }
                }
            }
            if (check){
                userDtos.add(userDtoNew);
            }
            // Thêm userDto vào danh sách userDtos

        }
        return userDtos;
    }

    @Override
    public List<UserDto> findAllWithRolesById(int theId) {
        return null;
    }

    @Override
    public AppUser findById(int theId) {
        return userRepository.findById(theId);
    }

    @Override
    public Page<AppUser> findAllPage(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,10);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<AppUser> searchUserByEmailOrPhone(String theText, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,10);
        return userRepository.findAllByEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(theText,pageable);
    }

    @Override
    public void updateProfile(AppUser user) {
        AppUser userold = userRepository.findById(user.getId());

        if (!user.getFirstName().isEmpty()){
            userold.setFirstName(user.getFirstName());
        }

        if (!user.getLastName().isEmpty()){
            userold.setLastName(user.getLastName());
        }

        if (user.getPhoneNumber() != 0) {
            userold.setPhoneNumber(user.getPhoneNumber());
        }
        if (!user.getAddress().isEmpty()){
            userold.setAddress(user.getAddress());
        }

        if (user.getUserImg() != null){
            userold.setUserImg(user.getUserImg());
        }
        System.out.println(userold);
        userRepository.save(userold);
    }


}
