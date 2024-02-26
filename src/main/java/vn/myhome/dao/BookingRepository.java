package vn.myhome.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.myhome.entity.AppUser;
import vn.myhome.entity.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking findById(int theId);
    List<Booking> findAllByAppUser(AppUser appUser);
    Page<Booking> findAllByAppUser(AppUser appUser, Pageable pageable);

    @Query("select b from Booking b where upper(b.appUser.firstName) like upper(concat('%', :theText, '%')) or upper(b.appUser.phoneNumber) like upper(concat('%', :theText, '%')) or upper(b.bookingCode) like upper(concat('%', :theText, '%'))")
    Page<Booking> findAllByAppUserFirstNameContainingIgnoreCaseOrAppUserPhoneNumberContainingIgnoreCaseOrBookingCodeContainingIgnoreCase(String theText, Pageable pageable);
    @Query("SELECT b from Booking b join BookingDetail bd on b.id = bd.booking.id Where bd.status = 'Cancel Booking Pending'")
    Page<Booking> findAllByBookingDetailStatus(Pageable pageable);
    @Query("SELECT b from Booking b join BookingDetail bd on b.id = bd.booking.id Where bd.status = 'Wait for confirmation'")
    Page<Booking> findAllByBookingPending(Pageable pageable);
}