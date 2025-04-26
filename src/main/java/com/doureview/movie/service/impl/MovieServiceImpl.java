package com.doureview.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.common.Result;
import com.doureview.movie.entity.Movie;
import com.doureview.movie.mapper.MovieMapper;
import com.doureview.movie.service.MovieService;
import org.springframework.stereotype.Service;

/**
 * 电影模块业务层实现类
 */
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Override
    public Result<String> addMovie(Movie movie) {
        save(movie);
        return Result.ok("新增电影成功");
    }

    @Override
    public Result<Movie> getMovie(Long id) {
        Movie movie = getById(id);
        if (movie == null) {
            return Result.fail("电影不存在");
        }
        return Result.ok(movie);
    }

    @Override
    public Result<String> updateMovie(Long id, Movie movie) {
        movie.setId(id);
        updateById(movie);
        return Result.ok("更新成功");
    }

    @Override
    public Result<String> deleteMovie(Long id) {
        removeById(id);
        return Result.ok("删除成功");
    }
}