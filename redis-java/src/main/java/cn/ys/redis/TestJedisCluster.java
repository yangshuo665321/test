package cn.ys.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class TestJedisCluster {
    public static void main(String[] args) {
        // 第一步：使用JedisCluster对象。需要一个Set<HostAndPort>参数。Redis节点的列表。
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.25.11", 7001));
        nodes.add(new HostAndPort("192.168.25.11", 7002));
        nodes.add(new HostAndPort("192.168.25.11", 7003));
        nodes.add(new HostAndPort("192.168.25.11", 7004));
        nodes.add(new HostAndPort("192.168.25.11", 7005));
        nodes.add(new HostAndPort("192.168.25.11", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        // 第二步：直接使用JedisCluster对象操作redis。在系统中单例存在。
        jedisCluster.set("hello", "100");
        String result = jedisCluster.get("hello");
        // 第三步：打印结果
        System.out.println(result);
        // 第四步：系统关闭前，关闭JedisCluster对象。
        jedisCluster.close();
    }
}
