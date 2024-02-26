package vn.myhome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.myhome.dao.BookingRepository;
import vn.myhome.entity.AppUser;
import vn.myhome.entity.Booking;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{
    private BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository theBookingRepository){
        this.bookingRepository = theBookingRepository;
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findById(int theId) {
        return bookingRepository.findById(theId);
    }

    @Override
    public List<Booking> findAllByAppUser(AppUser appUser) {
        return bookingRepository.findAllByAppUser(appUser);
    }

    @Override
    public Page<Booking> findAllPage(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
        Pageable pageable = PageRequest.of(pageNo-1,10,sort);

        return this.bookingRepository.findAll(pageable);
    }

    @Override
    public Page<Booking> findAllByAppUserPage(AppUser appUser, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return this.bookingRepository.findAllByAppUser(appUser,pageable);
    }

    @Override
    public Page<Booking> findAllBookingByNamePhoneBkCode(String theText, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return this.bookingRepository.findAllByAppUserFirstNameContainingIgnoreCaseOrAppUserPhoneNumberContainingIgnoreCaseOrBookingCodeContainingIgnoreCase(theText,pageable);
    }

    @Override
    public Page<Booking> findAllByBookingDetailStatus(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return bookingRepository.findAllByBookingDetailStatus(pageable);
    }

    @Override
    public Page<Booking> findAllByBookingPending(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createDate");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return bookingRepository.findAllByBookingPending(pageable);
    }
}
