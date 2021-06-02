package com.usian.service;

import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Title: ItemParamService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 10:28
 */
@Service
public class ItemParamService {

    @Autowired
    private TbItemParamMapper itemParamMapper;
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {

        TbItemParam itemParam = new TbItemParam();
        itemParam.setItemCatId(itemCatId);

        return itemParamMapper.selectOne(itemParam);
    }
}
