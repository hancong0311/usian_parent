package com.usian.controller;

import com.usian.api.SearchFeign;
import com.usian.util.JsonUtils;
import com.usian.vo.ItemESVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: SearchController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/20 8:40
 */
@RestController
@RequestMapping("/frontend/searchItem")
public class SearchController {

    @Autowired
    private SearchFeign searchFeign;

    /**
     * 首页搜索的接口
     * @param q
     * @return
     */
    @RequestMapping("list")
    public String search(String q){
        List<ItemESVO> esvos =  searchFeign.search(q);
        return JsonUtils.objectToJson(esvos);
    }
}
