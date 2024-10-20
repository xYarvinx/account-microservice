package com.example.accountmicroservice.config;

import com.example.accountmicroservice.dto.TokenValidationRequest;
import com.example.accountmicroservice.dto.TokenValidationResponse;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {


    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange("authExchange");
    }

    @Bean
    public TopicExchange roleExchange() {
        return new TopicExchange("roleExchange");
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
    public Queue roleRequestQueue() {
        return new Queue("roleRequestQueue", true);
    }

    @Bean
    public Queue roleResponseQueue() {
        return new Queue("roleResponseQueue", true);
    }


    @Bean
    public Binding bindingAuthRequest(Queue authRequestQueue, TopicExchange authExchange) {
        return BindingBuilder.bind(authRequestQueue).to(authExchange).with("auth.request.*");
    }

    @Bean
    public Binding bindingAuthResponse(Queue authResponseQueue, TopicExchange authExchange) {
        return BindingBuilder.bind(authResponseQueue).to(authExchange).with("auth.response.*");
    }

    @Bean
    public Binding bindingRoleRequest(Queue roleRequestQueue, TopicExchange roleExchange) {
        return BindingBuilder.bind(roleRequestQueue).to(roleExchange).with("role.request.*");
    }

    @Bean
    public Binding bindingRoleResponse(Queue roleResponseQueue, TopicExchange roleExchange) {
        return BindingBuilder.bind(roleResponseQueue).to(roleExchange).with("role.response.*");
    }


    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }


    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("TokenValidationRequest", TokenValidationRequest.class);
        idClassMapping.put("TokenValidationResponse", TokenValidationResponse.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
