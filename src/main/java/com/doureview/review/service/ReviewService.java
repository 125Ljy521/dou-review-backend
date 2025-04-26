package com.doureview.review.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.review.entity.Review;

import java.util.List;

/**
 * 影评模块业务层接口
 */
public interface ReviewService extends IService<Review> {

    Result<String> addReview(Review review);

    Result<List<Review>> getReviewsByMovieId(Long movieId);

    Result<String> likeReview(Long id);

    Result<String> unlikeReview(Long id);

    Result<Review> getReview(Long id);

    Result<String> updateReview(Long id, Review review);

    Result<String> deleteReview(Long id);
}