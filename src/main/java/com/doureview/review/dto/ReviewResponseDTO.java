package com.doureview.review.dto;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Long movieId;
    private String content;
    private Integer score;
    private Integer likeCount;
}