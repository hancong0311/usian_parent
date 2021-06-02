package com.usian.controller;

import com.usian.pojo.TbItemCat;
import com.usian.service.ItemCatService;
import com.usian.vo.ItemCategoryDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: ItemCatController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 9:06
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;


    /**
     * 返回首页需要的所有的商品分类信息
     * @return
     */
    @RequestMapping("/selectItemCategoryAll")
    public ItemCategoryDataVO selectItemCategoryAll(){
       return  itemCatService.selectItemCategoryAll();


    }
    /**
     * 根据父类目ID，查询所有的当前的孩子节点
     * @param id
     * @return
     */
    @RequestMapping("selectItemCategoryByParentId")
    public List<TbItemCat> selectItemCategoryByParentId(@RequestParam(name="id") Long id){

        return itemCatService.selectItemCategoryByParentId(id);
    }
}
