package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: DetailController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/21 13:59
 */
@RestController
@RequestMapping("frontend/detail")
public class DetailController {

    @Autowired
    private ItemFeign itemFeign;


    @RequestMapping("selectItemInfo")
    public Result selectItemInfo(@RequestParam("itemId") Long itemId){

        TbItem item = itemFeign.selectItemInfo(itemId);
        return Result.ok(item);
    }


    @RequestMapping("selectTbItemParamItemByItemId")
    public Result selectTbItemParamItemByItemId(@RequestParam("itemId") Long itemId){
        TbItemParamItem itemParamItem = itemFeign.selectTbItemParamItemByItemId(itemId);
        return Result.ok(itemParamItem);
    }


    @RequestMapping("selectItemDescByItemId")
    public Result selectItemDescByItemId(@RequestParam("itemId") Long itemId){
        TbItemDesc itemDesc= itemFeign.selectItemDescByItemId(itemId);
        return Result.ok(itemDesc);
    }

}
