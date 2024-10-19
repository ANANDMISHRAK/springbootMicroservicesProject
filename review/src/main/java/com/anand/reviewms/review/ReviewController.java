package com.anand.reviewms.review;



import com.anand.reviewms.review.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    // Start to write Controller

    //GetAll Job
    @GetMapping
    public ResponseEntity<ReviewResponse> getAllReview() {
        ReviewResponse review = reviewService.findAll();


        if (review.getData() != null) {
            review.setMsg("Successfully retrieved all Review");
            return ResponseEntity.ok(review);
        } else {
            review.setMsg("No Review found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(review);
        }

    }

    // Get Company By Id
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getJobById(@PathVariable Long id)
    {
        ReviewResponse review = reviewService.findReviewByid(id);



        if (review.getData() != null) {
            review.setMsg("Successfully retrieved Review with id " + id);
            return ResponseEntity.ok(review);
        } else {
            review.setMsg("No review exist with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(review);
        }
    }

    // Create company
    @PostMapping()
    public ResponseEntity<ReviewResponse> createReview(@RequestBody Review review)
    {
        ReviewResponse savedReview= reviewService.createReview(review);


        if(savedReview.getData() != null)
        {
            savedReview.setMsg("sucessfully created review ");
            return ResponseEntity.ok(savedReview);
        }
        else
        {
            savedReview.setMsg("Failed to create review : ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(savedReview);
        }
    }

   // Update company by id
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReviewById(@PathVariable Long id, @RequestBody Review review)
    {
        ReviewResponse resReview = reviewService.updateReviewById(id, review);


        if(resReview.getData() != null)
        {
         resReview.setMsg("Sucessfully Updated Review which id "+ id);
         return ResponseEntity.ok(resReview);
        }
        else
        {
            resReview.setMsg("Not Updated Review");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resReview);
        }

    }


    // Delete company by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponse> deleteReviewById(@PathVariable Long id)
    {
        ReviewResponse review = reviewService.deleteReviewById(id);

       if(review.getData() != null)
       {
           review.setMsg("Review deleted Successfully");
           return  ResponseEntity.ok(review);
       }
       else {
           review.setMsg("Review  does not exist with id "+ id);
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(review);
       }
    }

}
