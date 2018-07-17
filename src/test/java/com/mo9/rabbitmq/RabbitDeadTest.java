package com.mo9.rabbitmq;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.mo9.rabbitmq.config.RabbitmqConfig;
import com.alibaba.fastjson.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
@EnableAutoConfiguration
public class RabbitDeadTest {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 测试死信队列.
	 * @param p
	 * @return the response entity
	 */
	@RequestMapping("/dead")
	public ResponseEntity deadLetter(String p) {
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		// 声明消息处理器 这个对消息进行处理 可以设置一些参数 对消息进行一些定制化处理 我们这里 来设置消息的编码 以及消息的过期时间
		// 因为在.net 以及其他版本过期时间不一致 这里的时间毫秒值 为字符串
		MessagePostProcessor messagePostProcessor = message -> {
			MessageProperties messageProperties = message.getMessageProperties();
			// 设置编码
			messageProperties.setContentEncoding("utf-8");
			// 设置过期时间10*1000毫秒
			messageProperties.setExpiration("10000");
			return message;
		};
		// 向DL_QUEUE 发送消息 10*1000毫秒后过期 形成死信
		rabbitTemplate.convertAndSend("E.LOCAL.T.MQtest.DeadLetter", "DL_KEY", p, messagePostProcessor, correlationData);
		return ResponseEntity.ok();
	}

	@Test
	public void asd(){
		System.out.println(23);
	}
	
	
	/**
	 * 确认机制 ConfirmCallbackListener 会收到确认的信息
	 */
	@Test
	public void confirmTest() {
		Content content = new Content();
		content.setOrderCode("a1od9211");
		content.setUserCode("sxu");
		System.out.println("===========================================1=====================================");
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setMessageId(UUID.randomUUID().toString());
		messageProperties.setTimestamp(new Date());
		Message message = new Message(SerializationUtils.serialize(content),messageProperties);
		CorrelationData correlationData = new CorrelationData(messageProperties.getMessageId());
//		rabbitTemplate.send("E.LOCAL.T.MQtest.DeadLetter", "DL_KEY", message, correlationData);
		
//		MessagePostProcessor messagePostProcessor = message2 -> {
//			MessageProperties messageProperties2 = message.getMessageProperties();
//			messageProperties.setContentEncoding("utf-8");
//			messageProperties.setExpiration("10000");
//			return message;
//		};
//		rabbitTemplate.convertAndSend("E.LOCAL.T.MQtest.DeadLetter", "DL_KEY", message, null, correlationData);
		
//		JSONObject request = this.getRequest("aaaa");
		rabbitTemplate.convertAndSend("E.LOCAL.T.MQtest.DeadLetter", "DL_KEY", message, correlationData);
		System.out.println("===========================================1=====================================");
	}
	
	public byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			if (object != null){
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unused")
	private JSONObject getRequest(String message) {
		JSONObject request = new JSONObject();
		request.put("timestamp", System.currentTimeMillis());
		request.put("messageId", UUID.randomUUID().toString());
		request.put("message", message);
		return request;
	}


}
