package cn.ys.es;

import cn.ys.es.pojo.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class ESClientTest {

    @Test
    public void createIndex() throws Exception {
        //1、创建一个Settings对象，相当于是一个配置信息。主要配置集群的名称。
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch-cluster")//设置集群名称
                .build();
        //2、创建一个客户端Client对象
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9302));
        //3、使用client对象创建一个索引库
        client.admin().indices().prepareCreate("index_hello")//设置条件
                .get();// .get()执行操作
        //4、关闭client对象
        client.close();
    }

    @Test
    public void setMappings() throws Exception {
        //1、创建一个Settings对象，相当于是一个配置信息。主要配置集群的名称。
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch-cluster")//设置集群名称
                .build();
        //2、创建一个客户端Client对象
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9302));

        //创建一个mappings信息
        /**
         * {
         *     "article":{
         *         "properties":{
         *             "id":{
         *                 "type":"long",
         *                 "store":true
         *             },
         *             "title":{
         *                 "type":"text",
         *                 "store":true,
         *                 "index":true,
         *                 "analyzer":"ik_smart"
         *             },
         *             "content":{
         *                 "type":"text",
         *                 "store":true,
         *                 "index":true,
         *                 "analyzer":"ik_smart"
         *             }
         *         }
         *     }
         * }
         */
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()//相当于{
                    .startObject("article")
                        .startObject("properties")
                            .startObject("id")
                                .field("type", "long")
                                .field("store", true)
                            .endObject()
                            .startObject("id")
                                .field("type", "text")
                                .field("store", true)
                                .field("index", true)
                                .field("analyzer", "ik_smart")
                            .endObject()
                            .startObject("id")
                                .field("type", "text")
                                .field("store", true)
                                .field("index", true)
                                .field("analyzer", "ik_smart")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();

        //3、使用client对象把mapping信息设置到索引库中
        client.admin().indices()
                //设置要做映射的索引
                .preparePutMapping("index_hello")
                //设置要做映射的type
                .setType("article")
                //mapping信息，可以是XContentBuilder对象可以是json格式的字符串
                .setSource(builder)
                //执行操作
                .get();
        //4、关闭client对象
        client.close();
    }

    private TransportClient client;

    @Before
    public void init() throws Exception {
        //1、创建一个Settings对象，相当于是一个配置信息。主要配置集群的名称。
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es")//设置集群名称
                .build();
        //2、创建一个客户端Client对象
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("120.78.134.111"), 9302));
    }

    //添加文档
    @Test
    public void testAddDocument() throws Exception {
        //创建一个client对象
        //创建一个文档对象
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .field("id", 1l)
                    .field("title", "ElasticSearch 是一个基于Lucene的开源搜索引擎")
                    .field("content", "Elasticsearch 所涉及到的每一项技术都不是创新或者革命性的，全文搜索，分析系统以及分布式数据库这些早就已经存在了。")
                .endObject();

        //把文档对象添加到索引库
        client.prepareIndex()
                //设置索引名称
                .setIndex("index_hello")
                //设置Type
                .setType("article")
                //设置文档的id，如果不设置的话，会自动生成一个id
                .setId("1")
                //设置文档信息
                .setSource(builder)
                //执行操作
                .get();

        //4、关闭client对象
        client.close();
    }

    //添加文档的第二张方式
    @Test
    public void testAddDocument2() throws Exception {
        //创建一个Article对象
        Article article = new Article();
        //设置对象的属性
        article.setId(3l);
        article.setTitle("ElasticSearch 是一个基于Lucene的开源搜索引擎");
        article.setContent("Elasticsearch 所涉及到的每一项技术都不是创新或者革命性的，全文搜索");
        //把article对象转换成json格式的字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDocument = objectMapper.writeValueAsString(article);
        //使用client对象把文档写入索引库
        client.prepareIndex("index_hello", "article", "3")
                .setSource(jsonDocument, XContentType.JSON)
                .get();
        //关闭客户端
        client.close();
    }

}
