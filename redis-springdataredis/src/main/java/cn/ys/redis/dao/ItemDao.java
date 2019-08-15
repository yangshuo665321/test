package cn.ys.redis.dao;

import cn.ys.redis.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDao extends JpaRepository<Item, Long> {
}
