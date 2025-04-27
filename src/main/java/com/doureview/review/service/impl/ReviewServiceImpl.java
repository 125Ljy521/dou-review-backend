package com.doureview.review.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.common.Result;
import com.doureview.config.RabbitMQConfig;
import com.doureview.review.dto.ReviewMessageDTO;
import com.doureview.review.dto.ReviewResponseDTO;
import com.doureview.review.dto.ReviewSaveDTO;
import com.doureview.review.entity.Review;
import com.doureview.review.mapper.ReviewMapper;
import com.doureview.review.service.ReviewService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 影评业务实现类
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    private final RabbitTemplate rabbitTemplate;

    public ReviewServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /** 发布影评 */
    @Override
    public Result<String> addReview(ReviewSaveDTO dto) {
        Review review = new Review();
        BeanUtils.copyProperties(dto, review);
        save(review);
        return Result.success("发布成功");
    }

    /** 根据电影ID查询影评列表 */
    @Override
    public Result<List<ReviewResponseDTO>> getReviewsByMovieId(Long movieId) {
        List<Review> reviews = lambdaQuery()
                .eq(Review::getMovieId, movieId)
                .list();
        List<ReviewResponseDTO> dtos = reviews.stream().map(r -> {
            ReviewResponseDTO rd = new ReviewResponseDTO();
            BeanUtils.copyProperties(r, rd);
            return rd;
        }).collect(Collectors.toList());
        return Result.success(dtos);
    }

    /** 发送点赞消息 */
    @Override
    public Result<String> likeReview(Long id) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.LIKE_ROUTING_KEY,
                new ReviewMessageDTO(id)
        );
        return Result.success("点赞消息发送成功");
    }

    /** 发送取消点赞消息 */
    @Override
    public Result<String> unlikeReview(Long id) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.UNLIKE_ROUTING_KEY,
                new ReviewMessageDTO(id)
        );
        return Result.success("取消点赞消息发送成功");
    }

    /** 查询单个影评 */
    @Override
    public Result<ReviewResponseDTO> getReview(Long id) {
        Review review = getById(id);
        if (review == null) {
            return Result.fail("影评不存在");
        }
        ReviewResponseDTO dto = new ReviewResponseDTO();
        BeanUtils.copyProperties(review, dto);
        return Result.success(dto);
    }

    /** 更新影评 */
    @Override
    public Result<String> updateReview(Long id, ReviewSaveDTO dto) {
        Review review = new Review();
        BeanUtils.copyProperties(dto, review);
        review.setId(id);
        updateById(review);
        return Result.success("更新成功");
    }

    /** 删除影评 */
    @Override
    public Result<String> deleteReview(Long id) {
        removeById(id);
        return Result.success("删除成功");
    }

    // （MQ 消费者会调用以下两个方法来完成数据库更新，故无需 @Override）

    /** 幂等自增点赞数 */
    public void incrementLikeCount(Long reviewId) {
        update(new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Review>()
                .setSql("like_count = like_count + 1")
                .eq("id", reviewId)
        );
    }

    /** 幂等自减点赞数，不低于 0 */
    public void decrementLikeCount(Long reviewId) {
        Review r = getById(reviewId);
        if (r != null) {
            int count = Math.max(0, r.getLikeCount() - 1);
            r.setLikeCount(count);
            updateById(r);
        }
    }
}