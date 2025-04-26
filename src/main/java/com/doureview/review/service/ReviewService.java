package com.doureview.review.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.review.dto.ReviewResponseDTO;
import com.doureview.review.dto.ReviewSaveDTO;
import com.doureview.review.entity.Review;

import java.util.List;

public interface ReviewService extends IService<Review> {

    Result<String> addReview(ReviewSaveDTO dto);

    Result<List<ReviewResponseDTO>> getReviewsByMovieId(Long movieId);

    Result<String> likeReview(Long id);

    Result<String> unlikeReview(Long id);

    Result<ReviewResponseDTO> getReview(Long id);

    Result<String> updateReview(Long id, ReviewSaveDTO dto);

    Result<String> deleteReview(Long id);
}