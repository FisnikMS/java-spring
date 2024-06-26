package com.fk.notification.config.rabbitMQ;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fk.notification.config.rabbitMQ.domain.Event;
import com.fk.notification.config.rabbitMQ.domain.NotificationUpdateEvent;

@Configuration
public class RabbitConfiguration {
  public static final String topicExchangeName = "notification-exchange";
  public static final String queueName = "notification-creation-queue";
  public static final String routingKey = "notification.creation.event";

  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  MessageConverter messageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

    DefaultClassMapper classMapper = new DefaultClassMapper();
    Map<String, Class<?>> map = new HashMap<>();
    map.put(
        "com.fk.application.config.rabbitMQ.domain.Event",
        Event.class);

    map.put(
      "com.fk.application.config.rabbitMQ.domain.NotificationUpdateEvent",
      NotificationUpdateEvent.class
    ); 
    classMapper.setIdClassMapping(map);

    classMapper.setTrustedPackages("*");
    classMapper.setDefaultType(Event.class);
    converter.setClassMapper(classMapper);

    return converter;
  }

  @Bean
  public RabbitTemplate template(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }
}
