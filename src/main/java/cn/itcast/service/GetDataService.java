package cn.itcast.service;

import cn.itcast.util.JedisUtil;
import cn.itcast.util.UiBean;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Service
public class GetDataService {
    Jedis jedis= JedisUtil.getJedis();
    public String getData(){
        Map<String,String>testData=jedis.hgetAll("bussiness::order::total");
        String []produceId=new String[10];
        String[]producetSumPrice=new String[10];
        int i=0;
        for (Map.Entry<String,String>entry:testData.entrySet()){
            produceId[i]=entry.getKey();
            producetSumPrice[i]=entry.getValue();
            i++;
        }
        UiBean ub=new UiBean();
        ub.setProduceId(produceId);
        ub.setProducetSumPrice(producetSumPrice);
        return JSONObject.toJSONString(ub);
    }
}
