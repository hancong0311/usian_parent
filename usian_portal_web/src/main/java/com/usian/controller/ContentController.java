package com.usian.controller;

import com.usian.api.ContentFeign;
import com.usian.util.Result;
import com.usian.vo.BigADContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: ContentController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/14 14:10
 */
@RequestMapping("/frontend/content")
@RestController
public class ContentController {

    @Autowired
    private ContentFeign contentFeign;

    /**
     * 查询首页打广告信息
     * @return
     */
    @RequestMapping("selectFrontendContentByAD")
    public Result selectFrontendContentByAD(){
      List<BigADContentVO> ads =  contentFeign.selectFrontendContentByAD();
      return Result.ok(ads);
    }
}
