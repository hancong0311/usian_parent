package com.usian.controller;

import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbItemCat;
import com.usian.service.ContentCatService;
import com.usian.vo.BigADContentVO;
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
@RequestMapping("/content")
public class ContentCatController {

    @Autowired
    private ContentCatService contentCatService;



    @RequestMapping("selectFrontendContentByAD")
    public List<BigADContentVO> selectFrontendContentByAD(){
     return   contentCatService.selectFrontendContentByAD();
    }

    @RequestMapping("/deleteContentCategoryById")
    public void deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){
        contentCatService.deleteContentCategoryById(categoryId);
    }

    @RequestMapping("insertContentCategory")
    public void insertContentCategory(@RequestParam("parentId") Long parentId,@RequestParam("name") String name){
        contentCatService.insertContentCategory(parentId,name);
    }
    /**
     * 根据父类目ID，查询所有的当前的孩子节点
     * @param id
     * @return
     */
    @RequestMapping("selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(@RequestParam(name="id") Long id){

        return contentCatService.selectContentCategoryByParentId(id);
    }
}
