package com.doureview.review.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.common.Result;
import com.doureview.review.dto.ReviewResponseDTO;
import com.doureview.review.dto.ReviewSaveDTO;
import com.doureview.review.entity.Review;
import com.doureview.review.mapper.ReviewMapper;
import com.doureview.review.service.ReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Override
    public Result<String> addReview(ReviewSaveDTO dto) {
        Review review = new Review();
        BeanUtils.copyProperties(dto, review);
        save(review);
        return Result.success("发布成功");
    }

    @Override
    public Result<List<ReviewResponseDTO>> getReviewsByMovieId(Long movieId) {
        List<Review> reviews = lambdaQuery().eq(Review::getMovieId, movieId).list();
        List<ReviewResponseDTO> dtos = reviews.stream().map(review -> {
            ReviewResponseDTO dto = new ReviewResponseDTO();
            BeanUtils.copyProperties(review, dto);
            return dto;
        }).collect(Collectors.toList());
        return Result.success(dtos);
    }

    @Override
    public Result<String> likeReview(Long id) {
        Review review = getById(id);
        if (review == null) return Result.fail("影评不存在");
        review.setLikeCount(review.getLikeCount() + 1);
        updateById(review);
        return Result.success("点赞成功");
    }

    @Override
    public Result<String> unlikeReview(Long id) {
        Review review = getById(id);
        if (review == null) return Result.fail("影评不存在");
        int count = Math.max(0, review.getLikeCount() - 1);
        review.setLikeCount(count);
        updateById(review);
        return Result.success("取消点赞成功");
    }

    @Override
    public Result<ReviewResponseDTO> getReview(Long id) {
        Review review = getById(id);
        if (review == null) return Result.fail("影评不存在");
        ReviewResponseDTO dto = new ReviewResponseDTO();
        BeanUtils.copyProperties(review, dto);
        return Result.success(dto);
    }

    @Override
    public Result<String> updateReview(Long id, ReviewSaveDTO dto) {
        Review review = new Review();
        BeanUtils.copyProperties(dto, review);
        review.setId(id);
        updateById(review);
        return Result.success("更新成功");
    }

    @Override
    public Result<String> deleteReview(Long id) {
        removeById(id);
        return Result.success("删除成功");
    }
}