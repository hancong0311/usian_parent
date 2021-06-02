package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.util.Result;
import com.usian.vo.ItemCategoryDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: ItemCategoryController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/14 9:37
 */
@RestController
@RequestMapping("frontend/itemCategory/")
public class ItemCategoryController {

    @Autowired
    private ItemFeign itemFeign;

    @RequestMapping("selectItemCategoryAll")
    public Result selectItemCategoryAll(){

       ItemCategoryDataVO data =  itemFeign.selectItemCategoryAll();
       return Result.ok(data);

    }
}
