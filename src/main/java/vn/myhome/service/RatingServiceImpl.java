package vn.myhome.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.myhome.dao.RatingRepository;
import vn.myhome.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating saveRating(Rating rating) {
        if (rating.getId() != null) {
            Optional<Rating> existingRating = ratingRepository.findById(rating.getId());
            if (existingRating.isPresent()) {
                Rating updatedRating = existingRating.get();
                updatedRating.setComment(rating.getComment());
                updatedRating.setEvaluationScore(rating.getEvaluationScore());
                return ratingRepository.save(updatedRating);
            }
        }
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRatingById(Long id) {
        ratingRepository.deleteById(id);
    }

    @Override
    public void deleteRating(int ratingId) {
        ratingRepository.deleteById((long) ratingId);
    }

    @Override
    public Rating findById(int commentId) {
        return ratingRepository.findById((long) commentId).orElse(null);
    }

    @Override
    public Rating findRatingById(Long id) {
        return ratingRepository.findById(id).orElse(null);
    }

    @Override
    public List<Rating> findRatingsByCreatedAtBetween(Date startDate, Date endDate) {
        List<Rating> ratings = ratingRepository.findRatingsByCreatedAtBetween(startDate, endDate);
        return ratings;
    }

    @Override
    public Page<Rating> findAllPage(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return ratingRepository.findAll(pageable);
    }

    @Override
    public Page<Rating> findRatingsByRatingIdPaged(Long ratingId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public Page<Rating> findAllByRoomId(Long roomId, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(pageNo-1,5,sort);
        return ratingRepository.findAllByRoomId(roomId,pageable);
    }


}

