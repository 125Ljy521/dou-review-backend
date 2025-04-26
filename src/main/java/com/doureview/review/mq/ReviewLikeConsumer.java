package com.doureview.review.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doureview.review.entity.Review;
import com.doureview.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 监听点赞消息，异步更新数据库中的点赞数
 */
@Slf4j
@Component
public class ReviewLikeConsumer {

    @Resource
    private ReviewService reviewService;

    /**
     * 监听 review.like.queue 队列，接收影评点赞的 reviewId
     */
    @RabbitListener(queues = "review.like.queue")
    public void handleReviewLike(Long reviewId) {
        log.info("接收到影评点赞消息，reviewId: {}", reviewId);

        Review review = reviewService.getById(reviewId);
        if (review == null) {
            log.warn("影评不存在，reviewId: {}", reviewId);
            return;
        }

        review.setLikeCount(review.getLikeCount() + 1);
        reviewService.updateById(review);
        log.info("影评点赞数更新成功");
    }
}