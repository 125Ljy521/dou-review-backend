package com.doureview.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类：定义交换机、队列与路由绑定
 */
@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String EXCHANGE_NAME      = "review.exchange";
    // 点赞队列名称
    public static final String LIKE_QUEUE         = "review.like.queue";
    // 取消点赞队列名称
    public static final String UNLIKE_QUEUE       = "review.unlike.queue";
    // 点赞路由键
    public static final String LIKE_ROUTING_KEY   = "review.like";
    // 取消点赞路由键
    public static final String UNLIKE_ROUTING_KEY = "review.unlike";

    /**
     * 声明一个 Topic 类型的交换机，持久化
     */
    @Bean
    public TopicExchange reviewExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    /**
     * 声明点赞队列，持久化
     */
    @Bean
    public Queue likeQueue() {
        return new Queue(LIKE_QUEUE, true);
    }

    /**
     * 声明取消点赞队列，持久化
     */
    @Bean
    public Queue unlikeQueue() {
        return new Queue(UNLIKE_QUEUE, true);
    }

    /**
     * 将点赞队列绑定到交换机，使用点赞路由键
     */
    @Bean
    public Binding likeBinding(Queue likeQueue, TopicExchange reviewExchange) {
        return BindingBuilder.bind(likeQueue)
                .to(reviewExchange)
                .with(LIKE_ROUTING_KEY);
    }

    /**
     * 将取消点赞队列绑定到交换机，使用取消点赞路由键
     */
    @Bean
    public Binding unlikeBinding(Queue unlikeQueue, TopicExchange reviewExchange) {
        return BindingBuilder.bind(unlikeQueue)
                .to(reviewExchange)
                .with(UNLIKE_ROUTING_KEY);
    }
}