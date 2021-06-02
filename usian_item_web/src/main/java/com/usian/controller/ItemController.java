package com.usian.controller;

import com.usian.api.ItemFeign;
import com.usian.pojo.TbItem;
import com.usian.util.PageResult;
import com.usian.util.Result;
import com.usian.vo.ItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: ItemController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/10 13:55
 */
@RestController
@RequestMapping("/backend/item/")
@Api(value = "商品模块")
public class ItemController {

    @Autowired
    private ItemFeign itemFeign;


    /**
     * 新增商品
     * @param itemVO
     * @return
     */
    @RequestMapping("insertTbItem")
    @ApiOperation(value = "新增商品接口")
    public Result insertTbItem(ItemVO itemVO){
        itemFeign.insertTbItem(itemVO);
        return Result.ok();
    }

    @GetMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(name = "page",defaultValue = "1") Integer page,@RequestParam(name="rows",defaultValue = "2") Integer rows){

       PageResult pageResult =  itemFeign.selectTbItemAllByPage(page,rows);
        return Result.ok(pageResult);
    }

    @GetMapping("/selectItemInfo")

    public Result selectItemInfo(@RequestParam("itemId") Long itemId){
        TbItem item = null;
        try {
            item = itemFeign.selectItemInfo(itemId);
            return Result.ok(item);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

}
