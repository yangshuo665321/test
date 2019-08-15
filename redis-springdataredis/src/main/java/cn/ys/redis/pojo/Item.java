package cn.ys.redis.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "item")
@Data
public class Item implements Serializable {

    @Id
    private Long id;
    private String name;
    private Double price;
}
