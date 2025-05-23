package cn.itcast.processdata

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool
import java.util.Properties

object RedisClient {
    val props=new Properties()
    props.load(this.getClass.getClassLoader.getResourceAsStream("redis.properties"))
    val redisHost:String =props.getProperty("jedis.host")
    val redisPort:String =props.getProperty("jedis.port")
    val redisTimeout:String =props.getProperty("jedis.max.wait.millis")
    lazy val pool: JedisPool = new JedisPool(new GenericObjectPoolConfig(), redisHost, redisPort.toInt, redisTimeout.toInt)
  lazy val hook = new Thread {
    override def run() = {
      println("执行关闭钩子...")
      if (!pool.isClosed) {
        pool.destroy()
      }
    }
  }

  sys.addShutdownHook(hook.start())

  sys.addShutdownHook(hook.start())
}
