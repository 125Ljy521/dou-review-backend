package com.doureview.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.common.Result;
import com.doureview.movie.dto.MovieResponseDTO;
import com.doureview.movie.dto.MovieSaveDTO;
import com.doureview.movie.entity.Movie;
import com.doureview.movie.mapper.MovieMapper;
import com.doureview.movie.service.MovieService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

    @Override
    public Result<String> saveMovie(MovieSaveDTO dto) {
        Movie movie = new Movie();
        BeanUtils.copyProperties(dto, movie);
        save(movie);
        return Result.success("添加成功");
    }

    @Override
    public Result<List<MovieResponseDTO>> listMovies() {
        List<Movie> movies = list();
        List<MovieResponseDTO> dtos = movies.stream().map(movie -> {
            MovieResponseDTO dto = new MovieResponseDTO();
            BeanUtils.copyProperties(movie, dto);
            return dto;
        }).collect(Collectors.toList());
        return Result.success(dtos);
    }

    @Override
    public Result<MovieResponseDTO> getMovieById(Long id) {
        Movie movie = getById(id);
        if (movie == null) return Result.fail("电影不存在");
        MovieResponseDTO dto = new MovieResponseDTO();
        BeanUtils.copyProperties(movie, dto);
        return Result.success(dto);
    }

    @Override
    public Result<String> updateMovie(Long id, MovieSaveDTO dto) {
        Movie movie = new Movie();
        BeanUtils.copyProperties(dto, movie);
        movie.setId(id);
        updateById(movie);
        return Result.success("更新成功");
    }

    @Override
    public Result<String> deleteMovie(Long id) {
        removeById(id);
        return Result.success("删除成功");
    }
}