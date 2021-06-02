package com.usian.controller;

import com.usian.api.ContentFeign;
import com.usian.pojo.TbContentCategory;
import com.usian.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @Title: ItemCategoryController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 8:58
 */
@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {


    @Autowired
    private ContentFeign contentFeign;


    @RequestMapping("deleteContentCategoryById")
    public Result deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){

        contentFeign.deleteContentCategoryById(categoryId);
        return Result.ok();
    }


    /**
     * 新增 内容的分类
     * @param parentId  父分类ID
     * @param name  新分类的名字
     * @return
     */
    @RequestMapping("insertContentCategory")
    public Result insertContentCategory(@RequestParam("parentId") Long parentId,@RequestParam("name") String name){
        contentFeign.insertContentCategory(parentId,name);
        return Result.ok();
    }
    /**
     *
     * @param id  父内容类目ID
     * @return
     */
    @RequestMapping("selectContentCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(name="id",defaultValue = "0") Long id){

       List<TbContentCategory>  contCats = contentFeign.selectContentCategoryByParentId(id);

       return Result.ok(contCats);
    }




}
