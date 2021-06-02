package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.ItemService;
import com.usian.util.PageResult;
import com.usian.vo.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: ItemController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/10 10:09
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(@RequestParam("itemId") Long itemId){
        return itemService.selectItemDescByItemId(itemId);
    }
    @RequestMapping("selectTbItemParamItemByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId") Long itemId){
       return   itemService.selectTbItemParamItemByItemId(itemId);
    }

    @RequestMapping("insertTbItem")
    public void insertTbItem(@RequestBody ItemVO itemVO){
        itemService.insertTbItem(itemVO);
    }
    @RequestMapping("queryById")
    public TbItem queryById(Long id){
        return itemService.queryById(id);
    }

    @GetMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name="rows") Integer rows){
        return itemService.selectTbItemAllByPage(page,rows);
    }
}
