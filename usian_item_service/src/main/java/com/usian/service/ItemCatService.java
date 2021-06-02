package com.usian.service;

import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.util.RedisClient;
import com.usian.vo.ItemCategoryDataVO;
import com.usian.vo.ItemCateoryDefVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.TargetAwareIdentifierAccessor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Title: ItemCatService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/11 9:07
 */
@Service
public class ItemCatService {


    public static int i = 1;
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private RedisClient redisClient;

    public List<TbItemCat> selectItemCategoryByParentId(Long parentId) {

        // 方式一
//        TbItemCat tbItemCat = new TbItemCat();
//        tbItemCat.setParentId(parentId);
//
//        List<TbItemCat> result = itemCatMapper.select(tbItemCat);


        Example example = Example.builder(TbItemCat.class)
                .where(Sqls.custom().andEqualTo("parentId",parentId))
                .build();


        List<TbItemCat> result = itemCatMapper.selectByExample(example);
        return result;


    }

    /**
     * 组装所有的分类信息的核心代码
     * @return
     *      1  0  test
     *      2   0   test2
     *
     *
     *      test
     *      test2
     *
     *      Hash
     *
     *      INDEX_CATEGORY_COLLCETION  id  具体的值
     *
     *
     *
     *
     *
     *
     */
    public ItemCategoryDataVO selectItemCategoryAll() {
        //1. 先去缓存中，有，直接返回
//        ItemCategoryDataVO data = (ItemCategoryDataVO) redisClient.get("INDEX_CATEGORY_LIST");
//        if(data!=null){
//            return data;
//        }
        //2. 没有了，去数据库找


        ItemCategoryDataVO data = new ItemCategoryDataVO();
        if(redisClient.exists("INDEX_CATEGORY_COLLCETION")) {//存在
            List list = redisClient.hgetAll("INDEX_CATEGORY_COLLCETION");
            data.setData(list);
            return data;
        }

        List list = selectItemCategoryDefVoByParentId(0L);//从数据库查找
        // 查询所有的一级分类的集合
        data.setData(list);// 所有以及分类集合？  集合的个数？
        //3. 查到放入到redis,并返回

        //map 的key  value
        //    自增的数字        分类的名字/分类的ItemCateoryDefVO的对象
//        HashMap map = new HashMap();
//        int i=1;
//        for (Object o : list) {
//            map.put(i++,o);
//        }



        //   a b c d
//        Collectors.toMap(e-> {return XX::getId;}, e ->e);

        // e  代表遍历集合时的 某一个元素
       Map map = (Map) list.stream().collect(Collectors.toMap(e ->{
            //  key 的生成策略于 e 有关
            if (e instanceof String ){
                return e;
             }
            ItemCateoryDefVO e2 = (ItemCateoryDefVO)e;
            return  e2.getN();
            // key的生成策略时自增的
//            return  ItemCatService.i++ +"";
        } ,e ->e));


        redisClient.hsetAll("INDEX_CATEGORY_COLLCETION",map);
//        redisClient.set("INDEX_CATEGORY_LIST",data);

        return data;

    }

    /*
       查询一级的分离
     */
    public List selectItemCategoryDefVoByParentId(Long parentId){//0

        List<TbItemCat> itemCats = selectItemCategoryByParentId(parentId);// 1  0  test   2 0 test

        List itemNewCats = new ArrayList();//新分类，类型的集合

        // 将旧类型  ItemCat ---> ItemCategoryDefVO/String
        for (TbItemCat itemCat : itemCats) {
            // 当前节点是否是叶子节点
            if(!itemCat.getIsParent()){
                itemNewCats.add(itemCat.getName());

            }else{
                ItemCateoryDefVO defVO = new ItemCateoryDefVO();//某一个分类
                defVO.setN(itemCat.getName());// name --- n

                // 查找当前这个分类下的所有的子类
                // select *  from  xx where parent_id = itemCat.getId()  IbItemCat
                List<ItemCateoryDefVO> itemCateoryDefVOS = selectItemCategoryDefVoByParentId(itemCat.getId());
                defVO.setI(itemCateoryDefVOS);
                itemNewCats.add(defVO);
            }
        }
        return itemNewCats;

    }


}
