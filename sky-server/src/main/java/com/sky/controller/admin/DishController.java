package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation(value = "新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品，菜品数据：{}", dishDTO);

        dishService.saveWithFlavor(dishDTO);

        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation(value = "菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询，参数为：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @ApiOperation(value = "菜品批量删除")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除：{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

}
