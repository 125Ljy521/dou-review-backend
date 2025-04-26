package com.doureview.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.auth.util.UserHolder;
import com.doureview.common.Result;
import com.doureview.config.RabbitMQConfig;
import com.doureview.review.entity.Review;
import com.doureview.review.mapper.ReviewMapper;
import com.doureview.review.service.ReviewService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 影评模块业务层实现类
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Override
    public Result<String> addReview(Review review) {
        save(review);
        return Result.ok("发布成功");
    }

    @Override
    public Result<List<Review>> getReviewsByMovieId(Long movieId) {
        List<Review> reviews = list(new QueryWrapper<Review>().eq("movie_id", movieId));
        return Result.ok(reviews);
    }

    private static final String REVIEW_LIKE_KEY = "REVIEW:LIKE:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Result<String> likeReview(Long reviewId) {
        // 获取当前登录用户 ID
        Long userId = UserHolder.get().getId();
        String key = REVIEW_LIKE_KEY + reviewId;

        // 判断是否已经点赞（幂等）
        Boolean isMember = redisTemplate.opsForSet().isMember(key, userId.toString());
        if (Boolean.TRUE.equals(isMember)) {
            return Result.fail("您已点赞过该影评");
        }

        // 添加点赞记录
        redisTemplate.opsForSet().add(key, userId.toString());

        // 发送 MQ 消息
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "", reviewId);
        return Result.ok("点赞成功");
    }

    @Override
    public Result<String> unlikeReview(Long id) {
        Review review = getById(id);
        if (review == null || review.getLikeCount() == 0) {
            return Result.fail("无可取消点赞");
        }
        review.setLikeCount(review.getLikeCount() - 1);
        updateById(review);
        return Result.ok("取消点赞成功");
    }

    @Override
    public Result<Review> getReview(Long id) {
        Review review = getById(id);
        if (review == null) {
            return Result.fail("影评不存在");
        }
        return Result.ok(review);
    }

    @Override
    public Result<String> updateReview(Long id, Review review) {
        review.setId(id);
        updateById(review);
        return Result.ok("更新成功");
    }

    @Override
    public Result<String> deleteReview(Long id) {
        removeById(id);
        return Result.ok("删除成功");
    }
}