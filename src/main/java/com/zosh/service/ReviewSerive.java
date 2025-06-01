package com.zosh.service;

import java.util.List;

import com.zosh.Exception.ReviewException;
import com.zosh.model.Review;
import com.zosh.model.User;
import com.zosh.request.ReviewRequest;
import com.zosh.response.RatingsResponse;

public interface ReviewSerive {
	
    public Review submitReview(ReviewRequest review,User user);
    public void deleteReview(Long reviewId) throws ReviewException;
    public double calculateAverageRating(List<Review> reviews);
    public List<RatingsResponse> getAllRatings();
}
