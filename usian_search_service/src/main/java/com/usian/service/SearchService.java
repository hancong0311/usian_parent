package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import com.usian.mapper.TbItemMapper;
import com.usian.util.ESUtil;
import com.usian.util.JsonUtils;
import com.usian.vo.ItemESVO;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sun.tools.internal.xjc.reader.Ring.add;

/**
 * @Title: SearchService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/19 14:19
 */
@Service
public class SearchService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private ESUtil esUtil;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 将数据库中的商品信息，加入到es
     *
     *
     */
    public void importAll()  {
        // 查一部（一页），导入导一部分
        int pageNum = 1;
        while(true){
            //1. 查询mysql
            PageHelper.startPage(pageNum,1000);
            List<ItemESVO> itemESVOS = itemMapper.queryAll4ES();

            if(itemESVOS==null || itemESVOS.size()==0){
                break;
            }
            //2. 存入到es  操作es
            //2.1 判断索引是否存在，不存在创建结构
            if(!esUtil.existsIndex("usian")){
                esUtil.createIndex(1,0,"usian","item","{\n" +
                        "  \"_source\": {\n" +
                        "    \"excludes\": [\n" +
                        "      \"item_desc\"\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"properties\": {\n" +
                        "    \"id\": {\n" +
                        "       \"type\": \"keyword\",\n" +
                        "       \"index\": false\n" +
                        "    },\n" +
                        "    \"item_title\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    },\n" +
                        "    \"item_sell_point\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    },\n" +
                        "    \"item_price\": {\n" +
                        "      \"type\": \"float\"\n" +
                        "    },\n" +
                        "    \"item_image\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"index\": false\n" +
                        "    },\n" +
                        "    \"item_category_name\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    },\n" +
                        "    \"item_desc\": {\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"analyzer\": \"ik_max_word\",\n" +
                        "      \"search_analyzer\": \"ik_smart\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
            }
            //2.1 ，将数据存入es中

            BulkRequest bulkRequest = new BulkRequest();
            itemESVOS.forEach(e->{ // 商品对象 ---》商品json串
                 bulkRequest.add(new IndexRequest("usian", "item").source(JsonUtils.objectToJson(e),
                    XContentType.JSON));
        });
            try {
                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 如果查询不到了，代表已经全部导入成功了，就结束该循环

            pageNum++;
        }


    }

    /**
     * 搜索的核心代码 查询es
     * @param q
     * @return
     */
    public List<ItemESVO> search(String q) {
        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("usian");
        searchRequest.types("item");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(q,"item_title","item_sell_point","item_desc"));
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        List<HighlightBuilder.Field> fields = highlightBuilder.fields();
        fields.add(new HighlightBuilder.Field("item_title"));
        fields.add(new HighlightBuilder.Field("item_sell_point"));
        fields.add(new HighlightBuilder.Field("item_desc"));

        searchSourceBuilder.highlighter(highlightBuilder);

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        ArrayList<ItemESVO> data = new ArrayList<>();
        // 执行搜索
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            // 匹配的文档
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            // 日期格式化对象
            for (SearchHit hit : searchHits) {
                // 源文档内容
                String source = hit.getSourceAsString();//json 串
                ItemESVO itemESVO = JsonUtils.jsonToPojo(source, ItemESVO.class);


                //处理高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null) {
                    HighlightField highlightField = highlightFields.get("item_title");
                    if(highlightField!=null){
                        Text[] fragments = highlightField.getFragments();
                        itemESVO.setItemTitle(fragments[0].toString());
                    }

                     highlightField = highlightFields.get("item_sell_point");
                    if(highlightField!=null){
                        Text[] fragments = highlightField.getFragments();
                        itemESVO.setItemSellPoint(fragments[0].toString());
                    }
                    highlightField = highlightFields.get("item_desc");
                    if(highlightField!=null){
                        Text[] fragments = highlightField.getFragments();
                        itemESVO.setItemDesc(fragments[0].toString());
                    }

                }

                data.add(itemESVO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
