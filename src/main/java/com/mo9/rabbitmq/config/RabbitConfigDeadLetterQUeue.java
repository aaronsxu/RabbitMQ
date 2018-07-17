package com.mo9.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitConfigDeadLetterQUeue {
	 /**
     * 死信队列 交换机标识符
     */
    private static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
	
	 /**
     * 死信队列跟交换机类型没有关系 不一定为directExchange  不影响该类型交换机的特性.
     * @return the exchange
     */
    @Bean("deadLetterExchange")
    public Exchange deadLetterExchange() {
//        return ExchangeBuilder.directExchange("DL_EXCHANGE").durable(true).build();
        return ExchangeBuilder.topicExchange("E.LOCAL.T.MQtest.DeadLetter").durable(true).build();
    }
    
    /**
     * 声明一个死信队列.
     * x-dead-letter-exchange   对应  死信交换机
     * x-dead-letter-routing-key  对应 死信队列
     *
     * @return the queue
     */
    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    声明  死信交换机
        args.put("x-dead-letter-exchange", "E.LOCAL.T.MQtest.DeadLetter");
//       x-dead-letter-routing-key    声明 死信路由键
        args.put("x-dead-letter-routing-key", "KEY_dead");
        return QueueBuilder.durable("Q.LOCAL.DURABLE.MQtest.DeadLetter").withArguments(args).build();
    }

    /**
     * 定义死信队列转发队列.
     * @return the queue
     */
    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable("Q.LOCAL.DURABLE.MQtest.Redirect").build();
    }

    /**
     * 死信路由通过 DL_KEY 绑定键绑定到死信队列上.
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding("Q.LOCAL.DURABLE.MQtest.DeadLetter", Binding.DestinationType.QUEUE, "E.LOCAL.T.MQtest.DeadLetter", "DL_KEY", null);

    }
  
    
    /**
     * 死信路由通过 KEY_R 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding("Q.LOCAL.DURABLE.MQtest.Redirect", Binding.DestinationType.QUEUE, "E.LOCAL.T.MQtest.DeadLetter", "KEY_dead", null);
    }
    
    
    
    
	    @Bean("exchage1")
	    public TopicExchange exchange() {
	    	return new TopicExchange("E.LOCAL.T.MQtest.DeadLetter2",true,false);
	    }
	    @Bean("CMessage2")
	    public Queue CMessage() {
	        return new Queue("Q.LOCAL.DURABLE.MQtest.Redirect2",true);
	    }
	    @Bean
	    public Binding redirectBinding2(Queue CMessage2, TopicExchange exchage1) {
	    	return BindingBuilder.bind(CMessage2).to(exchage1).with("redirectBinding2");
	    }
    

}
