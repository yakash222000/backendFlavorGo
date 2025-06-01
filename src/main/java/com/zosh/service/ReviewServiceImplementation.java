package com.zosh.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zosh.model.Food;
import com.zosh.repository.FoodRepository;
import com.zosh.response.RatingsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.Exception.ReviewException;
import com.zosh.model.Restaurant;
import com.zosh.model.Review;
import com.zosh.model.User;
import com.zosh.repository.RestaurantRepository;
import com.zosh.repository.ReviewRepository;
import com.zosh.request.ReviewRequest;
@Service
public class ReviewServiceImplementation implements ReviewSerive {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FoodRepository foodRepository;

   @Override
    public Review submitReview(ReviewRequest reviewRequest, User user) {
        Review review = new Review();
        System.out.println(reviewRequest);
        
        System.out.println(reviewRequest.getFoodId());
         Optional<Food> food = foodRepository.findById(reviewRequest.getFoodId());
         if(food.isPresent()) {
        	 review.setFood(food.get());
         }
        review.setCustomer(user);
        review.setMessage(reviewRequest.getReviewText());
        review.setRating(reviewRequest.getRating());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    
    @Override
    public void deleteReview(Long reviewId) throws ReviewException {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent()) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new ReviewException("Review with ID " + reviewId + " not found");
        }
    }

    @Override
    public double calculateAverageRating(List<Review> reviews) {
    	 double totalRating = 0;

         for (Review review : reviews) {
             totalRating += review.getRating();
         }

         if (reviews.size() > 0) {
             return totalRating / reviews.size();
         } else {
             return 0;
         }
    }
    @Override
    public List<RatingsResponse> getAllRatings() {
       List<Review> reviews = reviewRepository.findAll();
       List<RatingsResponse> ratingsResponses = new ArrayList<RatingsResponse>();
       for (Review review : reviews) {
           RatingsResponse ratingsResponse = new RatingsResponse();
           ratingsResponse.foodId = review.getFood().getId();
           ratingsResponse.rating = review.getRating();
           ratingsResponse.userId = review.getCustomer().getId();
           ratingsResponses.add(ratingsResponse);
       }
       return ratingsResponses;
    }
}

