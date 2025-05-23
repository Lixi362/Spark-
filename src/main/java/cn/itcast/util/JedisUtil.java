package cn.itcast.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

public class JedisUtil {
    private JedisUtil(){}
    private static JedisPool jedisPool;
    static{
        Properties props=new Properties();
        try{
            props.load(JedisUtil.class.getClassLoader().getResourceAsStream("redis.properties"));
            JedisPoolConfig poolConfig=new JedisPoolConfig();
            poolConfig.setMaxTotal(Integer.valueOf(props.getProperty("jedis.max.total")));
            poolConfig.setMaxIdle(Integer.valueOf(props.getProperty("jedis.max.idle")));
            poolConfig.setMinIdle(Integer.valueOf(props.getProperty("jedis.min.idle")));
            poolConfig.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.max.wait.millis")));
            String host= props.getProperty("jedis.host");
            int port=Integer.valueOf(props.getProperty("jedis.port"));
            jedisPool=new JedisPool(poolConfig,host,port,10000);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
    public static void returnJedis(Jedis jedis){
        jedis.close();
    }
}
