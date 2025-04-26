package com.doureview.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.movie.dto.MovieResponseDTO;
import com.doureview.movie.dto.MovieSaveDTO;
import com.doureview.movie.entity.Movie;

import java.util.List;

public interface MovieService extends IService<Movie> {

    Result<String> saveMovie(MovieSaveDTO dto);

    Result<List<MovieResponseDTO>> listMovies();

    Result<MovieResponseDTO> getMovieById(Long id);

    Result<String> updateMovie(Long id, MovieSaveDTO dto);

    Result<String> deleteMovie(Long id);
}