package org.example.dynamicqueues.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;

/**
 * Author: otabek
 * Date: 16/12/23 12:24
 */
@Service
@Log4j2
public class RabbitQueueServiceImpl implements RabbitQueueService {


    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitListenerEndpointRegistry
            rabbitListenerEndpointRegistry;


    @Override
    public void addNewQueue(String queueName, String exchangeName, String routingKey) {
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
        if (queueProperties == null) {
            Queue queue = new Queue(queueName, true, false, false);
            Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);

            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(binding);
        }

        this.addQueueToListener(exchangeName, queueName);
    }

    @Override
    public void addQueueToListener(String listenerId, String queueName) {
        log.info("adding queue : " + queueName + " to listener with id : " + listenerId);

        if (!checkQueueExistOnListener(listenerId, queueName)) {
            AbstractMessageListenerContainer container = this.getMessageListenerContainerById(listenerId);
            container.addQueueNames(queueName);
            log.info("queue is being listened by " + listenerId + " listener");
        } else {
            log.info("given queue name : " + queueName + " not exist on given listener id");
        }
    }

    @Override
    public void removeQueueFromListener(String listenerId, String queueName) {

        log.info("removing queue: " + queueName + " from listener: " + listenerId);

        if (checkQueueExistOnListener(listenerId, queueName)) {
            AbstractMessageListenerContainer container = this.getMessageListenerContainerById(listenerId);
            container.removeQueueNames(queueName);

            log.info("deleting queue from rabbit management");
            this.rabbitAdmin.deleteQueue(queueName);
        } else {
            log.info("queue: " + queueName + " is not being listened by: " + queueName + "\naction is cancelled");
        }
    }

    @Override
    public Boolean checkQueueExistOnListener(String listenerId, String queueName) {
        try {
            log.info("checking queueName : " + queueName + " exist on listener id : " + listenerId);
            log.info("getting queueNames");
            String[] queueNames = this.getMessageListenerContainerById(listenerId).getQueueNames();
            log.info("queueNames : " + Arrays.toString(queueNames));
            log.info("checking " + queueName + " exist on active queues");
            for (String name : queueNames) {
                log.info("name : " + name + " with checking name : " + queueName);
                if (name.equals(queueName)) {
                    log.info("queue name exist on listener, returning true");
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            log.error("Error on checking queue exist on listener");
            log.error("error message : " + e);
            return Boolean.FALSE;
        }
    }


    private AbstractMessageListenerContainer getMessageListenerContainerById(String listenerId) {
        log.info("getting message listener container by id : " + listenerId);
        return ((AbstractMessageListenerContainer) this.rabbitListenerEndpointRegistry
                .getListenerContainer(listenerId)
        );
    }
}
