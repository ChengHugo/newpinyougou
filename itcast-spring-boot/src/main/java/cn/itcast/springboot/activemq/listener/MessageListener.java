package cn.itcast.springboot.activemq.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageListener {

    /**
     * 接收MQ的消息
     * @param map 接收的信息
     */
    @JmsListener(destination = "spring.boot.mq.queue")
    public void receiveMsg(Map<String, Object> map){
        System.out.println(map);

    }
}
