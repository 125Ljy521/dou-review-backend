package com.doureview.review.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {
    private Long id;
    private Long userId;
    private Long movieId;
    private String content;
    private Integer likeCount;
    private LocalDateTime createTime;
}