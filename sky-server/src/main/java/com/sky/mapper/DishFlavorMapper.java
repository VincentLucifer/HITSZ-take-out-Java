package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味数据
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除口味数据
     * @param dishids
     */
    void deleteByIds(List<Long> dishids);

    /**
     * 根据菜品id查询口味数据
     * @param dishid
     * @return
     */
    List<DishFlavor> getByDishId(Long dishid);
}
