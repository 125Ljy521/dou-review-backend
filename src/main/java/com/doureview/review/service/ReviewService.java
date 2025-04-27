package com.doureview.review.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.review.dto.ReviewResponseDTO;
import com.doureview.review.dto.ReviewSaveDTO;
import com.doureview.review.entity.Review;

import java.util.List;

/**
 * 影评业务接口
 */
public interface ReviewService extends IService<Review> {

    /** 发布影评 */
    Result<String> addReview(ReviewSaveDTO dto);

    /** 根据电影ID查询影评列表 */
    Result<List<ReviewResponseDTO>> getReviewsByMovieId(Long movieId);

    /** 发送点赞消息 */
    Result<String> likeReview(Long id);

    /** 发送取消点赞消息 */
    Result<String> unlikeReview(Long id);

    /** 查询单个影评 */
    Result<ReviewResponseDTO> getReview(Long id);

    /** 更新影评 */
    Result<String> updateReview(Long id, ReviewSaveDTO dto);

    /** 删除影评 */
    Result<String> deleteReview(Long id);
}