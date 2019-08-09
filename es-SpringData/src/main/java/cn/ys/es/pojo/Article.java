package cn.ys.es.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "index_blog", type = "article")
@Data
@ToString
public class Article {

    @Id
    @Field(type = FieldType.Long, index = true, store = true)//index 默认为true，可以不写
    private long id;

    @Field(type = FieldType.text, index = true, store = true, analyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.text, index = true, store = true, analyzer = "ik_smart")
    private String content;
}
