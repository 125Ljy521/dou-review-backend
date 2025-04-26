package com.doureview.movie.dto;

import lombok.Data;

@Data
public class MovieResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String director;
}