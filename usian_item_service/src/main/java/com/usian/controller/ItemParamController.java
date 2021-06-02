package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: ItemParamController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 10:26
 */
@RestController
@RequestMapping("/itemParam")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;
    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){

        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }
}
