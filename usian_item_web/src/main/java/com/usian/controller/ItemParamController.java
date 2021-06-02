package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItemParam;
import com.usian.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: ItemParamController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 10:23
 */
@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {

    @Autowired
    private ItemFeign itemFeign;

    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
       TbItemParam itemParam =  itemFeign.selectItemParamByItemCatId(itemCatId);
       return Result.ok(itemParam);
    }
}
