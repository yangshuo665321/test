package cn.ys.mongo.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 *  吐槽
 */
@Data
public class Spit implements Serializable {

    @Id
    private String _id; // ID
    private String content; // 吐槽内容
    private Date publishtime; // 发布日期
    private String userid; // 发布人ID
    private String nickname; // 发布人昵称
    private Integer visits; // 浏览量
    private Integer thumbup; // 点击数
    private Integer share; // 分享数
    private Integer comment; // 点击数
    private String state; // 是否可见
    private String parentid; // 上级ID
}
