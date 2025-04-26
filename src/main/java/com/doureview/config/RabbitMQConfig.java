package com.doureview.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 交换机名称 & 队列名称
    public static final String EXCHANGE = "review.fanout";
    public static final String QUEUE = "review.like.queue";

    /**
     * 声明交换机：扇出类型，广播式发送
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue likeQueue() {
        return new Queue(QUEUE);
    }

    /**
     * 队列绑定到交换机
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(likeQueue()).to(fanoutExchange());
    }
}