package com.doureview.review.controller;

import com.doureview.common.Result;
import com.doureview.review.entity.Review;
import com.doureview.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 影评模块接口层
 * 负责接收请求和返回结果
 */
@RestController
@RequestMapping("/review")
@Api(tags = "影评模块")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 发布影评
     */
    @ApiOperation("发布影评")
    @PostMapping
    public Result<String> addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    /**
     * 根据电影ID查询影评列表
     */
    @ApiOperation("根据电影ID查询影评列表")
    @GetMapping("/movie/{movieId}")
    public Result<List<Review>> getReviewsByMovieId(@PathVariable Long movieId) {
        return reviewService.getReviewsByMovieId(movieId);
    }

    /**
     * 点赞影评
     */
    @ApiOperation("点赞影评")
    @PostMapping("/{id}/like")
    public Result<String> likeReview(@PathVariable Long id) {
        return reviewService.likeReview(id);
    }

    /**
     * 取消点赞
     */
    @ApiOperation("取消点赞")
    @PostMapping("/{id}/unlike")
    public Result<String> unlikeReview(@PathVariable Long id) {
        return reviewService.unlikeReview(id);
    }

    /**
     * 查询单个影评详情
     */
    @ApiOperation("查询单个影评详情")
    @GetMapping("/{id}")
    public Result<Review> getReview(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    /**
     * 更新影评
     */
    @ApiOperation("更新影评")
    @PutMapping("/{id}")
    public Result<String> updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    /**
     * 删除影评
     */
    @ApiOperation("删除影评")
    @DeleteMapping("/{id}")
    public Result<String> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }
}