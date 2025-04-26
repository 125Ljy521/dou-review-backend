package com.doureview.review.dto;

import lombok.Data;

@Data
public class ReviewSaveDTO {
    private Long movieId;
    private String content;
    private Integer score;
}