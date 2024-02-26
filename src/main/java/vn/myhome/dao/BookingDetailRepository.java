package vn.myhome.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.myhome.entity.BookingDetail;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    BookingDetail findById(int theId);

//    @Query("SELECT bd.updateDate, SUM(bd.totalPrice)  FROM BookingDetail bd WHERE bd.status = 'Hoàn Thành' OR bd.status ='Đã thanh toán'")
//    List<Object[]> calculateMonthlyRevenue();

    @Query("SELECT FUNCTION('YEAR', bd.updateDate) AS year, FUNCTION('MONTH', bd.updateDate) AS month, SUM(bd.totalPrice) AS monthlyRevenue " +
            "FROM BookingDetail bd " +
            "WHERE bd.status = 'Finished' OR bd.status = 'Paid' " +
            "GROUP BY year, month")
    List<Object[]> calculateMonthlyRevenue();

    @Query("SELECT FUNCTION('YEAR', bd.updateDate) AS year, FUNCTION('MONTH', bd.updateDate) AS month, SUM(bd.totalPrice) AS monthlyRevenue " +
            "FROM BookingDetail bd " +
            "WHERE bd.status = 'Cancel Booking' " +
            "GROUP BY year, month")
    List<Object[]> getDataBkCancel();

    @Query("SELECT bd FROM BookingDetail bd WHERE bd.status = 'Wait for confirmation'")
    List<BookingDetail> getPendingBookingdetails();

    @Query("SELECT FUNCTION('YEAR',bd.updateDate) AS year,FUNCTION('MONTH', bd.updateDate) AS month, SUM(CASE WHEN bd.status = 'Finished' THEN 1 ELSE 0 END) AS completed_count , SUM(CASE WHEN bd.status = 'Cancel Booking' THEN 1 ELSE 0 END) AS cancelled_count, SUM(CASE WHEN bd.status IN ('Finished', 'Cancel Booking') THEN 1 ELSE 0 END) AS total_count " +
            "FROM BookingDetail bd " +
            "GROUP BY year ,month " +
            "ORDER BY year ,month ")
    List<Object[]> getNumCompletedAndCancelled();

    @Query("SELECT FUNCTION('YEAR',bd.updateDate) AS year,FUNCTION('MONTH', bd.updateDate) AS month, SUM(CASE WHEN bd.status = 'Finished' OR bd.status = 'Paid' THEN 1 ELSE 0 END) AS paided_count , SUM(CASE WHEN bd.status = 'Payment Pending' THEN 1 ELSE 0 END) AS unpaid_count , SUM(CASE WHEN bd.status = 'Cancel Booking' THEN 1 ELSE 0 END) AS cancelled_count " +
            "FROM BookingDetail bd " +
            "GROUP BY year ,month " +
            "ORDER BY year ,month ")
    List<Object[]> getDataForPieChart();


}