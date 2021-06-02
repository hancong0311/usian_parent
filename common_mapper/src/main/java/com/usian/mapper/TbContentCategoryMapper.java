package com.usian.mapper;

import com.usian.pojo.TbContentCategory;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbContentCategoryMapper extends Mapper<TbContentCategory> {


    /**
     * 查询 指定子节点 相同父的  所有的活着子节点
     * @param id
     * @return
     */
    List<TbContentCategory> findByParentId(Long id);

    /**
     * 修改指定子节点的父节点，为子节点
     * @param id
     */
    void updateParentStatusByChildId(Long id);
}
