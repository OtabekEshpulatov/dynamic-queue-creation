package org.example.dynamicqueues.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dynamicqueues.listeners.TgQueueListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Service;

/**
 * Author: otabek
 * Date: 16/12/23 12:24
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class RabbitQueueServiceImpl implements RabbitQueueService {

    private final RabbitAdmin rabbitAdmin;


    @Override
    public void addNewQueue(String queueName, String exchangeName, String routingKey) {
        try {
            Queue queue = new Queue(queueName, true, false, false);
            Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);

            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(binding);
        } catch (Exception ex) {
            log.error(ex);
        }

        this.registerListener(queueName);
    }


    private void registerListener(String queueName) {
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(rabbitAdmin.getRabbitTemplate().getConnectionFactory());
        listenerContainer.addQueueNames(queueName);
        configureListener(listenerContainer);

        listenerContainer.start();
    }

    private static void configureListener(SimpleMessageListenerContainer listenerContainer) {
        listenerContainer.setPrefetchCount(1);
        listenerContainer.setMessageListener(new TgQueueListener());
    }
}
