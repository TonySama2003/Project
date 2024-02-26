package vn.myhome.service;

import org.springframework.data.domain.Page;
import vn.myhome.entity.Comment;
import vn.myhome.entity.Rating;
import java.util.Date;
import java.util.List;

public interface RatingService {


    Rating saveRating(Rating rating);

    List<Rating> getAllRatings();
    Rating getRatingById(Long id);
    void deleteRatingById(Long id);
    void deleteRating(int commentId);

    Rating findById(int commentId);

    Rating findRatingById(Long id);

    List<Rating> findRatingsByCreatedAtBetween(Date startDate, Date endDate);
    Page<Rating> findAllPage(Integer pageNo);
    Page<Rating> findRatingsByRatingIdPaged(Long ratingId, Integer pageNo, Integer pageSize);
    Page<Rating> findAllByRoomId(Long ratingId,Integer pageNo);
}