package cn.itcast.createorder;

import com.alibaba.fastjson.JSONObject;

import java.util.Random;
import java.util.UUID;

public class PaymentInfo {
    private static final long SerialVersionUId=1L;
    private String orderId;
    private String productId;
    private long productPrice;
    public PaymentInfo(){

    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
    //模拟订单数据
    public String random(){
        Random r=new Random();
        this.orderId= UUID.randomUUID().toString().replaceAll("-","");
        this.productPrice=r.nextInt(1000);
        this.productId=r.nextInt(10)+"";
        JSONObject obj =new JSONObject();
        String jsonString =obj.toJSONString(this);
        return jsonString;
    }
}
