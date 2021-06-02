package com.usian.service;

import com.usian.constant.BigADConstant;
import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbItemCat;
import com.usian.vo.BigADContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: ItemCatService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 9:07
 */
@Service
public class ContentCatService {

    @Autowired
    private TbContentCategoryMapper contentCatMapper;

    @Autowired
    private TbContentMapper contentMapper;

    public List<TbContentCategory> selectContentCategoryByParentId(Long parentId) {

        // 方式一
//        TbItemCat tbItemCat = new TbItemCat();
//        tbItemCat.setParentId(parentId);
//
//        List<TbItemCat> result = itemCatMapper.select(tbItemCat);


        Example example = Example.builder(TbContentCategory.class)
                .where(Sqls.custom().andEqualTo("parentId",parentId).andEqualTo("status",1))
                .orderByAsc("sortOrder","name")
                .build();


        List<TbContentCategory> result = contentCatMapper.selectByExample(example);
        return result;


    }

    @Transactional
    public void insertContentCategory(Long parentId, String name) {

        //新增新节点
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);

        // 设置一些默认值
        tbContentCategory.setStatus(1);
        tbContentCategory.setIsParent(false);
        Date now = new Date();
        tbContentCategory.setCreated(now);
        tbContentCategory.setSortOrder(1);

        contentCatMapper.insertSelective(tbContentCategory);

        // 修改父
        //1. 判断 父，原来是不是父（查询父的信息：根据主键查询）
        TbContentCategory fatherNode = contentCatMapper.selectByPrimaryKey(parentId);

        //2. 如果不是父，改成 父
        if(!fatherNode.getIsParent()){
            fatherNode.setIsParent(true);
            fatherNode.setUpdated(now);
            contentCatMapper.updateByPrimaryKey(fatherNode);
        }
    }

    /*
             父1
                 子1
                     子子11

     */
    @Transactional
    public void deleteContentCategoryById(Long categoryId) {
        //0. 判断该节点是否有子节点
        //  select * from tb_content_cateogry where parent_id = #{categoryId}
        List<TbContentCategory> sunNodes = selectContentCategoryByParentId(categoryId);
        if(sunNodes!=null && sunNodes.size()>0){
            return;// 不进行删除操作
        }



        //1. 删除子节点 state 1 -- >2  update
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(categoryId);
        tbContentCategory.setStatus(2);
        Date now = new Date();
        tbContentCategory.setUpdated(now);
        contentCatMapper.updateByPrimaryKeySelective(tbContentCategory);

        //2. 修改父节点，是否还是父?
        // 2.0 查询到父节点 的 id   select parent_id from tb_content_category where id = categoryId
        //2.1 查询活着的字节点 // select *  from tb_content_category  where parent_id = xxx?
        // select * from tb_content_category where parent_id =
        //    (select parent_id from b_content_category where id = #{category_id});  子查询

        // SELECT a.*
        //  FROM tb_content_category a  # 全部的
        //  LEFT JOIN  tb_content_category b   ON a.`parent_id` = b. `parent_id`
        //  WHERE b.id =94;
        List<TbContentCategory> childNodes = contentCatMapper.findByParentId(categoryId);


        // 2，2 如果没有了，把父变为子节点
        if(childNodes==null || childNodes.size()==0){
            // 把父变为子节点
            // UPDATE  tb_content_category SET is_parent = 0 WHERE id =( SELECT parent_id FROM  (SELECT b.parent_id FROM tb_content_category b WHERE b.id = #{id}) a);
            contentCatMapper.updateParentStatusByChildId(categoryId);

        }



    }

    public List<BigADContentVO>   selectFrontendContentByAD() {
        //1. 查询 大广告内容    TbContent 的集合  select * from tb_content where category_id = 89
        TbContent tbContent = new TbContent();//创建一个查询条件对象
        tbContent.setCategoryId(BigADConstant.BIG_AD_CATEGORY_ID);
        List<TbContent> oldAds = contentMapper.select(tbContent);
        //2. TbContent集合----》BigADContentVO类型
        List<BigADContentVO> newAds = new ArrayList<>();
        oldAds.forEach(oldAd -> {
            BigADContentVO newAd = new BigADContentVO();
            newAd.setHeight(BigADConstant.COMMON_HEIGHT);
            newAd.setHeightB(BigADConstant.COMMON_HEIGHT_B);
            newAd.setWidth(670);
            newAd.setWidthB(550);
            newAd.setSrc(oldAd.getPic());
            newAd.setSrcB(oldAd.getPic2());
            newAd.setHref(oldAd.getUrl());
            newAd.setAlt(oldAd.getContent());
            newAds.add(newAd);
        });
        return newAds;

    }
}
