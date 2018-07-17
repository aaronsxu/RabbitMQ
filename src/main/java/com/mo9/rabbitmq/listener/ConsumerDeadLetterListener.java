package com.mo9.rabbitmq.listener;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class ConsumerDeadLetterListener {
	
	
	/**
     * 监听死信队列.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = "Q.LOCAL.DURABLE.MQtest.DeadLetter")
    public void DeadLetter(Message message, Channel channel) throws IOException {
    	try {
			System.out.println("11111111111111111111111111");
			String body = new String(message.getBody());
			System.out.println("Q.LOCAL.DURABLE.MQtest.DeadLetter  : " + body);
//			Thread.sleep(10000);
			int i=1/0;
			System.out.println("hahahhaha");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("catch============================");
		}
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        log.debug("dead message  10s 后 消费消息 {}",new String (message.getBody()));
    }
	
	/**
     * 监听替补队列 来验证死信.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = "Q.LOCAL.DURABLE.MQtest.Redirect")
    public void redirect(Message message, Channel channel) throws IOException {
    	System.out.println("2222222222222222222222222222222222222222");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        log.debug("dead message  10s 后 消费消息 {}",new String (message.getBody()));
    }


}
