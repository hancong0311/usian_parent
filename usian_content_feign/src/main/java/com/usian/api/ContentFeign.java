package com.usian.api;

import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemParam;
import com.usian.vo.BigADContentVO;
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
@FeignClient("usian-content-service")
public interface ContentFeign {


    @RequestMapping("/content/selectFrontendContentByAD")
    public List<BigADContentVO> selectFrontendContentByAD();


    /*
    删除 某个分类
     */
    @RequestMapping("/content/deleteContentCategoryById")
    public void deleteContentCategoryById(@RequestParam("categoryId") Long categoryId);

    @RequestMapping("content/insertContentCategory")
    public void insertContentCategory(@RequestParam("parentId") Long parentId,@RequestParam("name") String name);
    @RequestMapping("/content/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(@RequestParam(name = "id") Long id);


}
