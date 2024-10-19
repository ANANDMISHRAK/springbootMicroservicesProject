package com.anand.reviewms.review.impl;



import com.anand.reviewms.review.Review;
import com.anand.reviewms.review.ReviewRepository;
import com.anand.reviewms.review.ReviewService;
import com.anand.reviewms.review.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public ReviewServiceImpl() {
    }

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


//    CompanyResponse companyResponse;
//
//    public CompanyServiceImpl(CompanyResponse companyResponse) {
//        this.companyResponse = companyResponse;
//    }

    Long nextId= 1L;

    // Start Implement Services

    @Override
    public ReviewResponse findAll() {
        List<Review> resReview=  reviewRepository.findAll();
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setData(resReview);

        return reviewResponse;
    }

    @Override
    public ReviewResponse findReviewByid(Long id) {

       Optional<Review> review = reviewRepository.findById(id);
        ReviewResponse reviewResponse = new ReviewResponse();
       if(review.isPresent()) {
           Review dbReview = review.get();
           reviewResponse.setData(dbReview);
         }
       else {
           reviewResponse.setData(null);
       }

        return reviewResponse;
    }

    @Override
    public ReviewResponse createReview(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();
        try{
            review.setId(nextId ++);
            reviewRepository.save(review) ;
            reviewResponse.setData(review);
            return reviewResponse;

        }
        catch (Exception e)
        {
            reviewResponse.setData(null);
            return reviewResponse;
        }

    }

    @Override
    public ReviewResponse updateReviewById(Long id, Review review) {
        Optional<Review> opReview = reviewRepository.findById(id);
        ReviewResponse reviewResponse = new ReviewResponse();
        if(opReview.isPresent())
        {
            Review dbReview = opReview.get();

            dbReview.setTitle(review.getTitle());
            dbReview.setDescription(review.getDescription());
            dbReview.setRaiting(review.getRaiting());


            reviewRepository.save(dbReview);
             reviewResponse.setData(dbReview);
        }
        else
        {
            reviewResponse.setData(null);
        }
        return reviewResponse;
    }

    @Override
    public ReviewResponse deleteReviewById(Long id) {
        Optional<Review> opReview = reviewRepository.findById(id);
        ReviewResponse reviewResponse = new ReviewResponse();
        if(opReview.isPresent())
        {
            Review dbReview = opReview.get();
            reviewRepository.deleteById(id);
            reviewResponse.setData(dbReview);

        }
        else {
            reviewResponse.setData(null);

        }
        return  reviewResponse;
    }
}
