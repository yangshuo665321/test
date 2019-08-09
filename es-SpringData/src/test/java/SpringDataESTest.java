import cn.ys.es.dao.ArticleDao;
import cn.ys.es.pojo.Article;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataESTest {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 创建索引
     * @throws Exception
     */
    @Test
    public void createIndex() throws Exception {
        //创建索引，并配置映射关系
        template.createIndex(Article.class);
        //如果创建索引时没有配置映射关系，单独配置映射关系，使用下面的操作
        //template.putMapping(Article.class);
    }

    /**
     * 添加文档或更新文档，更新文档会先删除后插入
     * @throws Exception
     */
    @Test
    public void addDocument() throws Exception {
        for (int i = 1; i < 100; i++) {
            //创建一个Article对象
            Article article = new Article();
            article.setId(i);
            article.setTitle(i + "Spring Data框架");
            article.setContent(i + "Spring Data JPA 框架，主要针对的就是 Spring 唯一没有简化到的业务逻辑代码，" +
                    "至此，开发者连仅剩的实现持久层业务逻辑的工作都省了，唯一要做的，就只是声明持久层的接口，" +
                    "其他都交给 Spring Data JPA 来帮你完成！");
            //把文档写入索引库
            articleDao.save(article);
        }
    }

    /**
     * 删除文档
     * @throws Exception
     */
    @Test
    public void deleteDocument() throws Exception {
        articleDao.deleteById(1l);
    }

    /**
     * 根据ID查询文档
     * @throws Exception
     */
    @Test
    public void findById() throws Exception {
        Article article = articleDao.findById(1l).get();
        System.out.println(article);
    }

    /**
     * 查询全部文档
     * @throws Exception
     */
    @Test
    public void findAll() throws Exception {
        Iterable<Article> articles = articleDao.findAll();
        articles.forEach(article -> System.out.println(article));
    }

    /**
     * 自定义查询
     * @throws Exception
     */
    @Test
    public void testFindByTitle() throws Exception {
        List<Article> articles = articleDao.findByTitle("1Spring");
        articles.forEach(article -> System.out.println(article));
    }

    /**
     * 自定义查询
     * @throws Exception
     */
    @Test
    public void testFindByTitleOrContent() throws Exception {
        List<Article> articles = articleDao.findByTitleOrContent("1Spring", "19Spring Data JPA 框架");
        articles.forEach(article -> System.out.println(article));
    }

    /**
     * 自定义查询
     * @throws Exception
     */
    @Test
    public void testPageable() throws Exception {
        Pageable pageable = PageRequest.of(1, 20);
        List<Article> articles = articleDao.findByTitleOrContent("Spring", "Spring Data JPA 框架", pageable);
        articles.forEach(article -> System.out.println(article));
    }

    /**
     * 测试NativeSearchQuery查询
     * @throws Exception
     */
    @Test
    public void testNativeSearchQuery() throws Exception {
        //创建一个查询对象
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("14Spring Data JPA 框架").defaultField("title"))
                .withPageable(PageRequest.of(0, 15))
                .build();
        //执行查询
        List<Article> articles = template.queryForList(query, Article.class);
        articles.forEach(article -> System.out.println(article));
    }
}
