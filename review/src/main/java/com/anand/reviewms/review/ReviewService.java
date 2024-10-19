package com.anand.reviewms.review;



import com.anand.reviewms.review.response.ReviewResponse;


public interface ReviewService {
    ReviewResponse findAll();

    ReviewResponse findReviewByid(Long id);

    ReviewResponse createReview(Review review);

    ReviewResponse updateReviewById(Long id, Review review);

    ReviewResponse deleteReviewById(Long id);
}
