package cn.itcast.websocket;

import cn.itcast.service.GetDataService;
import cn.itcast.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.websocket.OnMessage;
import javax.websocket.Session;

@ServerEndpoint("/uiwebSocket")
public class UiWebSocket {
//    静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount=0;
//    concurrent包的线程安全Set集合，用来存放每个客户端对应的MyWebSocket对象
//    若要实现服务端与单一客户端通信吧的化，可以使用Map来存放,其中Key可以为用户标识
    private static CopyOnWriteArraySet<UiWebSocket>webSocketSet=new CopyOnWriteArraySet<UiWebSocket>();
//    与某个客户端的连接和会话,需要通过它来给客户端发送数据
    @Autowired
    private GetDataService getDataService;
    private Session session;
    // 构造函数中手动注入
    public UiWebSocket() {
        this.getDataService = SpringContextUtils.getBean("getDataService", GetDataService.class);
    }
//    建立连接成功时调用
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        try {
            String data = getDataService.getData();
            session.getBasicRemote().sendText(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    连接断开时调用方法
    @OnClose
    public void onClose(Session session) {
        ScheduledExecutorService scheduler = (ScheduledExecutorService) session.getUserProperties().get("scheduler");
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }


    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println("来自客户端的消息"+message);
//        群发消息
        for (final UiWebSocket item:webSocketSet){
            try {
                while(true){
                    item.sendMessage(getDataService.getData());
                    Thread.sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }
//    出错时调用
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }
//    根据自己需要添加的方法
    public void  sendMessage(String message)throws IOException{
        this.session.getBasicRemote().sendText(message);
    }
//    获取连接数
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    //    添加连接数
    public static synchronized void addOnlineCount(){
        UiWebSocket.onlineCount++;
    }
    //    减少连接数
    public static synchronized void subOnlineCount(){
        UiWebSocket.onlineCount--;
    }
}
