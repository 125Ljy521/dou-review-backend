package com.doureview.movie.controller;

import com.doureview.common.Result;
import com.doureview.movie.entity.Movie;
import com.doureview.movie.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电影模块接口层
 * 负责接收前端请求 & 返回结果
 */
@RestController
@RequestMapping("/movie")
@Api(tags = "电影模块")
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * 新增电影
     */
    @ApiOperation("新增电影")
    @PostMapping
    public Result<String> addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    /**
     * 查询所有电影
     */
    @ApiOperation("查询所有电影")
    @GetMapping("/list")
    public Result<List<Movie>> list() {
        return Result.ok(movieService.list());
    }

    /**
     * 根据 ID 查询电影详情
     */
    @ApiOperation("根据 ID 查询电影详情")
    @GetMapping("/{id}")
    public Result<Movie> getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    /**
     * 更新电影信息
     */
    @ApiOperation("更新电影信息")
    @PutMapping("/{id}")
    public Result<String> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    /**
     * 删除电影
     */
    @ApiOperation("删除电影")
    @DeleteMapping("/{id}")
    public Result<String> deleteMovie(@PathVariable Long id) {
        return movieService.deleteMovie(id);
    }
}