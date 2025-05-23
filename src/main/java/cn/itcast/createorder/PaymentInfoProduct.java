package cn.itcast.createorder;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
public class PaymentInfoProduct {
    public static void main(String[] args) {
        Properties props=new Properties();
        props.put("bootstrap.servers","hadoop01:9092,hadoop02:9092,hadoop03:9092");
        props.put("acks","all");
        props.put("retries",0);
        props.put("batch.size","16384");
        props.put("linger.ms","1");
        props.put("buffer.memory",33554432);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer <String,String> kafkaProducer =new KafkaProducer <String,String> (props);
        PaymentInfo pay=new PaymentInfo();
        while(true){
            String message=pay.random();
            kafkaProducer.send(new ProducerRecord<String,String >("itcast_order",message));
            System.out.println("信息已经发送到Kafka:"+message);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
