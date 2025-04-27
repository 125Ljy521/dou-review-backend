package com.doureview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞/取消点赞消息载体 DTO，
 * 只包含要处理的影评 ID
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMessageDTO {
    /**
     * 被点赞或取消点赞的影评 ID
     */
    private Long reviewId;
}