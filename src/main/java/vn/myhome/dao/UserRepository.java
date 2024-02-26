package vn.myhome.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.myhome.dto.UserDto;
import vn.myhome.entity.AppUser;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findUserByEmail(String userName);
    //lay danh sach user va role
    @Query("SELECT u.id, u.firstName, u.lastName, u.email, u.phoneNumber, u.address, u.dateOfBrith, u.updateDate,u.createDate,u.enabled, ur.role FROM AppUser u JOIN UserRole ur ON u.id = ur.appUser.id")
    List<Object[]> findAllWithRoles();

    @Query("SELECT u.id, u.firstName, u.lastName, u.email, u.phoneNumber, u.address, u.dateOfBrith, u.updateDate,u.createDate,u.enabled, ur.role FROM AppUser u JOIN UserRole ur ON u.id = ur.appUser.id WHERE u.id = ?1")
    List<Object[]> findAllWithRolesById(int theId);

    AppUser findById(int theId);

    @Query("select a from AppUser a where upper(a.email) like upper(concat('%', :theText, '%')) or upper(a.phoneNumber) like upper(concat('%', :theText, '%'))")
    Page<AppUser> findAllByEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(String theText, Pageable pageable);



}