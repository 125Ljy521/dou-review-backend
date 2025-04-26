package com.doureview.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.movie.entity.Movie;

/**
 * 电影模块业务层接口
 */
public interface MovieService extends IService<Movie> {

    Result<String> addMovie(Movie movie);

    Result<Movie> getMovie(Long id);

    Result<String> updateMovie(Long id, Movie movie);

    Result<String> deleteMovie(Long id);
}