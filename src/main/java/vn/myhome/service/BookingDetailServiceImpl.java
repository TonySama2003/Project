package vn.myhome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.myhome.dao.BookingDetailRepository;
import vn.myhome.dao.BookingRepository;
import vn.myhome.dto.DataBookingCancelDTO;
import vn.myhome.dto.DataForPieChartDTO;
import vn.myhome.dto.RevenueDataDTO;
import vn.myhome.entity.Booking;
import vn.myhome.entity.BookingDetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookingDetailServiceImpl implements BookingDetailService{
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    public BookingDetailServiceImpl(BookingDetailRepository bookingDetailRepository){
        this.bookingDetailRepository = bookingDetailRepository;
    }
    @Override
    public BookingDetail save(BookingDetail bookingDetail) {
        return bookingDetailRepository.save(bookingDetail);
    }
    @Override
    public void updateStatus(int bookingDetailId, String status) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId);

        if (bookingDetail != null) {
            bookingDetail.setStatus(status);
            bookingDetail.setUpdateDate(new Date());
            bookingDetailRepository.save(bookingDetail);
        }
    }

    @Override
    public BookingDetail findById(int theId) {
        return bookingDetailRepository.findById(theId);
    }

    @Override
    public List<RevenueDataDTO> getRevenue() {
        //tạo list rỗng nhận dữ liệu
        List<RevenueDataDTO> revenueDataDTOList = new ArrayList<>();
        //lấy dữ liệu từ repository
        List<Object[]> results = bookingDetailRepository.calculateMonthlyRevenue();
        //gán dữ liệu vào list
        for (Object[] result : results){
            revenueDataDTOList.add(new RevenueDataDTO((int)result[0],(int)result[1],(double)result[2]));
        }
        System.out.println(revenueDataDTOList);
        return revenueDataDTOList;
    }

    @Override
    public List<BookingDetail> getPendingBookingdetails() {
        return bookingDetailRepository.getPendingBookingdetails();
    }

    @Override
    public List<RevenueDataDTO> getNumCompletedAndCancelled() {
        List<RevenueDataDTO> revenueDataDTOList = new ArrayList<>();
        //lay ket qua
        List<Object[]> results = bookingDetailRepository.getNumCompletedAndCancelled();

        for (Object[] result : results){
            revenueDataDTOList.add(new RevenueDataDTO((int)result[0],(int)result[1],(long)result[2],(long)result[3],(long)result[4]));
        }
        return revenueDataDTOList;
    }

    @Override
    public List<DataForPieChartDTO> getDataForPieChart() {
        List<DataForPieChartDTO> dataForPieChartDTOList = new ArrayList<>();
        //lấy kết quả
        List<Object[]> results = bookingDetailRepository.getDataForPieChart();

        for (Object[] result : results){
            dataForPieChartDTOList.add(new DataForPieChartDTO((int)result[0],(int)result[1],(long)result[2],(long)result[3],(long)result[4]));
        }

        return dataForPieChartDTOList;
    }

    @Override
    public List<DataBookingCancelDTO> getDataBkCancel() {
        List<DataBookingCancelDTO> dataBookingCancelDTOList = new ArrayList<>();
        //lấy dữ liệu từ repository
        List<Object[]> results = bookingDetailRepository.getDataBkCancel();
        //gán dữ liệu vào list
        for (Object[] result : results){
            dataBookingCancelDTOList.add(new DataBookingCancelDTO((int)result[0],(int)result[1],(double)result[2]));
        }
        return dataBookingCancelDTOList;
    }

}
