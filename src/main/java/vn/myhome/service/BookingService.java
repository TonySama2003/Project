package vn.myhome.service;

import org.springframework.data.domain.Page;
import vn.myhome.entity.AppUser;
import vn.myhome.entity.Booking;

import java.util.List;

public interface BookingService {
    Booking save(Booking booking);
    List<Booking> findAll();
    Booking findById(int theId);
    List<Booking> findAllByAppUser(AppUser appUser);
    Page<Booking> findAllPage(Integer pageNo);
    Page<Booking> findAllByAppUserPage(AppUser appUser,Integer pageNo);
    Page<Booking> findAllBookingByNamePhoneBkCode(String theText,Integer pageNo);
    Page<Booking> findAllByBookingDetailStatus(Integer pageNo);
    Page<Booking> findAllByBookingPending(Integer pageNo);
}
