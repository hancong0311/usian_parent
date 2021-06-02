package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItemCat;
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
@RequestMapping("/backend/itemCategory")
public class ItemCategoryController {


    @Autowired
    private ItemFeign itemFeign;
    /**
     *
     * @param id  父类目ID
     * @return
     */
    @RequestMapping("selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(name="id",defaultValue = "0") Long id){

       List<TbItemCat>  itemCats = itemFeign.selectItemCategoryByParentId(id);

       return Result.ok(itemCats);
    }


}
