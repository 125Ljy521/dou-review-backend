package com.doureview.review.controller;

import com.doureview.common.Result;
import com.doureview.review.dto.ReviewResponseDTO;
import com.doureview.review.dto.ReviewSaveDTO;
import com.doureview.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 影评模块接口层
 * 只负责接收请求与返回结果，业务逻辑由 ReviewService 实现
 */
@RestController
@RequestMapping("/review")
@Api(tags = "影评模块")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @ApiOperation("发布影评")
    @PostMapping
    public Result<String> addReview(@RequestBody ReviewSaveDTO dto) {
        return reviewService.addReview(dto);
    }

    @ApiOperation("根据电影ID查询影评列表")
    @GetMapping("/movie/{movieId}")
    public Result<List<ReviewResponseDTO>> getReviewsByMovieId(@PathVariable Long movieId) {
        return reviewService.getReviewsByMovieId(movieId);
    }

    @ApiOperation("点赞影评")
    @PostMapping("/{id}/like")
    public Result<String> likeReview(@PathVariable Long id) {
        return reviewService.likeReview(id);
    }

    @ApiOperation("取消点赞影评")
    @PostMapping("/{id}/unlike")
    public Result<String> unlikeReview(@PathVariable Long id) {
        return reviewService.unlikeReview(id);
    }

    @ApiOperation("查询单个影评详情")
    @GetMapping("/{id}")
    public Result<ReviewResponseDTO> getReview(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    @ApiOperation("更新影评")
    @PutMapping("/{id}")
    public Result<String> updateReview(@PathVariable Long id, @RequestBody ReviewSaveDTO dto) {
        return reviewService.updateReview(id, dto);
    }

    @ApiOperation("删除影评")
    @DeleteMapping("/{id}")
    public Result<String> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }
}