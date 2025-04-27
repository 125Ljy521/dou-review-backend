package com.doureview.review.listener;

import com.doureview.config.RabbitMQConfig;
import com.doureview.review.dto.ReviewMessageDTO;
import com.doureview.review.service.impl.ReviewServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 消费者：监听点赞/取消点赞消息队列，
 * 调用 ServiceImpl 中的幂等方法更新数据库
 */
@Component
public class ReviewMessageListener {

    private final ReviewServiceImpl reviewServiceImpl;

    public ReviewMessageListener(ReviewServiceImpl reviewServiceImpl) {
        this.reviewServiceImpl = reviewServiceImpl;
    }

    /**
     * 监听点赞队列，处理并自增点赞数
     */
    @RabbitListener(queues = RabbitMQConfig.LIKE_QUEUE)
    public void handleLike(ReviewMessageDTO msg) {
        reviewServiceImpl.incrementLikeCount(msg.getReviewId());
    }

    /**
     * 监听取消点赞队列，处理并自减点赞数
     */
    @RabbitListener(queues = RabbitMQConfig.UNLIKE_QUEUE)
    public void handleUnlike(ReviewMessageDTO msg) {
        reviewServiceImpl.decrementLikeCount(msg.getReviewId());
    }
}