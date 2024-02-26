package vn.myhome.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.myhome.entity.Comment;
import vn.myhome.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findRatingsByCreatedAtBetween(Date startDate, Date endDate);
    @Query("select c From Rating c where c.room.id = : blodId")
    Page<Rating> findAllByRoomId(Long roomId, Pageable pageable);
    @Query("select r from Rating r where upper(r.user) like upper(concat('%', :theText , '%')) or upper(r.comment) like upper(concat('%', :theText, '%'))")
    Page<Rating> findAllByTitleContainingIgnoreCaseOrTypeContainingIgnoreCase(String theText, Pageable pageable);

    //Thêm các phương thức truy xuất tùy chỉnh dựa trên nhu cầu của bạn
}

