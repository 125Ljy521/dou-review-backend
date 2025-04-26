package com.doureview.movie.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Movie {
    private Long id;
    private String title;
    private String director;
    private Integer releaseYear;
    private String cover;
    private LocalDateTime createTime;
}