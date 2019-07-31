package cn.ys.jd.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * jd_item 对应的实体类
 *
 * @author yangshuo
 */
@Entity
@Table(name = "jd_item")
@Data
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spu;
    private Long sku;
    private String title;
    private Double price;
    private String pic;
    private String url;
    private Date created;
    private Date updated;
}
