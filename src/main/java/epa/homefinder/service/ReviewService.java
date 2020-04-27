package epa.homefinder.service;

import epa.homefinder.dao.AdRepository;
import epa.homefinder.dao.ReviewRepository;
import epa.homefinder.dao.UserRepository;
import epa.homefinder.dto.ReviewDtoRequest;
import epa.homefinder.dto.ReviewDtoResponse;
import epa.homefinder.entity.Review;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, AdRepository adRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewDtoRequest> getAdReviews(Long adId) {
        List<Review> reviewList = reviewRepository.findAllByAdOrderByIdDesc(adRepository.findById(adId).get());
        List<ReviewDtoRequest> reviewDtoRequestList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewDtoRequest reviewDtoRequest = ReviewDtoRequest.builder().idReview(review.getId())
                    .comment(review.getComment())
                    .rating(review.getRating())
                    .like(review.getRating())
                    .username(review.getUser().getUsername())
                    .userType(review.getUser().getType())
                    .mail(review.getUser().getEmail())
                    .date(review.getDate())
                    .build();
            reviewDtoRequestList.add(reviewDtoRequest);
        }
        return reviewDtoRequestList;
    }

    public Long saveAdReview(ReviewDtoResponse reviewDtoResponse) {
        Review review = Review.builder().ad(adRepository.findById(reviewDtoResponse.getAdId()).get())
                .user(userRepository.findByEmail(reviewDtoResponse.getMail()))
                .comment(reviewDtoResponse.getComment())
                .rating(reviewDtoResponse.getRating())
                .like(reviewDtoResponse.getLike())
                .date(this.getCurrentTime())
                .build();
        Review review1 = reviewRepository.save(review);
        return review1.getId();
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.delete(reviewRepository.findById(reviewId).get());
    }

    public void updateReview(ReviewDtoResponse reviewDtoResponse) {
        Review review = reviewRepository.findById(reviewDtoResponse.getIdReview()).get();
        review.setComment(reviewDtoResponse.getComment());
        review.setRating(reviewDtoResponse.getRating());
        review.setLike(reviewDtoResponse.getLike());
        reviewRepository.save(review);
    }

    public Timestamp getCurrentTime() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }
}
