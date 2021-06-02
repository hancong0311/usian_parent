package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.dto.ItemDto;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.*;
import com.usian.util.IDUtils;
import com.usian.util.PageResult;
import com.usian.util.RedisClient;
import com.usian.vo.ItemVO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;
import java.util.List;

/**
 * @Title: ItemService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/10 10:10
 */
@Service
public class ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisClient redisClient;

    /**
     * 查询商品基本信息  string
     *                   key  value
     *                   hash
     * @param id
     * @return
     */
    public TbItem queryById(Long id) {
        TbItem item = (TbItem) redisClient.get("ITEM_" + id);

        if(item!=null){
            return item;
        }

        //加 分布式锁
        if(redisClient.lock("ITEM_LOCK"+id,"")){//获取到锁
            redisClient.expire("ITEM_LOCK"+id,2);
            item = itemMapper.selectByPrimaryKey(id);//null
            if(item == null){  //解决缓存穿透的问题
                item = new TbItem();
                redisClient.set("ITEM_"+id,item);
                redisClient.expire("ITEM_"+id,2);
            }else{
                redisClient.set("ITEM_"+id,item);
                redisClient.expire("ITEM_"+id,60*60*24);
            }

            redisClient.del("ITEM_LOCK"+id);
        }else{//没有获取到锁
            queryById(id);
        }

        return item;
    }

    /**
     * 查询商品列表的
     * @param page
     * @param rows
     * @return
     */
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {



        PageHelper.startPage(page,rows);
        List<TbItem> tbItems = itemMapper.selectAll();
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);

        // pageInfo ----> PageResult
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageInfo.getList());
        pageResult.setPageIndex(page);
        pageResult.setTotalPage(pageInfo.getPages());

        return pageResult;

    }

    /*
            程序使用  @Transactional 一定会生效吗？
     */
    @Transactional
    public void insertTbItem(ItemVO itemVO) {
        // 1. item
        // itemVO 商品对象

        long itemId = IDUtils.genItemId();
        Date now = new Date();
        itemVO.setId(itemId);
        itemVO.setCreated(now);
        itemVO.setStatus((byte)1);
        itemMapper.insertSelective(itemVO);

        //2. item_desc
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(itemVO.getDesc());
        itemDesc.setCreated(now);
        itemDescMapper.insertSelective(itemDesc);


        //3. item_param_item
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemVO.getItemParams());
        itemParamItem.setCreated(now);
        itemParamItemMapper.insertSelective(itemParamItem);


        ItemDto itemDto = new ItemDto();
        itemDto.setItem(itemVO);
        itemDto.setTbItemDesc(itemDesc);
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(itemVO.getCid());
        itemDto.setCategoryName(itemCat.getName());
        // 新增数据到es
        amqpTemplate.convertAndSend("item_exchange","item.insert",itemDto);


    }

    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {



        Example example = Example.builder(TbItemParamItem.class)
                .where(Sqls.custom().andEqualTo("itemId",itemId))
                .build();
        List<TbItemParamItem> tbItemParamItems = itemParamItemMapper.selectByExample(example);

        if(tbItemParamItems!=null && tbItemParamItems.size()>0){
            return tbItemParamItems.get(0);
        }
        return null;

    }

    public TbItemDesc selectItemDescByItemId(Long itemId) {


        return itemDescMapper.selectByPrimaryKey(itemId);

    }
}
