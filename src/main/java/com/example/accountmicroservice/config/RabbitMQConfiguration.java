package com.example.accountmicroservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange("authExchange");
    }

    @Bean
    public Queue authRequestQueue() {
        return new Queue("authRequestQueue", true);
    }

    @Bean
    public Queue authResponseQueue() {
        return new Queue("authResponseQueue", true);
    }

    @Bean
    public Binding bindingAuthRequest(Queue authRequestQueue, TopicExchange authExchange) {
        return BindingBuilder.bind(authRequestQueue).to(authExchange).with("auth.request");
    }

    @Bean
    public Binding bindingAuthResponse(Queue authResponseQueue, TopicExchange authExchange) {
        return BindingBuilder.bind(authResponseQueue).to(authExchange).with("auth.response");
    }
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
