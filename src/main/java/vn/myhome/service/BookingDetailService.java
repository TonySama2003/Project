package vn.myhome.service;

import vn.myhome.dto.DataBookingCancelDTO;
import vn.myhome.dto.DataForPieChartDTO;
import vn.myhome.dto.RevenueDataDTO;
import vn.myhome.entity.BookingDetail;

import java.util.List;

public interface BookingDetailService {
    BookingDetail save(BookingDetail bookingDetail);
    public void updateStatus(int bookingDetailId, String status);
    BookingDetail findById(int theId);
    List<RevenueDataDTO> getRevenue();
    List<BookingDetail> getPendingBookingdetails();
    List<RevenueDataDTO> getNumCompletedAndCancelled();
    List<DataForPieChartDTO> getDataForPieChart();
    List<DataBookingCancelDTO> getDataBkCancel();
}
