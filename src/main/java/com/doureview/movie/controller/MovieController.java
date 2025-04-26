package com.doureview.movie.controller;

import com.doureview.common.Result;
import com.doureview.movie.dto.MovieResponseDTO;
import com.doureview.movie.dto.MovieSaveDTO;
import com.doureview.movie.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@Api(tags = "电影模块")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @ApiOperation("添加电影")
    @PostMapping
    public Result<String> save(@RequestBody MovieSaveDTO dto) {
        return movieService.saveMovie(dto);
    }

    @ApiOperation("获取电影列表")
    @GetMapping("/list")
    public Result<List<MovieResponseDTO>> list() {
        return movieService.listMovies();
    }

    @ApiOperation("根据 ID 获取电影")
    @GetMapping("/{id}")
    public Result<MovieResponseDTO> getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @ApiOperation("更新电影")
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody MovieSaveDTO dto) {
        return movieService.updateMovie(id, dto);
    }

    @ApiOperation("删除电影")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return movieService.deleteMovie(id);
    }
}