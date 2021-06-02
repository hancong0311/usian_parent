package com.usian.api;

import com.usian.pojo.*;
import com.usian.util.PageResult;
import com.usian.util.Result;
import com.usian.vo.ItemCategoryDataVO;
import com.usian.vo.ItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: ItemFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/10 14:17
 */
@FeignClient("usian-item-service")
public interface ItemFeign {


    @RequestMapping("item/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(@RequestParam("itemId") Long itemId);

    /**
     * 查询商品对应的规格参数值
     * @param itemId
     * @return
     */
    @RequestMapping("item/selectTbItemParamItemByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId") Long itemId);
    /**
     * 返回首页需要的所有的商品分类信息
     * @return
     */
    @RequestMapping("/itemCat/selectItemCategoryAll")
    public ItemCategoryDataVO selectItemCategoryAll();

    @RequestMapping("item/insertTbItem")
    public void insertTbItem(@RequestBody ItemVO itemVO);
    @RequestMapping("/itemParam/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId);

    @RequestMapping("/itemCat/selectItemCategoryByParentId")
    public List<TbItemCat> selectItemCategoryByParentId(@RequestParam(name="id") Long id);

    @RequestMapping("/item/queryById")
    TbItem selectItemInfo(@RequestParam("id") Long id);

    @GetMapping("/item/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name="rows") Integer rows);
}
