package com.usian.api;

import com.usian.vo.ItemESVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Title: SearchFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/20 8:45
 */
@FeignClient("usian-search-service")
public interface SearchFeign {

    @RequestMapping("search/list")
    public List<ItemESVO> search(@RequestParam("q") String q);
}
